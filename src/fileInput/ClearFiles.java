package fileInput;

import java.io.IOException;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		清除生成文件
 */
public class ClearFiles {
	/**
	 * 
	 * Function:
	 *		删除生成的文件
	 * Details:
	 *		使用命令行删除
	 * Remark: 2018年9月19日 下午12:53:28
	 */
	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();// 调用控制台方法
		try {
			rt.exec("cmd /c rm " + Constant.basePath + "/*");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
