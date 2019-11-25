package cspAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		������ʵ�൱��һ�����ࡣ���ǿ��Խ������еķ����ϲ���AbstractCSPMethods���С�ֻ�ǽ���Dijkstra���ԭ��
 * 		��ΪDijkstra���кܶ෽����������ֱ�ӵ���AbstractCSPMethods���еķ�������ΪDijkstra�಻�Ǽ̳��˺��ߡ�
 * 		��ˣ������¼���һ�������࣬�󲿷��Ǹ�Dijkstra�������á�Ҳ����������ΪUtils���Ӻ��ʡ� �����ṩ��
 */
public class Common {
	/**
	 * 
	 * Function: 
	 * 		�ڸ���theta�󣬽������ۺ���������ο�jutter���ģ�����֮ǰ����),��ԭ����ıߴ���ת��Ϊ������ıߴ���
	 * Details:
	 * 		����ת�����������������һ�µġ�������cos��sin����������lambda���á�
	 * 		ʵ���Ͻ�����䣬ֻ�����Խ����������cos(theta)����
	 * Remark: 2018��9��18�� ����9:58:18
	 */
	protected static double[][] getEdge(int[] Node, double[][] Id, int[][] IdLink, double theta) {
		int nodeNum = Node.length;
		double[][] Edge = new double[nodeNum][nodeNum];
		for (int i = 0; i < Id.length; i++) {
			Edge[(int) Id[i][0]][(int) Id[i][1]] = Math.cos(theta) * Id[i][2] + Math.sin(theta) * Id[i][3];// ���ۺ���
		}
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if ((Edge[i][j] == 0) && (i != j)) {
					Edge[i][j] = Constant.MAX_VALUE;// �������ڵı���һ���ܴ��ֵ���棬��ֵ��setting.xml������
				}
			}
		}
		return Edge;
	}

	/**
	 * 
	 * Function: 
	 * 		�÷����ǽ��ߵ���Ϣ�������������µ��ڴ��С�
	 * Details:
	 * 		�����ڲ����У����ʹ�����ã���ı������ֵ��Ϊ�˱�֤�ڲ��������У���֤ԭ�ȸ�����ֵ���ᷢ���ı䣬
	 * 		��һЩ�ط����и�������ͻ�ԭ��������֤��������Ӱ�����ݡ�
	 * 		���ﱾ��Ӧ��ʹ��Object���clone���������ڲ��Ǻ���Ϥ�������Լ�ʵ���˱��渱���ķ�����
	 * Remark: 2018��9��18�� ����10:02:29
	 */
	protected static double[][] deepCloneEdge(double[][] Edge) {// ����Edge�ĸ���
		double[][] subEdge = new double[Edge.length][Edge.length];
		for (int i = 0; i < Edge.length; i++) {
			for (int j = 0; j < Edge.length; j++) {
				subEdge[i][j] = Edge[i][j];// ����
			}
		}
		return subEdge;
	}

	/**
	 * 
	 * Function: 
	 * 		�÷����ǽ�·����Ϣ�������������µ��ڴ���
	 * Details: 
	 * 		����ʵ�֣�������deepCloneEdge��������
	 * Remark: 2018��9��18�� ����10:05:08
	 */
	protected static List<Integer> deepCloneList(List<Integer> path) {// ����·������
		List<Integer> subPath = new ArrayList<>();
		for (int i = 0; i < path.size(); i++) {
			subPath.add(path.get(i));// ����
		}
		return subPath;
	}

	/**
	 * 
	 * Function: 
	 * 		�÷���������·��Ϣ����ȡ���������е����Ⱥ���С�� 
	 * Details:
	 * 		����������·���������յ���ֵĴ�����������Ϊÿ����·�������յ�ֱ���Ϊ���Ⱥ���ȡ�
	 * 		������ͳ�������нڵ�Ķȡ���Ȼ����ĶȺ�AbstractCSPMethods�н��ܵĶȸ������ơ�
	 * 		Ȼ������еĶȽ�������Ȼ�󷵻�������С��������
	 * Remark: 2018��9��18�� ����10:05:56
	 */
	public static int[] GetMinAndMaxDegree(int nodenum, double[][] Id) {
		int[] frequence = new int[nodenum];
		for (int i = 0; i < Id.length; i++) {
			frequence[(int) Id[i][0]]++;
			frequence[(int) Id[i][1]]++;
		}
		Arrays.sort(frequence);// ����
		return new int[] { frequence[0], frequence[frequence.length - 1] };
	}

	/**
	 * 
	 * Function:
	 * 		 �����·���ϵĴ����ܺ� 
	 * Details: 
	 * 		����CSP�ӿڸ÷�����������ͬ����Ҫ��Ϊ�����е�Dijkstra��ʹ�á�
	 * Remark: 2018��9��19�� ����11:10:23
	 */
	public static double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink) {// �������·���ϵĴ���
		// TODO Auto-generated method stub
		double total = 0;
		for (int i = path.size() - 1; i >= 1; i--) {
			int startOfPath = path.get(i);
			int nextOfPath = path.get(i - 1);
			int id = IdLink[startOfPath][nextOfPath];
			total += Id[id][2];
		}
		return total;
	}

	/**
	 * 
	 * Function: 
	 * 		�����·���ϵ���ʱ�ܺ�
	 * Details: 
	 * 		����CSP�ӿڸ÷�����������ͬ����Ҫ��Ϊ�����е�Dijkstra��ʹ�á�
	 * Remark: 2018��9��19�� ����11:11:32
	 */
	public static double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink) {// �������·���ϵ���ʱ
		// TODO Auto-generated method stub
		double delay = 0;
		for (int i = path.size() - 1; i >= 1; i--) {
			int startOfPath = path.get(i);
			int nextOfPath = path.get(i - 1);
			int id = IdLink[startOfPath][nextOfPath];
			delay += Id[id][3];
		}
		return delay;
	}
}
