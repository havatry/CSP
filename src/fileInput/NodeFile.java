package fileInput;

import randomTopology.Point;
import randomTopology.RandomPoint;

/**
 * 
 * OverView:
 *		��ȡ�ڵ����
 */
public class NodeFile {
	/**
	 * 
	 * Function:
	 *		��ȡ�����ļ��Ľڵ����
	 * Details:
	 *		ʹ��randomTopology���е�RandomPoint������ȡ
	 *		ָ���ļ��Ľڵ����
	 * Remark: 2018��9��19�� ����12:54:46
	 */
	public static Point[] GetNodes(int time) {
		return new RandomPoint().getPoints(time);
	}
}
