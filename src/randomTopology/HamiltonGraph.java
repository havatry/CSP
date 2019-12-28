package randomTopology;

/**
 * 
 * OverView: 
 * 		在网络的所有随机孤立点之间连接成一个哈密尔顿圈 保证网络的连通性。 
 * 		该类提供给原先网络添加哈密尔顿圈方法。
 */
public class HamiltonGraph extends Graph {
	private int numNodes;

	public HamiltonGraph(int numNodes) {
		super(numNodes);
		// TODO Auto-generated constructor stub
		this.numNodes = numNodes;
	}

	/**
	 * 
	 * Function:
	 *		构造哈密尔顿圈
	 * Details:
	 *		将所有节点的编号从头到尾依次连接
	 * Remark: 2018年9月19日 下午1:05:07
	 */
	protected Graph connectHamilton() {
		for (int i = 0; i < numNodes - 1; i++) {
			addUndirectedEdge(i, i + 1);
		}
		addUndirectedEdge(numNodes - 1, 0);
		return this;
	}
}
