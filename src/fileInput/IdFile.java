package fileInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类主要通过文件来获取ID矩阵。 提供获取链路矩阵、获取边权重矩阵、获取边连接矩阵方法。
 */
public class IdFile {
	/**
	 * 
	 * Function:
	 *		通过给定文件，获取文件终点链路信息。
	 *		文件格式为每行对应起点、终点、代价和延时
	 * Details:
	 *		通过获取文件行数，构造链路矩阵。
	 *		然后读取特定文件，写入链路矩阵
	 * Remark: 2018年9月19日 下午12:48:31
	 */
	public static double[][] GetId(boolean multi) {
		String fileName = Constant.specFile ? Constant.specIdFile : Constant.idFile.replace(".", "_" + Constant.TimeForTest + ".");
		double[][] id = null;
		try {
			int lineNum = Files.readAllLines(Paths.get(fileName)).size();
			int size = multi ? 5 : 4;
			id = new double[lineNum - 1][size];// 0-4分别表示起点，终点，代价，延时
			Scanner in = new Scanner(new File(fileName));
			in.nextLine();// 先除去第一行备注
			while (in.hasNextLine()) {
				String[] parts = in.nextLine().split("\t");
				int currentId = Integer.parseInt(parts[0]);
				id[currentId][0] = Integer.parseInt(parts[1]);// 起点
				id[currentId][1] = Integer.parseInt(parts[2]);// 终点
				id[currentId][2] = Integer.parseInt(parts[3]);// 代价
				id[currentId][3] = Integer.parseInt(parts[4]);// 延时
				if (multi) {
					id[currentId][4] = Integer.parseInt(parts[5]); // 丢包
				}
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 
	 * Function:
	 *		获取边权重矩阵
	 * Details:
	 *		在给定的链路矩阵时候，由于链路矩阵的第三列就是边上的权重
	 *		因此可以将两个节点对应链路上的第三列值便是这两点的权重
	 * Remark: 2018年9月19日 下午12:49:49
	 */
	public static double[][] GetEdge(double[][] Id) {
		int nodeNum = Constant.numNodes;
		int[][] idlink = GetIdLink(Id);
		double[][] edge = new double[nodeNum][nodeNum];
		for (int i = 0; i < nodeNum; i++)
			Arrays.fill(edge[i], Constant.MAX_VALUE);
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					edge[i][j] = 0;
				} else {
					edge[i][j] = Id[idlink[i][j]][2];//核心操作
				}
			}
		}
		return edge;
	}

	/**
	 * 
	 * Function:
	 *		获取边连接矩阵
	 * Details:
	 *		边连接矩阵是指通过两个点唯一定位到一个链路上。
	 *		如果两个点之间没有链路，那么边连接矩阵的值为-1，通常用来判断。
	 * Remark: 2018年9月19日 下午12:51:00
	 */
	public static int[][] GetIdLink(double[][] Id) {
		int nodeNum = Constant.numNodes;
		int[][] IdLink = new int[nodeNum][nodeNum];
		for (int i = 0; i < IdLink.length; i++)
			Arrays.fill(IdLink[i], -1);
		for (int i = 0; i < Id.length; i++)
			IdLink[(int) Id[i][0]][(int) Id[i][1]] = i;// Id号，核心操作
		return IdLink;
	}
}
