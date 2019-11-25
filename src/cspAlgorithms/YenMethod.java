package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		������Ҫ�����Yen�㷨����⡣����������Dijkstra�еĸ���������ʵ�֡�
 */
public class YenMethod extends AbstractCSPMethods {
	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		Dijkstra dijkstra = new Dijkstra();
		// ���ø�������
		List<Integer> path = dijkstra.YenBasedDelayConstraint(Node, Common.getEdge(Node, Id, IdLink, 0), Id, IdLink,
				start, end, delayConstraint);
		CallDijkstraTime = dijkstra.getCallDijkstraTime();// ���ص��ô���
		return path;
	}
}
