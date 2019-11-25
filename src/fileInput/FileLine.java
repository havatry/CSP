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
}
