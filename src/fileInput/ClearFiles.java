package fileInput;

import java.io.IOException;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		��������ļ�
 */
public class ClearFiles {
	/**
	 * 
	 * Function:
	 *		ɾ�����ɵ��ļ�
	 * Details:
	 *		ʹ��������ɾ��
	 * Remark: 2018��9��19�� ����12:53:28
	 */
	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();// ���ÿ���̨����
		try {
			rt.exec("cmd /c rm " + Constant.basePath + "/*");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
