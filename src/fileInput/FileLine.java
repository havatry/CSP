package fileInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * OverView:
 *		����������ȡһ�������ļ����ж�����
 */
public class FileLine {
	/**
	 * 
	 * Function:
	 *		��ȡ�ļ�������
	 * Details:
	 *		ʹ���ۼƱ�������ÿ�α������н��м���
	 * Remark: 2018��9��19�� ����12:53:58
	 */
	public static int GetLineNumber(String filename) {
		int count=0;
		try {
			Scanner in=new Scanner(new File(filename));
			while(in.hasNextLine()){
				in.nextLine();
				count++;//�����ۻ�
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public static int parseNodeNumFormIdFile(String idFileName) {
		int max = 0;
		try {
			Scanner in = new Scanner(new File(idFileName));
			in.nextLine();
			while (in.hasNextLine()) {
				String[] part = in.nextLine().split("\t");
				max = Math.max(max, Math.max(Integer.parseInt(part[1]), 
						Integer.parseInt(part[2])));
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return max;
	}
}
