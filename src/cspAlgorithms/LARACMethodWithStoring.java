package cspAlgorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类主要是用来求单起点到多终点的最佳路径。在求起点到第一个终点的时候，记录中间结果。 
 * 		然后用中间结果求到其余终点的最佳路径。
 *		 提供一个辅助内部类。
 */
public class LARACMethodWithStoring extends LARACMethod {
	private ArrayNode[] nodeInfo;// 核心数组

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
	 * 		辅助类，主要初始化nodeInfo数组。提供三个属性，分别是延时和theta1和theta2。具体参考算法总结.word
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
		for (int i = 0; i < super.thetaList.size(); i++) {// 对每个theta进行考虑更新状态树
			changeInfo(Node, Id, IdLink, super.thetaList.get(i), delayConstraint, start);
		}
		return path;
	}

	/**
	 * 
	 * Function: 
	 * 		依据给定的thate来更新其对应状态树中每个节点的theta1和theta2
	 * Details:
	 * 		采取nodeInfo数组，用延时来更新theta1和theta2。并且将多个状态树放在一个状态树上进行考虑 简化过程 
	 * Remark: 2018年9月19日 上午11:59:09
	 */
	private void changeInfo(int[] Node, double[][] Id, int[][] IdLink, double theta, double D, int start) {
		int[] prePath = getPrePath(Node, Id, theta, start);
		List<List<Integer>> allPaths = new ArrayList<>();
		Set<Integer> hasAdd = new HashSet<>();
		for (int i = 0; i < nodeInfo.length; i++) {// 可以节省路径
			if (hasAdd.contains(i))
				continue;
			List<Integer> path = Dijkstra.SinglePath(prePath, start, i);// 计算到其余节点的最短路径
			hasAdd.addAll(path);
			allPaths.add(path);
		}
		nodeInfo[start].delay = 0.0;// init
		// 主要是获取每个节点的theta1和theta2，这两个theta之后在求对应节点的路径时候会代替默认的thetaTop和thetaBelow
		for (int i = 0; i < allPaths.size(); i++) {
			// 这里之所以从allPaths.get(i).size()-2开始，因为allPaths.get(i).size()-1处本身nodeInfo的delay值为0
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
	 * 		该方法给定theta和起始节点来找前驱路径
	 * Details:
	 * 		首先用theta构造新网络的边权重。然后调用本包中Dijkstra类的PrePath方法来返回前驱路径。
	 * 		该方法主要作为上面的changeInfo方法的辅助方法。
	 * Remark: 2018年9月19日 下午12:03:33
	 */
	protected static int[] getPrePath(int[] Node, double[][] Id, double theta, int start) {// 获取前驱路径
		// 构造新的网络边的权重
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
		// 调用dijkstra算法返回
		return new Dijkstra().PrePath(Node, Edge, start);
	}
}
