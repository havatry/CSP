package cspAlgorithms;

import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		�����ڸ���һ��CSP�����󣬼�����CSP�������������������ڵľ�ȷ�⡣
 */
public class SearchTriangle extends AbstractCSPMethods {
	private CSP CspMethod; // �������CSP����

	public SearchTriangle(CSP CspMethod) {
		// TODO Auto-generated constructor stub
		this.CspMethod = CspMethod;
	}

	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// �����CSP������ȡ���·����������Ѳ���ͬ�ھ�ȷ��ע�����֣�
		List<Integer> path = CspMethod.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
		if (path == null)
			return null;// �Ҳ���·�����򷵻�
		double theta = CspMethod.getTheta();// ��ȡCSP�����ѽ��ʱ���Ӧ��б�ʽǶ�
		// ������Ƕ�����baseCost,����baseCost��ο������е�Dijkstra����YenWithSearch������Ӧ����
		double baseCost = Math.cos(theta) * Ctheta(path, Id, IdLink) + Math.sin(theta) * delayConstraint;
		Dijkstra dijkstra = new Dijkstra();
		// ����YenWithSearch������������������������·��
		List<List<Integer>> allPaths = dijkstra.YenWithSearch(Node, Common.getEdge(Node, Id, IdLink, theta), Id, IdLink,
				start, end, baseCost, theta, delayConstraint);
		// �ҳ��������·���д�����С���Ǹ�����Ϊ��ȷ��
		List<Integer> retPath = null;
		int MIN = Constant.MIN;
		for (int i = 0; i < allPaths.size(); i++) {
			if (Ptheta(allPaths.get(i), Id, IdLink) <= delayConstraint) {
				if (Ctheta(allPaths.get(i), Id, IdLink) < MIN) {
					MIN = (int) Ctheta(allPaths.get(i), Id, IdLink);
					retPath = allPaths.get(i);
				}
			}
		}
		// �ӱ���Dijkstra���л�ȡ����dijkstra�Ĵ���
		CallDijkstraTime = dijkstra.getCallDijkstraTime() + CspMethod.getCallDijkstraTime();
		return retPath;
	}
}
