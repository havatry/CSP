package trial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
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
import cspAlgorithms.BiLAD;
import fileInput.IdFile;
import randomTopology.XMLHelper;
import randomTopology.Constant;

/**
 * 
 * OverView:
 * 		 该类完成指定算法的比较，并将结果写入到Excel中
 * 		 该类提供生成Excel方法、返回Excel文件名方法
 */
public class MutilMethodsComparision {
	private String filename;// Excel名称
	private String[] descrption = { "节点数", "总边数", "平均度", "最小度", "最大度", "最小延时", "gama" };// Excel前面几列的描述字段
	private String[] testargs = { "c", "d", "调用dijkstra次数", "运算时间/ms" };// 每个方法要测试的项
	private String[] methodNames = { "LARAC", "BiLAD", "ExactBiLAD" };// 测试的方法
	private int[] delayConstraint;// 延时约束增加值，在当前最小延时下进行的增加值作为延时阈值
	private double[] gama;// gama参数
	private SwingWorker<String, String> ref;// 图形界面中的引用

	public MutilMethodsComparision() {// 默认main方法调用此构造函数
		// TODO Auto-generated constructor stub
		delayConstraint = new int[] { 1, 2, 3 };
		gama = new double[] { 0.05, 0.1 };
	}

	// 该构造函数是图形界面进行调用的，指定延时增加矩阵、测试方法矩阵、gama参数矩阵以及引用
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
	 *		核心方法。用来生成Excel文件。
	 * Details:
	 *		首先检查文件名，获取文件名结尾的最大数字，并在此数字上加一。类似Linux上的添加用户时候给的UID。
	 *		然后是POI进行操作。具体操作过程参照网上教程。
	 * Remark: 2018年9月19日 下午1:50:01
	 */
	public void DesignExcel() throws FileNotFoundException, IOException {
		checkFilename();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("MultiMethodsComparision");
		// 冻结窗口
		if (Boolean.parseBoolean(XMLHelper.getValue("//allInfo/test/isFreeze")))
			sheet.createFreezePane(0, 2);

		int baseRowNum = Constant.group * Constant.copy + 4;// 每个gama占用行数
		for (int k = 0; k < gama.length; k++) {// 遍历gama参数矩阵
			XMLHelper.setValue("//allInfo/test/gama", gama[k] + "");// 将gama值写入setting.xml文件中
																	// 该值会在写入后重新设置Constant的gama值
																	// 参考randomTopology包中XMLHelp类修改xml值的方法
			HSSFRow row = sheet.createRow(baseRowNum * k);
			int baseNum = 4 * methodNames.length + 1;// 这是delay基准
			// 进行单元格的合并，每个方法合并一次单元格
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
			// 开始添加描述字段
			row = sheet.createRow(baseRowNum * k + 1);
			for (int i = 0; i < descrption.length; i++) {// 添加描述
				row.createCell(i).setCellValue(descrption[i]);
			}
			// 开始添加方法字段
			for (int i = 0; i < delayConstraint.length; i++) {// 每个约束都添加方法
				row.createCell(baseNum * i + descrption.length).setCellValue("delayconstraint");
				sheet.setColumnWidth(baseNum * i + descrption.length, 13 * 256);
				for (int j = 0; j < methodNames.length; j++) {// 每个方法都添加测试参数
					for (int h = 0; h < testargs.length; h++) {// 添加测试参数
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
					// 通知MainRun类 ，触发task2的监听器
					if (ref != null)
						ref.firePropertyChange("progress", 0,
								(k * (Constant.group * Constant.copy) + t * Constant.copy + h + 1) * 100
										/ (Constant.group * Constant.copy * gama.length));
					// 构造网络的拓扑
					row = sheet.createRow(baseRowNum * k + t * Constant.copy + h + 2);
					int nodenum = Constant.step * (t + 1);// baseNumNodes
					int[] Node = new int[nodenum];
					for (int s = 0; s < nodenum; s++) {
						Node[s] = s;
					}
					Constant.TimeForTest = t * Constant.copy + h + 1;// 测试数据的index，通知修改TimeForTest
					double[][] Id = IdFile.GetId(false);
					int[][] IdLink = IdFile.GetIdLink(Id);
					// 预设是2和6
					// 预处理一下
					int start = Constant.start;
					int end = Constant.end;
					if (nodenum < 2) {
						// 异常
						JOptionPane.showMessageDialog(null, "Node number less than 2, program exit");
						System.exit(1);
					}
					if (start >= nodenum) {
						start = (int)(Math.random() * nodenum); // 随机取一个点
					}
					if (end >= nodenum) {
						do {
							end = (int)(Math.random() * nodenum);
						} while (start == end);
					}

					// 设置描述字段的具体值
					row.createCell(0).setCellValue(nodenum);// 设置节点数
					HSSFCell cell = row.createCell(1);
					cell.setCellValue(Id.length / 2);// 设置总边数
					HSSFCellStyle style = workbook.createCellStyle();
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					cell = row.createCell(2);
					cell.setCellValue(AbstractCSPMethods.getAverageDegree(Node, Id) / 2);
					cell.setCellStyle(style);// 设置平均度
					int[] MinAndMaxDegree = Common.GetMinAndMaxDegree(nodenum, Id);
					row.createCell(3).setCellValue(MinAndMaxDegree[0] / 2);// 设置最小度
					row.createCell(4).setCellValue(MinAndMaxDegree[1] / 2);// 设置最大度
					double mindelay = new BiLAD().GetMinDelay(Node, Id, IdLink, start, end);
					row.createCell(5).setCellValue(mindelay);// 设置最小延时
					row.createCell(6).setCellValue(gama[k]);// 设置gama
					int[] delayCST = new int[delayConstraint.length];
					for (int i = 0; i < delayConstraint.length; i++) {// 设置延时
						delayCST[i] = (int) mindelay + delayConstraint[i];
					}

					// 计算操作CORE,设置每个方法的四个字段的值，比较臃肿，待修改......
					for (int i = 0; i < delayCST.length; i++) {
						row.createCell(baseNum * i + descrption.length).setCellValue(delayCST[i]);
						for (int j = 0; j < methodNames.length; j++) {
							double c, d;
							int time;
							long startTime = System.currentTimeMillis();
							if (methodNames[j].equals("BiLAD")) {
								BiLAD lmwnsf = new BiLAD();
								List<Integer> pathlmwnsf = lmwnsf.OptimalPath(Node, Id, IdLink, delayCST[i], start,
										end);
								if (pathlmwnsf == null) {
									continue;
								}
								c = lmwnsf.Ctheta(pathlmwnsf, Id, IdLink);
								d = lmwnsf.Ptheta(pathlmwnsf, Id, IdLink);
								time = lmwnsf.getCallDijkstraTime();
							} else if (methodNames[j].equals("ExactBiLAD")) {
								SearchTriangle st = new SearchTriangle(new BiLAD());
								List<Integer> pathst = st.OptimalPath(Node, Id, IdLink, delayCST[i], start, end);
								if (pathst == null) {
									continue;
								}
								c = st.Ctheta(pathst, Id, IdLink);
								d = st.Ptheta(pathst, Id, IdLink);
								time = st.getCallDijkstraTime();
							} else {// 其他方法
								c = d = 0.0;
								time = 0;
							}
							int haste = (int) (System.currentTimeMillis() - startTime);
							row.createCell(baseNum * i + descrption.length + 4 * j + 1).setCellValue(c);
							row.createCell(baseNum * i + descrption.length + 4 * j + 2).setCellValue(d);
							row.createCell(baseNum * i + descrption.length + 4 * j + 3).setCellValue(time);// 待改进
							row.createCell(baseNum * i + descrption.length + 4 * j + 4).setCellValue(haste);
						}
					}
					// end for operation
				}
			}
		}
		sheet.setColumnHidden(3, true);
		sheet.setColumnHidden(4, true);
		sheet.setColumnHidden(5, true);
		sheet.setColumnHidden(6, true);
		// 写入到Excel文件中
		workbook.write(new FileOutputStream(new File(filename)));
		workbook.close();
		// 注意，这里虽然关闭了。但是在图形界面中，生成的Excel文件处于被占用状态。需要在图形界面的适当位置
		// 使用垃圾回收。
	}

	/**
	 * 
	 * Function: 
	 * 		检测文件，从而生成该目录下唯一的文件
	 * Details: 
	 * 		检测目录下文件结尾数字的最大值，然后将最大值加1作为新文件的结尾。
	 * 		该方法作为上面的DesignExcel的辅助方法 
	 * Remark: 2018年9月19日 下午2:00:46
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
		filename = "resource/excel/算法比较" + (max + 1) + ".xls";// 如果没有文件，那么文件名为算法比较1.xls
	}

	/**
	 * 
	 * Function: 
	 * 		自动关机的方法
	 * Details:
	 * 		 通过控制台，来实现自动关机。
	 * 		该方法主要配和在生成Excel中使用了YEN算法。
	 * 		因为YEN算法很耗时间。 一般来说100个用例2个多小时。
	 * 		建议在使用YEN算法的时候，自动关机。
	 * Remark: 2018年9月19日 下午2:02:01
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
	 * 		返回生成的Excel的文件名
	 * Details: 
	 * 		该方法主要是在图形界面中提示Excel生成的位置（绝对路径）
	 * Remark:  2018年9月19日 下午2:05:01
	 */
	public String getFilename() {
		return filename;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MutilMethodsComparision tmc = new MutilMethodsComparision();// 控制台调用该方法
		tmc.checkFilename();
		tmc.DesignExcel();
		System.out.println("Done");
	}
}
