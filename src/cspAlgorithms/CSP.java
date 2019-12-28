package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		该接口为所有CSP算法提供规范。其中核心是要求所有CSP算法实现OptimalPath方法。
 * 		另外还有对于LARACMethodWithStoring的MutliOptimalPaths,
 * 		之所以将此方法定义为default并实现，实际上是免去了在抽象类中完成实现。因为很多时候该方法不会被调用，
 * 		而只在LARACMethodWithStoring会被重写。
 * 		然后是求路径上的代价、求路径上的延时、获取最佳的theta、获取调用Dijkstra次数的四个方法。
 */
public interface CSP {
	/**
	 * 
	 * Function:
	 * 		 该方法是每个算法的核心。完成给定的网络拓扑（一般有Node,Id,IDLink三个部分组成),
	 * 		以及给定的延时阈值约束的情况下,返回从start节点到end节点的最短路径
	 * Details:
	 * 		 具体实现在每个算法的详细注释中
	 * Remark: 2018年9月18日 下午10:11:54
	 */
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end);

	/**
	 * 
	 * Function: 
	 * 		该方法默认返回null,当且仅当为LARACMethodWithStoring的时候会重写该方法。 
	 * Details:
	 * 		具体实现在对应算法注释中
	 * Remark: 2018年9月18日 下午10:13:24
	 */
	default public List<List<Integer>> MutliOptimalPaths(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink,
			int delayConstraint, int start, int end[]) {
		return null;
	}

	/**
	 * 
	 * Function:
	 * 		 给定一个路径，求这个路径上的总代价
	 * Details: 
	 * 		依据路径上的每段链路，将这些链路上的代价累计起来，最后返回累计的代价
	 * Remark: 2018年9月18日 下午10:14:16
	 */
	public double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink);

	/**
	 * 
	 * Function: 
	 * 		给定一个路径，求这个路径上的总延时 
	 * Details: 
	 * 		依据路径上的每段链路，将这些链路上的延时累计起来，最后返回累计的延时
	 * Remark: 2018年9月18日 下午10:15:11
	 */
	public double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink);

	/**
	 * 
	 * Function: 
	 * 		返回算法计算完成后的最佳theta 
	 * Details:
	 * 		初始的时候theta为-1，当时算法计算结束后，如果theta还是为-1，说明算法运行出错。
	 * 		否则theta是表示算法运行最后收敛的斜率对应的角度。
	 * 		该theta一方面可以作为参考，另一方面还可以结合搜索算法(本包中的SearchTriangle类)来
	 * 		对三角形内的点进行搜索，从而找到最优解。ExactBiLAD就是对BiLAD进行的搜索。
	 * Remark: 2018年9月18日 下午10:16:12
	 */
	public double getTheta();

	/**
	 * 
	 * Function: 
	 * 		返回算法计算完成后的调用Dijkstra次数。 
	 * Details:
	 * 		一般而言,每调用一次dijkstra算法就会得到一个新的路径，而这些新路径是
	 * 		最后路径的中间过程。但也有些dijkstra算法的计算只是为了做些辅助工作。
	 * 		这主要体现在Yen算法中。这涉及到Yen算法的内部原来，具体参考维基百科Yen算法介绍。
	 * Remark: 2018年9月18日 下午10:18:18
	 */
	public int getCallDijkstraTime();
}
