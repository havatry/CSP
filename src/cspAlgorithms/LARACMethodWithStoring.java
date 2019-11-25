package cspAlgorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		������Ҫ����������㵽���յ�����·����������㵽��һ���յ��ʱ�򣬼�¼�м����� 
 * 		Ȼ�����м����������յ�����·����
 *		 �ṩһ�������ڲ��ࡣ
 */
public class LARACMethodWithStoring extends LARACMethod {
	private ArrayNode[] nodeInfo;// ��������

	@Override
	public List<List<Integer>> MutliOptimalPaths(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink,
			int delayConstraint, int start, int[] end) {
		// TODO Auto-generated method stub
		List<List<Integer>> allPaths = new ArrayList<>();
		allPaths.add(OptimalPath(Node, Id, IdLink, delayConstraint, start, end[0]));
		for (int i = 1; i < end.length; i++)
			allPaths.add(ActualPath(Node, Id, IdLink, nodeInfo[end[i]].theta1, nodeInfo[end[i]].theta2, delayConstraint,
					start, end[i]));
		return allPaths;
	}

	/**
	 * 
	 * OverView: 
	 * 		�����࣬��Ҫ��ʼ��nodeInfo���顣�ṩ�������ԣ��ֱ�����ʱ��theta1��theta2������ο��㷨�ܽ�.word
	 */
	private class ArrayNode {
		protected double delay = -1;
		protected double theta1 = -1.0;
		protected double theta2 = Math.PI;
	}

	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		nodeInfo = new ArrayNode[Node.length];
		List<Integer> path = super.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
		for (int i = 0; i < super.thetaList.size(); i++) {// ��ÿ��theta���п��Ǹ���״̬��
			changeInfo(Node, Id, IdLink, super.thetaList.get(i), delayConstraint, start);
		}
		return path;
	}

	/**
	 * 
	 * Function: 
	 * 		���ݸ�����thate���������Ӧ״̬����ÿ���ڵ��theta1��theta2
	 * Details:
	 * 		��ȡnodeInfo���飬����ʱ������theta1��theta2�����ҽ����״̬������һ��״̬���Ͻ��п��� �򻯹��� 
	 * Remark: 2018��9��19�� ����11:59:09
	 */
	private void changeInfo(int[] Node, double[][] Id, int[][] IdLink, double theta, double D, int start) {
		int[] prePath = getPrePath(Node, Id, theta, start);
		List<List<Integer>> allPaths = new ArrayList<>();
		Set<Integer> hasAdd = new HashSet<>();
		for (int i = 0; i < nodeInfo.length; i++) {// ���Խ�ʡ·��
			if (hasAdd.contains(i))
				continue;
			List<Integer> path = Dijkstra.SinglePath(prePath, start, i);// ���㵽����ڵ�����·��
			hasAdd.addAll(path);
			allPaths.add(path);
		}
		nodeInfo[start].delay = 0.0;// init
		// ��Ҫ�ǻ�ȡÿ���ڵ��theta1��theta2��������theta֮�������Ӧ�ڵ��·��ʱ������Ĭ�ϵ�thetaTop��thetaBelow
		for (int i = 0; i < allPaths.size(); i++) {
			// ����֮���Դ�allPaths.get(i).size()-2��ʼ����ΪallPaths.get(i).size()-1������nodeInfo��delayֵΪ0
			for (int j = allPaths.get(i).size() - 2; j >= 0; j--) {
				int currentIndex = allPaths.get(i).get(j);
				nodeInfo[currentIndex].delay += nodeInfo[allPaths.get(i).get(j - 1)].delay
						+ Id[IdLink[allPaths.get(i).get(j - 1)][currentIndex]][3];
				if (nodeInfo[currentIndex].delay >= D) {
					if (theta > nodeInfo[currentIndex].theta1) {
						nodeInfo[currentIndex].theta1 = theta;
					}
				} else {
					if (theta < nodeInfo[currentIndex].theta2) {
						nodeInfo[currentIndex].theta2 = theta;
					}
				}
			}
		}
		for (int i = 0; i < nodeInfo.length; i++)
			nodeInfo[i].delay = 0;// reset
	}

	/**
	 * 
	 * Function: 
	 * 		�÷�������theta����ʼ�ڵ�����ǰ��·��
	 * Details:
	 * 		������theta����������ı�Ȩ�ء�Ȼ����ñ�����Dijkstra���PrePath����������ǰ��·����
	 * 		�÷�����Ҫ��Ϊ�����changeInfo�����ĸ���������
	 * Remark: 2018��9��19�� ����12:03:33
	 */
	protected static int[] getPrePath(int[] Node, double[][] Id, double theta, int start) {// ��ȡǰ��·��
		// �����µ�����ߵ�Ȩ��
		double[][] Edge = new double[Node.length][Node.length];
		for (int i = 0; i < Id.length; i++) {
			Edge[(int) Id[i][0]][(int) Id[i][1]] = Math.cos(theta) * Id[i][2] + Math.sin(theta) * Id[i][3];
		}
		for (int i = 0; i < Node.length; i++) {
			for (int j = 0; j < Node.length; j++) {
				if ((Edge[i][j] == 0) && (i != j)) {
					Edge[i][j] = Constant.MAX_VALUE;
				}
			}
		}
		// ����dijkstra�㷨����
		return new Dijkstra().PrePath(Node, Edge, start);
	}
}
