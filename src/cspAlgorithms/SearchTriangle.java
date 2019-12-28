package cspAlgorithms;

import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类在给定一个CSP方法后，继续在CSP方法后求三角形区域内的精确解。
 */
public class SearchTriangle extends AbstractCSPMethods {
	private CSP CspMethod; // 待传入的CSP方法

	public SearchTriangle(CSP CspMethod) {
		// TODO Auto-generated constructor stub
		this.CspMethod = CspMethod;
	}

	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// 传入的CSP方法获取最佳路径（这里最佳不等同于精确，注意区分）
		List<Integer> path = CspMethod.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
		if (path == null)
			return null;// 找不到路径，则返回
		double theta = CspMethod.getTheta();// 获取CSP求得最佳解的时候对应的斜率角度
		// 用这个角度来求baseCost,关于baseCost请参考本包中的Dijkstra类中YenWithSearch方法对应描述
		double baseCost = Math.cos(theta) * Ctheta(path, Id, IdLink) + Math.sin(theta) * delayConstraint;
		Dijkstra dijkstra = new Dijkstra();
		// 利用YenWithSearch方法求出所有满足条件的最短路径
		List<List<Integer>> allPaths = dijkstra.YenWithSearch(Node, Common.getEdge(Node, Id, IdLink, theta), Id, IdLink,
				start, end, baseCost, theta, delayConstraint);
		// 找出所有最短路径中代价最小的那个，即为精确解
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
		// 从本包Dijkstra类中获取调用dijkstra的次数
		CallDijkstraTime = dijkstra.getCallDijkstraTime() + CspMethod.getCallDijkstraTime();
		return retPath;
	}
}
