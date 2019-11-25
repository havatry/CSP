package fileInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		�����Ǵ��ļ��л�ȡ���Ľڵ�ı��
 */
public class CoreNodeFile {
	/**
	 * 
	 * Function:
	 *		��ȡ���Ľڵ�
	 * Details:
	 *		ͨ����ȡ�ļ��������Ľڵ�ı�ż��뵽�б��У�������
	 * Remark: 2018��9��19�� ����12:52:55
	 */
	public static List<Integer> GetCoreNodeIndex() {
		List<Integer> coreIndex = new ArrayList<Integer>();
		// ��ȡ�����ļ�
		try {
			Scanner in = new Scanner(new File(Constant.coreNodeFile.replace(".", "_" + Constant.TimeForTest + ".")));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				coreIndex.add(Integer.parseInt(line.substring(6)));// ÿ�е�6���ַ���ʼ��ȡ���
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coreIndex;
	}
}
