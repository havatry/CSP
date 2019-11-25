package randomTopology;

/**
 * 
 * OverView: 
 * 		��������������������֮�����ӳ�һ�����ܶ���Ȧ ��֤�������ͨ�ԡ� 
 * 		�����ṩ��ԭ��������ӹ��ܶ���Ȧ������
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
	 *		������ܶ���Ȧ
	 * Details:
	 *		�����нڵ�ı�Ŵ�ͷ��β��������
	 * Remark: 2018��9��19�� ����1:05:07
	 */
	protected Graph connectHamilton() {
		for (int i = 0; i < numNodes - 1; i++) {
			addUndirectedEdge(i, i + 1);
		}
		addUndirectedEdge(numNodes - 1, 0);
		return this;
	}
}
