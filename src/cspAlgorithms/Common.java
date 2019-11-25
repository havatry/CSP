package cspAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类其实相当于一个基类。本是可以将该类中的方法合并到AbstractCSPMethods类中。只是介于Dijkstra类的原因。
 * 		因为Dijkstra类中很多方法，并不能直接调用AbstractCSPMethods类中的方法，因为Dijkstra类不是继承了后者。
 * 		因此，这里新加了一个公共类，大部分是给Dijkstra类来调用。也许将该类命名为Utils更加合适。 该类提供了
 */
public class Common {
	/**
	 * 
	 * Function: 
	 * 		在给定theta后，借助代价函数（具体参考jutter论文，或者之前描述),将原网络的边代价转变为新网络的边代价
	 * Details:
	 * 		这里转变和论文描述并不是一致的。这里用cos和sin函数来代替lambda作用。
	 * 		实际上结果不变，只不过对结果进行缩放cos(theta)倍。
	 * Remark: 2018年9月18日 下午9:58:18
	 */
	protected static double[][] getEdge(int[] Node, double[][] Id, int[][] IdLink, double theta) {
		int nodeNum = Node.length;
		double[][] Edge = new double[nodeNum][nodeNum];
		for (int i = 0; i < Id.length; i++) {
			Edge[(int) Id[i][0]][(int) Id[i][1]] = Math.cos(theta) * Id[i][2] + Math.sin(theta) * Id[i][3];// 代价函数
		}
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if ((Edge[i][j] == 0) && (i != j)) {
					Edge[i][j] = Constant.MAX_VALUE;// 将不存在的边以一个很大的值代替，该值在setting.xml中设置
				}
			}
		}
		return Edge;
	}

	/**
	 * 
	 * Function: 
	 * 		该方法是将边的信息拷贝出来放在新的内存中。
	 * Details:
	 * 		由于在操作中，如果使用引用，会改变最初的值。为了保证在操作过程中，保证原先给定的值不会发生改变，
	 * 		在一些地方进行副本保存和还原，进而保证操作不会影响数据。
	 * 		这里本来应该使用Object类的clone方法，介于不是很熟悉，还是自己实现了保存副本的方法。
	 * Remark: 2018年9月18日 下午10:02:29
	 */
	protected static double[][] deepCloneEdge(double[][] Edge) {// 保存Edge的副本
		double[][] subEdge = new double[Edge.length][Edge.length];
		for (int i = 0; i < Edge.length; i++) {
			for (int j = 0; j < Edge.length; j++) {
				subEdge[i][j] = Edge[i][j];// 保存
			}
		}
		return subEdge;
	}

	/**
	 * 
	 * Function: 
	 * 		该方法是将路径信息拷贝出来放在新的内存中
	 * Details: 
	 * 		具体实现，和上面deepCloneEdge方法类似
	 * Remark: 2018年9月18日 下午10:05:08
	 */
	protected static List<Integer> deepCloneList(List<Integer> path) {// 保存路径副本
		List<Integer> subPath = new ArrayList<>();
		for (int i = 0; i < path.size(); i++) {
			subPath.add(path.get(i));// 保存
		}
		return subPath;
	}

	/**
	 * 
	 * Function: 
	 * 		该方法依据链路信息来获取网络拓扑中的最大度和最小度 
	 * Details:
	 * 		遍历所有链路，将起点和终点出现的次数自增。因为每个链路的起点和终点分别作为出度和入度。
	 * 		这样就统计完所有节点的度。当然这里的度和AbstractCSPMethods中介绍的度概念类似。
	 * 		然后对所有的度进行排序，然后返回最大和最小的两个。
	 * Remark: 2018年9月18日 下午10:05:56
	 */
	public static int[] GetMinAndMaxDegree(int nodenum, double[][] Id) {
		int[] frequence = new int[nodenum];
		for (int i = 0; i < Id.length; i++) {
			frequence[(int) Id[i][0]]++;
			frequence[(int) Id[i][1]]++;
		}
		Arrays.sort(frequence);// 排序
		return new int[] { frequence[0], frequence[frequence.length - 1] };
	}

	/**
	 * 
	 * Function:
	 * 		 求给定路径上的代价总和 
	 * Details: 
	 * 		这是CSP接口该方法中描述相同。主要是为本包中的Dijkstra类使用。
	 * Remark: 2018年9月19日 上午11:10:23
	 */
	public static double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink) {// 用于求解路径上的代价
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
	 * 		求给定路径上的延时总和
	 * Details: 
	 * 		这是CSP接口该方法中描述相同。主要是为本包中的Dijkstra类使用。
	 * Remark: 2018年9月19日 上午11:11:32
	 */
	public static double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink) {// 用于求解路径上的延时
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
