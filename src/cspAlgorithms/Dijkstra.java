package cspAlgorithms;

import java.util.ArrayList;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类是Yen算法、搜索算法、其他CSP算法的基础。主要依据dijkstra算法求最短路径、
 * 		找出第一个满足延时阈值的路径、搜索三角形里面满足延时阈值的最佳路径、返回调用dijkstra次数
 */
public class Dijkstra {
	private int CallDijkstraTime = 0;// 初始化调用Dijkstra次数

	/**
	 * 
	 * Function: 
	 * 		这是自己写的一个Dijkstra算法。 
	 * Details: 
	 * 		具体实现参考数据结构书籍 
	 * Remark: 2018年9月18日 下午10:21:56
	 */
	protected List<Integer> DijkstraOfPath(int[] Node, double[][] Edge, int start, int end) {
		// 初始化
		final int NumberOfNode = Node.length;
		int next = -1;// 记录选择的下一个结点
		boolean[] S = new boolean[NumberOfNode];// S集合
		int[] pre = new int[NumberOfNode];// 记录前驱节点
		double[] Distance = new double[NumberOfNode];// 构造最短距离矩阵**
		for (int i = 0; i < pre.length; i++) {
			pre[i] = -1;
		}
		for (int i = 0; i < NumberOfNode; i++) {
			Distance[i] = Edge[start][i];
			pre[i] = start;// 在此记录前驱
		}
		S[start] = true;
		pre[start] = -1;

		while (!isFullOfTrue(S)) {// 当S集不满
			double Min = Integer.MAX_VALUE;// 用于筛选最小的distance
			// 选择最小的Distance,对应的节点为next
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// 从非S集合中选择
					if (Distance[i] < Min) {
						Min = Distance[i];
						next = i;
					}
				}
			}

			// 以next来松弛路径
			S[next] = true;
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// 松弛非S集合中的顶点
					if (Distance[next] + Edge[next][i] < Distance[i]) {
						Distance[i] = Distance[next] + Edge[next][i];
						pre[i] = next;
					}
				}
			}
		}
		// 找出到终点的路径,回溯法
		List<Integer> path = new ArrayList<Integer>();
		while (pre[end] != -1) {
			path.add(end);
			end = pre[end];
		}
		path.add(start);// 加上起始节点,自己思考...
		// 每次调用该方法时候，都会触发调用次数自增的操作
		CallDijkstraTime++;
		return path;// 这是反向的路径，比如输出36-32-18-2。实际路径是2-18-32-36。其中数字是节点编号，编号从0开始。
	}

	/**
	 * 
	 * Function: 
	 * 		判断S集合是不是已经满了，该方法作为上个DijkstraOfPath方法的辅助方法。 
	 * Details:
	 * 		S集合表示已经访问的节点集合。这里用布尔数组模拟集合。 当且仅当所有节点都在S中，返回真 
	 * Remark: 2018年9月18日 下午10:26:34
	 */
	protected boolean isFullOfTrue(boolean[] S) {
		boolean full = true;
		for (int i = 0; i < S.length; i++) {
			if (!S[i]) {
				full = false;
			}
		}
		return full;
	}

	/**
	 * 
	 * Function:
	 * 		 这是对于一个网络，同时求出两条最短路径。分别是对应路径上的c最小和d最小。
	 * 		也就是说求两点连线上的两个端点。
	 * Details:
	 * 		用pre1和pre2分别记录c最小和d最小路径的前驱节点,Distance记录边的连接权重，这里权重是新网络的。
	 * 		当松弛的时候对这三种情况进行处理。 如果松弛结果相同，则如果c可以更新则更新c，如果d可以更新则更新d。
	 * 		如果松弛结果不相同，则更新所有。（所有是指pre1,pre2,Disatcen) 
	 * Remark: 2018年9月19日 上午10:31:32
	 */
	public List<List<Integer>> AdjustDijkstraOfPath(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink,
			int start, int end) {
		final int NumberOfNode = Node.length;
		int next = -1;// 记录选择的下一个结点
		boolean[] S = new boolean[NumberOfNode];// S集合
		int[] pre1 = new int[NumberOfNode];// 记录前驱节点c最小
		int[] pre2 = new int[NumberOfNode];// 记录前驱节点d最小
		double[] Distance = new double[NumberOfNode];// 构造新网络中的代价矩阵
		double[] c = new double[NumberOfNode];// 构造原网络中的代价矩阵
		double[] d = new double[NumberOfNode];// 构造原网络中的延时矩阵

		// 初始化
		for (int i = 0; i < pre1.length; i++) {
			pre1[i] = -1;
			pre2[i] = -1;
		}
		for (int i = 0; i < NumberOfNode; i++) {
			Distance[i] = Edge[start][i];
			c[i] = IdLink[start][i] == -1 ? Constant.MAX_VALUE : Id[IdLink[start][i]][2];// IdLink中如果值为-1，说明两点直接没有链路
																							// ,具体参考README
			d[i] = IdLink[start][i] == -1 ? Constant.MAX_VALUE : Id[IdLink[start][i]][3];
			pre1[i] = start;// 在此记录前驱
			pre2[i] = start;
		}
		c[start] = d[start] = 0;
		S[start] = true;
		pre1[start] = -1;
		pre2[start] = -1;

		// 核心
		while (!isFullOfTrue(S)) {
			// 用于筛选最小的distance，并将最小distance对应的节点作为next
			double Min = Integer.MAX_VALUE;
			for (int i = 0; i < NumberOfNode; i++)
				if (!S[i]) {// 从非S集合中选择
					if (Distance[i] < Min) {
						Min = Distance[i];
						next = i;
					}
				}

			// 以next来松弛路径
			S[next] = true;
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// 松弛非S集合中的顶点
					if (Distance[next] + Edge[next][i] < Distance[i] - Constant.esp) {
						Distance[i] = Distance[next] + Edge[next][i];
						pre1[i] = next;
						pre2[i] = next;
						c[i] = c[next] + Id[IdLink[next][i]][2];
						d[i] = d[next] + Id[IdLink[next][i]][3];
					} else if (Distance[next] + Edge[next][i] <= Distance[i] + Constant.esp
							&& Distance[next] + Edge[next][i] >= Distance[i] - Constant.esp) {
						if (c[next] + Id[IdLink[next][i]][2] < c[i]) {
							pre1[i] = next;
							c[i] = c[next] + Id[IdLink[next][i]][2];
						}
						if (d[next] + Id[IdLink[next][i]][3] < d[i]) {
							pre2[i] = next;
							d[i] = d[next] + Id[IdLink[next][i]][3];
						}
					}
				}
			}
		}

		// 找出到终点的路径
		List<List<Integer>> paths = new ArrayList<>();
		List<Integer> pathc = new ArrayList<>();
		List<Integer> pathd = new ArrayList<>();
		pathc = SinglePath(pre1, start, end);// 实际上这里将回溯求路径转移到SinglePath方法中
		pathd = SinglePath(pre2, start, end);
		paths.add(pathc);
		paths.add(pathd);

		return paths;// 这是反向的路径
	}

	/**
	 * 
	 * Function: 
	 * 		依据给定的起始节点和结束节点和前驱矩阵，进行回溯求得路径。
	 * Details:
	 * 		这是上面AdjustDijkstraOfPath方法的辅助方法，实现代码重用。 
	 * Remark: 2018年9月19日 上午10:41:33
	 */
	protected static List<Integer> SinglePath(int[] pre, int start, int end) {
		List<Integer> path = new ArrayList<Integer>();
		while (pre[end] != -1) {
			path.add(end);
			end = pre[end];
		}
		path.add(start);
		return path;// 这是反向的路径
	}

	/**
	 * 
	 * Function:
	 *		这是在给定三角形区域内进行搜索，返回搜索区域内满足条件的所有最短路径
	 * Details:
	 *		该方法主要是作为本包中的SearchTriangle类的辅助方法
	 * Remark: 2018年9月19日 上午10:59:00
	 */
	// 下面这方法用于求二分法带搜索三角形,在上面的基础上稍作修改
	public List<List<Integer>> YenWithSearch(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink, int start,
			int end, double baseCost, double theta, double delayConstriant) {
		// baseCost是相对的cost，lambda是斜率
		// 明确要使用三个容器，一个用来装候选路径，一个装最终定下来的路径，一个装丢弃的路径
		List<List<Integer>> A = new ArrayList<>();
		List<List<Integer>> B = new ArrayList<>();
		List<List<Integer>> C = new ArrayList<>();
		int loop = 0;// 初始化循环次数
		List<Integer> shortestPath = DijkstraOfPath(Node, Edge, start, end);// 调用dijkstra算法求得最短路径
		double[][] subEdge = Common.deepCloneEdge(Edge);
		while (loop < Constant.notExistsPathForYenKValue) {
			// 判断当前最短路径是否满足条件,这里的条件不仅仅是判断是否超出给定的notExistsPathForValue值。
			// 同时如果还超出baseCost+Constant.esp。其中baseCost是初始点先与延时阈值线段的交点，然后该交点
			// 做斜率tan(theta)的直线与y轴交点的那个数值，就是baseCost。
			// 之后的每个点都是这么求其current。current的意义和baseCost相同。
			Edge = Common.deepCloneEdge(subEdge);
			double shortCost = 0;
			for (int i = shortestPath.size() - 1; i >= 1; i--) {
				int node1 = shortestPath.get(i);
				int node2 = shortestPath.get(i - 1);
				shortCost += Edge[node1][node2];
			}
			// 如果超出了更新的上界，那么就停机，加快搜索速度
			if (shortCost > Constant.notExistsPathForValue || shortCost > baseCost + Constant.esp) {// 这里5000依据具体情况调整
				return A;
			}

			// 计算当前点的上界值，然后和更新的上界进行比较，决定是否要继续更新上界
			double c = Common.Ctheta(shortestPath, Id, IdLink);
			double d = Common.Ptheta(shortestPath, Id, IdLink);
			double current = Math.cos(theta) * c + delayConstriant * Math.sin(theta);
			if (current < baseCost && d <= delayConstriant) {// 当前更新的前提是该点的延时，延时首先得满足延时阈值
				baseCost = current;
			}

			double MAX_NUMBER = Constant.ExistsPathForValue;// 用于筛选最短路径
			A.add(shortestPath);// 第一个无争议地定下来的路径
			// 下面开始依次删除边,这和上面的YenBasedDelayConstraint方法该步骤意思相同
			List<Integer> rootPath = new ArrayList<>();
			for (int i = shortestPath.size() - 1; i >= 1; i--) {
				int relateI = shortestPath.size() - 1 - i;// 相对变量i从0到size-1
				rootPath.add(shortestPath.get(i));
				int spurNode = shortestPath.get(i);
				Edge = Common.deepCloneEdge(subEdge);// 使用未变的副本
				// 接下来删除除了spurNode之外的所有节点
				for (int j = 0; j < rootPath.size(); j++) {
					if (rootPath.get(j) != spurNode) {
						for (int m = 0; m < Edge.length; m++) {
							Edge[rootPath.get(j)][m] = Constant.MAX_VALUE;// 删除行
							Edge[m][rootPath.get(j)] = Constant.MAX_VALUE;// 删除列
							// 以上表示已经删除非spur节点
						}
					}
				}
				// 得到更新后的Edge
				// 下面求spurPath
				int leftNode = shortestPath.get(i);// 要删除边的根点
				List<Integer> nodes = new ArrayList<>();// 要删除边的下一个节点的集合
				for (int j = 0; j < A.size(); j++) {// 依次遍历A中所有定下来的链表
					if (relateI >= A.get(j).size() - 1)// 已经改动
						continue;
					if (A.get(j).size() <= rootPath.size())
						break;
					int len = A.get(j).size();
					int current1 = 0;
					boolean add = true;
					int h = len - 1;
					for (; h >= len - rootPath.size(); h--) {
						if (A.get(j).get(h) == rootPath.get(current1)) {
							current1++;
						} else {
							add = false;
							break;
						}
					}
					if (add)
						nodes.add(A.get(j).get(h));
				}
				// 至此已经得到所有与leftNode相连的A中点
				// 重点：开始依次删边
				for (int j = 0; j < nodes.size(); j++) {
					int rightNode = nodes.get(j);
					Edge[leftNode][rightNode] = Constant.MAX_VALUE;
				}
				List<Integer> spurpath = DijkstraOfPath(Node, Edge, spurNode, end);// 这是删除边后得到最短路径
				List<Integer> path = conj(rootPath, spurpath);
				if ((!A.contains(path)) && (!B.contains(path)))// 保证该路径与之前的不重复
					B.add(path);
			}

			if (B.size() == 0) {
				return A;// 如果没有候选的就跳出
			}

			Edge = Common.deepCloneEdge(subEdge);
			int u = -1;// 记下留下的最小的路径
			for (int i = 0; i < B.size(); i++) {
				double cost = 0;// 用于比较最小的cost
				for (int j = B.get(i).size() - 1; j >= 1; j--) {
					int foreNode = B.get(i).get(j);
					int nextNode = B.get(i).get(j - 1);
					cost += Edge[foreNode][nextNode];
				} // 求出拿出来的第一个路径的代价
				if (cost < MAX_NUMBER) {
					MAX_NUMBER = cost;
					shortestPath = Common.deepCloneList(B.get(i));
					u = i;
				}
			}
			B.remove(u);// 将该路径从容器中删除之
			C.add(shortestPath);// 回收删除的路径
			loop++;
		}
		return A;
	}

	/**
	 * 
	 * Function:
	 * 		 将给定的两条路径进行连接成一条路径
	 * Details:
	 * 		 按照Yen算法的规定进行连接路径。这是Yen算法的辅助方法。
	 * 		首先将spurPath从头到尾加到新的列表中，然后将rootPath从尾到头加到列表后面。 
	 * Remark: 2018年9月19日 上午11:12:35
	 */
	protected List<Integer> conj(List<Integer> rootPath, List<Integer> spurPath) {
		List<Integer> path = new ArrayList<>();
		for (int i = 0; i < spurPath.size() - 1; i++) {
			path.add(spurPath.get(i));
		}
		for (int j = rootPath.size() - 1; j >= 0; j--) {
			path.add(rootPath.get(j));
		}
		return path;// 逆序的
	}

	/**
	 * 
	 * Function: 
	 * 		该方法是返回调用Dijkstra的次数 
	 * Details:
	 * 		由于每次调用DijkstraOfPath方法，本类中的CallDijkstraTime全局变量就会自增。
	 * 		这里当运行完成后，调用该方法就能求出所调用dijkstra的次数。 
	 * Remark: 2018年9月19日 上午11:16:16
	 */
	public int getCallDijkstraTime() {
		return CallDijkstraTime;
	}
}
