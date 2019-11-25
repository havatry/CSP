package randomTopology;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 
 * OverView: �������˵����
 */
public class GenerateRandomNetwork_MainProgram {
	/**
	 * 
	 * Function:
	 *		���ɸ�����������
	 * Details:
	 *		���������ļ��е�����group��ÿ��ĸ���copy
	 *		�������ɷ���Ҫ������ˣ����������е�Topology�ࡣ
	 * Remark: 2018��9��19�� ����1:00:22
	 */
	public static void main(String[] args) {
		for (int i = 0; i < Constant.group; i++) {
			XMLHelper.setValue("//allInfo/node/number", Constant.step * (i + 1) + "");// �����и��±���
			for (int j = 0; j < Constant.copy; j++) {
				new Topology().ProduceTopology();
				Constant.WriteFile_TimeFor++;// ���£���Ҫ
			}
		}
		writeCharacteristicToFile();//������д��
	}
	
	/**
	 * 
	 * Function:
	 *		��������Ĺؼ���������д�뵽�ļ���
	 * Details:
	 *		ͬ��
	 * Remark: 2018��9��19�� ����2:22:46
	 */
	public static void writeCharacteristicToFile() {
		//д����Ľڵ���ʡ�������������߸��ʡ��������Ե���߸��ʡ��߳���С��group��copy��step��С��д������
		PrintWriter out=null;
		try {
			out=new PrintWriter(Constant.CharacteristicFile);//һ��Ŀ¼��ֻ��һ�������ļ�
			out.println("���Ľڵ����: "+Constant.coreProbility);
			out.println("������������߸���: "+Constant.coreToCoreProbility);
			out.println("�������Ե���߸���: "+Constant.coreToNormalProbility);
			out.println("����߳�: "+Constant.W);
			out.println("��������: "+Constant.group);
			out.println("ÿ�����: "+Constant.copy);
			out.println("ÿ�����ڵ���: "+Constant.step);
			out.println("д������: "+new Date());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}finally {
			out.close();
		}
	}
}
