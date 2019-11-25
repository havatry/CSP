package fileInput;

import randomTopology.Point;
import randomTopology.RandomPoint;

/**
 * 
 * OverView:
 *		获取节点矩阵
 */
public class NodeFile {
	/**
	 * 
	 * Function:
	 *		获取给定文件的节点矩阵
	 * Details:
	 *		使用randomTopology包中的RandomPoint类来获取
	 *		指定文件的节点矩阵
	 * Remark: 2018年9月19日 下午12:54:46
	 */
	public static Point[] GetNodes(int time) {
		return new RandomPoint().getPoints(time);
	}
}
