package trial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cspAlgorithms.AbstractCSPMethods;
import cspAlgorithms.SearchTriangle;
import cspAlgorithms.Common;
import cspAlgorithms.LARACMethod;
import cspAlgorithms.LARACMethodWithMD;
import cspAlgorithms.BiLAD;
import cspAlgorithms.YenMethod;
import fileInput.IdFile;
import randomTopology.XMLHelper;
import randomTopology.Constant;

/**
 * 
 * OverView:
 * 		 �������ָ���㷨�ıȽϣ��������д�뵽Excel��
 * 		 �����ṩ����Excel����������Excel�ļ�������
 */
public class MutilMethodsComparision {
	private String filename;// Excel����
	private String[] descrption = { "�ڵ���", "�ܱ���", "ƽ����", "��С��", "����", "��С��ʱ", "gama" };// Excelǰ�漸�е������ֶ�
	private String[] testargs = { "c", "d", "����dijkstra����", "����ʱ��/ms" };// ÿ������Ҫ���Ե���
	private String[] methodNames = { "LARAC", "BiLAD", "ExactBiLAD" };// ���Եķ���
	private int[] delayConstraint;// ��ʱԼ������ֵ���ڵ�ǰ��С��ʱ�½��е�����ֵ��Ϊ��ʱ��ֵ
	private double[] gama;// gama����
	private SwingWorker<String, String> ref;// ͼ�ν����е�����

	public MutilMethodsComparision() {// Ĭ��main�������ô˹��캯��
		// TODO Auto-generated constructor stub
		delayConstraint = new int[] { 1, 2, 3 };
		gama = new double[] { 0.05, 0.1 };
	}

	// �ù��캯����ͼ�ν�����е��õģ�ָ����ʱ���Ӿ��󡢲��Է�������gama���������Լ�����
	public MutilMethodsComparision(String[] methodNames, int[] delayConstraint, double[] gama,
			SwingWorker<String, String> ref) {
		this.methodNames = methodNames;
		this.delayConstraint = delayConstraint;
		this.gama = gama;
		this.ref = ref;
	}

	/**
	 * 
	 * Function:
	 *		���ķ�������������Excel�ļ���
	 * Details:
	 *		���ȼ���ļ�������ȡ�ļ�����β��������֣����ڴ������ϼ�һ������Linux�ϵ�����û�ʱ�����UID��
	 *		Ȼ����POI���в���������������̲������Ͻ̡̳�
	 * Remark: 2018��9��19�� ����1:50:01
	 */
	public void DesignExcel() throws FileNotFoundException, IOException {
		checkFilename();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("MultiMethodsComparision");
		// ���ᴰ��
		if (Boolean.parseBoolean(XMLHelper.getValue("//allInfo/test/isFreeze")))
			sheet.createFreezePane(0, 2);

		int baseRowNum = Constant.group * Constant.copy + 4;// ÿ��gamaռ������
		for (int k = 0; k < gama.length; k++) {// ����gama��������
			XMLHelper.setValue("//allInfo/test/gama", gama[k] + "");// ��gamaֵд��setting.xml�ļ���
																	// ��ֵ����д�����������Constant��gamaֵ
																	// �ο�randomTopology����XMLHelp���޸�xmlֵ�ķ���
			HSSFRow row = sheet.createRow(baseRowNum * k);
			int baseNum = 4 * methodNames.length + 1;// ����delay��׼
			// ���е�Ԫ��ĺϲ���ÿ�������ϲ�һ�ε�Ԫ��
			for (int i = 0; i < delayConstraint.length; i++) {
				for (int j = 0; j < methodNames.length; j++) {
					row.createCell(baseNum * i + testargs.length * j + descrption.length + 1)
							.setCellValue(methodNames[j]);
					CellRangeAddress address = new CellRangeAddress(k * baseRowNum, k * baseRowNum,
							baseNum * i + testargs.length * j + descrption.length + 1,
							baseNum * i + testargs.length * (j + 1) + descrption.length);
					sheet.addMergedRegion(address);
				}
			}
			// ��ʼ��������ֶ�
			row = sheet.createRow(baseRowNum * k + 1);
			for (int i = 0; i < descrption.length; i++) {// �������
				row.createCell(i).setCellValue(descrption[i]);
			}
			// ��ʼ��ӷ����ֶ�
			for (int i = 0; i < delayConstraint.length; i++) {// ÿ��Լ������ӷ���
				row.createCell(baseNum * i + descrption.length).setCellValue("delayconstraint");
				sheet.setColumnWidth(baseNum * i + descrption.length, 13 * 256);
				for (int j = 0; j < methodNames.length; j++) {// ÿ����������Ӳ��Բ���
					for (int h = 0; h < testargs.length; h++) {// ��Ӳ��Բ���
						row.createCell(baseNum * i + descrption.length + testargs.length * j + h + 1)
								.setCellValue(testargs[h]);
					}
					sheet.setColumnWidth(baseNum * i + descrption.length + testargs.length * j + testargs.length - 1,
							14 * 256);
					sheet.setColumnWidth(baseNum * i + descrption.length + testargs.length * j + testargs.length,
							11 * 256);
				}
			}

			for (int t = 0; t < Constant.group; t++) {
				for (int h = 0; h < Constant.copy; h++) {
					// ֪ͨMainRun�� ������task2�ļ�����
					if (ref != null)
						ref.firePropertyChange("progress", 0,
								(k * (Constant.group * Constant.copy) + t * Constant.copy + h + 1) * 100
										/ (Constant.group * Constant.copy * gama.length));
					// �������������
					row = sheet.createRow(baseRowNum * k + t * Constant.copy + h + 2);
					int nodenum = Constant.step * (t + 1);// baseNumNodes
					int[] Node = new int[nodenum];
					for (int s = 0; s < nodenum; s++)
						Node[s] = s;
					Constant.TimeForTest = t * Constant.copy + h + 1;// �������ݵ�index��֪ͨ�޸�TimeForTest
					double[][] Id = IdFile.GetId();
					int[][] IdLink = IdFile.GetIdLink(Id);
					int start = 2;
					int end = 16;

					// ���������ֶεľ���ֵ
					row.createCell(0).setCellValue(nodenum);// ���ýڵ���
					HSSFCell cell = row.createCell(1);
					cell.setCellValue(Id.length / 2);// �����ܱ���
					HSSFCellStyle style = workbook.createCellStyle();
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					cell = row.createCell(2);
					cell.setCellValue(AbstractCSPMethods.getAverageDegree(Node, Id) / 2);
					cell.setCellStyle(style);// ����ƽ����
					int[] MinAndMaxDegree = Common.GetMinAndMaxDegree(nodenum, Id);
					row.createCell(3).setCellValue(MinAndMaxDegree[0] / 2);// ������С��
					row.createCell(4).setCellValue(MinAndMaxDegree[1] / 2);// ��������
					double mindelay = new LARACMethod().GetMinDelay(Node, Id, IdLink, start, end);
					row.createCell(5).setCellValue(mindelay);// ������С��ʱ
					row.createCell(6).setCellValue(gama[k]);// ����gama
					int[] delayCST = new int[delayConstraint.length];
					for (int i = 0; i < delayConstraint.length; i++) {// ������ʱ
						delayCST[i] = (int) mindelay + delayConstraint[i];
					}

					// �������CORE,����ÿ���������ĸ��ֶε�ֵ���Ƚ�ӷ�ף����޸�......
					for (int i = 0; i < delayCST.length; i++) {
						row.createCell(baseNum * i + descrption.length).setCellValue(delayCST[i]);
						for (int j = 0; j < methodNames.length; j++) {
							double c, d;
							int time;
							long startTime = System.currentTimeMillis();
							if (methodNames[j].equals("LARAC")) {
								LARACMethod lm = new LARACMethod();
								List<Integer> pathlm = lm.OptimalPath(Node, Id, IdLink, delayCST[i], start, end);
								if (pathlm == null) {
									continue;
								}
								c = lm.Ctheta(pathlm, Id, IdLink);
								d = lm.Ptheta(pathlm, Id, IdLink);
								time = lm.getCallDijkstraTime();
							} else if (methodNames[j].equals("BiLAD")) {
								BiLAD lmwnsf = new BiLAD();
								List<Integer> pathlmwnsf = lmwnsf.OptimalPath(Node, Id, IdLink, delayCST[i], start,
										end);
								if (pathlmwnsf == null) {
									continue;
								}
								c = lmwnsf.Ctheta(pathlmwnsf, Id, IdLink);
								d = lmwnsf.Ptheta(pathlmwnsf, Id, IdLink);
								time = lmwnsf.getCallDijkstraTime();
							} else if (methodNames[j].equals("YEN")) {
								YenMethod ym = new YenMethod();
								List<Integer> pathym = ym.OptimalPath(Node, Id, IdLink, delayCST[i], start, end);
								if (pathym == null) {
									continue;
								}
								c = ym.Ctheta(pathym, Id, IdLink);
								d = ym.Ptheta(pathym, Id, IdLink);
								time = ym.getCallDijkstraTime();
							} else if (methodNames[j].equals("LARACMD")) {
								LARACMethodWithMD lmwd = new LARACMethodWithMD();
								List<Integer> pathlmwd = lmwd.OptimalPath(Node, Id, IdLink, delayCST[i], start, end);
								if (pathlmwd == null) {
									continue;
								}
								c = lmwd.Ctheta(pathlmwd, Id, IdLink);
								d = lmwd.Ptheta(pathlmwd, Id, IdLink);
								time = lmwd.getCallDijkstraTime();
							} else if (methodNames[j].equals("ExactBiLAD")) {
								SearchTriangle st = new SearchTriangle(new BiLAD());
								List<Integer> pathst = st.OptimalPath(Node, Id, IdLink, delayCST[i], start, end);
								if (pathst == null) {
									continue;
								}
								c = st.Ctheta(pathst, Id, IdLink);
								d = st.Ptheta(pathst, Id, IdLink);
								time = st.getCallDijkstraTime();
							} else {// ��������
								c = d = 0.0;
								time = 0;
							}
							int haste = (int) (System.currentTimeMillis() - startTime);
							row.createCell(baseNum * i + descrption.length + 4 * j + 1).setCellValue(c);
							row.createCell(baseNum * i + descrption.length + 4 * j + 2).setCellValue(d);
							row.createCell(baseNum * i + descrption.length + 4 * j + 3).setCellValue(time);// ���Ľ�
							row.createCell(baseNum * i + descrption.length + 4 * j + 4).setCellValue(haste);
						}
					}
					// end for operation
				}
			}
		}
		// д�뵽Excel�ļ���
		workbook.write(new FileOutputStream(new File(filename)));
		workbook.close();
		// ע�⣬������Ȼ�ر��ˡ�������ͼ�ν����У����ɵ�Excel�ļ����ڱ�ռ��״̬����Ҫ��ͼ�ν�����ʵ�λ��
		// ʹ���������ա�
	}

	/**
	 * 
	 * Function: 
	 * 		����ļ����Ӷ����ɸ�Ŀ¼��Ψһ���ļ�
	 * Details: 
	 * 		���Ŀ¼���ļ���β���ֵ����ֵ��Ȼ�����ֵ��1��Ϊ���ļ��Ľ�β��
	 * 		�÷�����Ϊ�����DesignExcel�ĸ������� 
	 * Remark: 2018��9��19�� ����2:00:46
	 */
	public void checkFilename() {
		int max = 0;
		File[] files = new File("resource/excel").listFiles();
		for (int i = 0; i < files.length; i++) {
			int indexAfter = files[i].getName().lastIndexOf(".");
			int value = Integer.parseInt(files[i].getName().substring(4, indexAfter));
			if (value > max)
				max = value;
		}
		filename = "resource/excel/�㷨�Ƚ�" + (max + 1) + ".xls";// ���û���ļ�����ô�ļ���Ϊ�㷨�Ƚ�1.xls
	}

	/**
	 * 
	 * Function: 
	 * 		�Զ��ػ��ķ���
	 * Details:
	 * 		 ͨ������̨����ʵ���Զ��ػ���
	 * 		�÷�����Ҫ���������Excel��ʹ����YEN�㷨��
	 * 		��ΪYEN�㷨�ܺ�ʱ�䡣 һ����˵100������2����Сʱ��
	 * 		������ʹ��YEN�㷨��ʱ���Զ��ػ���
	 * Remark: 2018��9��19�� ����2:02:01
	 */
	public static void shutdown() {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("cmd /c shutdown /s /t 60");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Function: 
	 * 		�������ɵ�Excel���ļ���
	 * Details: 
	 * 		�÷�����Ҫ����ͼ�ν�������ʾExcel���ɵ�λ�ã�����·����
	 * Remark:  2018��9��19�� ����2:05:01
	 */
	public String getFilename() {
		return filename;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MutilMethodsComparision tmc = new MutilMethodsComparision();// ����̨���ø÷���
		tmc.checkFilename();
		tmc.DesignExcel();
		System.out.println("Done");
	}
}
