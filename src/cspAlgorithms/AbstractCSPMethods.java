package cspAlgorithms;

import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		这是所有CSP算法的基类。该基类实现了CSP接口，并且提供了一些其他基本方法。这些方法包括
 * 		给定theta时获取新网络的路径、获取最小度、获取平均度。
 */
public abstract class AbstractCSPMethods implements CSP {
	protected double esp = Constant.esp;// 二分法和LARAC的精度
	protected double theta = -1.0;// 负数表示没有找到路径
	protected int CallDijkstraTime = 0;// 返回调用Dijkstra算法的次数

	@Override
	public double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink) {// 用于求解路径上的代价
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

	@Override
	public double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink) {// 用于求解路径上的延时
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

	/**
	 * 
	 * Function:
	 * 		在给定网络拓扑情况下，通过theta重新构造边的权重。
	 * 		其中权重定义为c=cos(theta)*c+sin(theta)*d,c是原来
	 * 		网络中的权重，d是原来网络中的延时。在用新的代价的网络中，从start节点到end节点求出一个路径。
	 * Details:
	 * 		求路径的方法是dijkstra算法。该函数每次被调用的时候，dijkstra算法也被调用。
	 * 		因此总的调用dijkstra次数 就相应自增。
	 * Remark: 2018年9月18日 下午9:14:35
	 */
	protected List<Integer> getPath(int[] Node, double[][] Id, double theta, int start, int end) {
		// 构造新网络的权重
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
		CallDijkstraTime++;
		return new Dijkstra().DijkstraOfPath(Node, Edge, start, end);
	}

	/**
	 * 
	 * Function: 
	 * 		这实际上在求当theta为PI/2的时候最短路径，该路径对应的延时即为最小延时。
	 * Details:
	 * 		当theta为PI/2的时候，新的网络权重就变成原来网络的延时。
	 * 		此时最短路径就是对应原来网络的最小延时。
	 * Remark: 2018年9月18日 下午9:27:11
	 */
	public double GetMinDelay(int[] Node, double[][] Id, int[][] IdLink, int start, int end) {
		return Ptheta(getPath(Node, Id, Math.PI / 2, start, end), Id, IdLink); // d-minial
	}

	@Override
	public double getTheta() {
		return theta;
	}

	@Override
	public int getCallDijkstraTime() {
		return CallDijkstraTime;
	}

	/**
	 * 
	 * Function:
	 * 		 获取网络的平均度。 
	 * Detail:
	 * 		平均度的求法是网络中的所有边数除以总的节点数。这里的度实际上是包括出度和入度。
	 * 		简单来说就是一条边对应的度是2。这在生成Excel中会除以2。
	 * 		Id的具体含义请参考randomTopology包中Topology类介绍。
	 * Remark: 2018年9月18日  下午9:29:32
	 */
	public static double getAverageDegree(int[] Node, double[][] Id) {
		return (double) Id.length / Node.length;
	}
}
