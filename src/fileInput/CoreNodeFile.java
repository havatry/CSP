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
 * 		该类是从文件中获取核心节点的编号
 */
public class CoreNodeFile {
	/**
	 * 
	 * Function:
	 *		获取核心节点
	 * Details:
	 *		通过读取文件，将核心节点的编号加入到列表中，并返回
	 * Remark: 2018年9月19日 下午12:52:55
	 */
	public static List<Integer> GetCoreNodeIndex() {
		List<Integer> coreIndex = new ArrayList<Integer>();
		// 读取生成文件
		try {
			Scanner in = new Scanner(new File(Constant.coreNodeFile.replace(".", "_" + Constant.TimeForTest + ".")));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				coreIndex.add(Integer.parseInt(line.substring(6)));// 每行第6个字符开始获取编号
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coreIndex;
	}
}
