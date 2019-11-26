package fileInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * OverView:
 *		该类用来获取一个给定文件中有多少行
 */
public class FileLine {
	/**
	 * 
	 * Function:
	 *		获取文件的行数
	 * Details:
	 *		使用累计变量，对每次遍历的行进行计数
	 * Remark: 2018年9月19日 下午12:53:58
	 */
	public static int GetLineNumber(String filename) {
		int count=0;
		try {
			Scanner in=new Scanner(new File(filename));
			while(in.hasNextLine()){
				in.nextLine();
				count++;//行数累积
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
