package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		该类主要是完成Yen算法的求解。借助本包中Dijkstra中的辅助方法来实现。
 */
public class YenMethod extends AbstractCSPMethods {
	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		Dijkstra dijkstra = new Dijkstra();
		// 调用辅助方法
		List<Integer> path = dijkstra.YenBasedDelayConstraint(Node, Common.getEdge(Node, Id, IdLink, 0), Id, IdLink,
				start, end, delayConstraint);
		CallDijkstraTime = dijkstra.getCallDijkstraTime();// 返回调用次数
		return path;
	}
}
