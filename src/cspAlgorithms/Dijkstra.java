package cspAlgorithms;

import java.util.ArrayList;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		������Yen�㷨�������㷨������CSP�㷨�Ļ�������Ҫ����dijkstra�㷨�����·����
 * 		�ҳ���һ��������ʱ��ֵ��·������������������������ʱ��ֵ�����·�������ص���dijkstra����
 */
public class Dijkstra {
	private int CallDijkstraTime = 0;// ��ʼ������Dijkstra����

	/**
	 * 
	 * Function: 
	 * 		�����Լ�д��һ��Dijkstra�㷨�� 
	 * Details: 
	 * 		����ʵ�ֲο����ݽṹ�鼮 
	 * Remark: 2018��9��18�� ����10:21:56
	 */
	protected List<Integer> DijkstraOfPath(int[] Node, double[][] Edge, int start, int end) {
		// ��ʼ��
		final int NumberOfNode = Node.length;
		int next = -1;// ��¼ѡ�����һ�����
		boolean[] S = new boolean[NumberOfNode];// S����
		int[] pre = new int[NumberOfNode];// ��¼ǰ���ڵ�
		double[] Distance = new double[NumberOfNode];// ������̾������**
		for (int i = 0; i < pre.length; i++) {
			pre[i] = -1;
		}
		for (int i = 0; i < NumberOfNode; i++) {
			Distance[i] = Edge[start][i];
			pre[i] = start;// �ڴ˼�¼ǰ��
		}
		S[start] = true;
		pre[start] = -1;

		while (!isFullOfTrue(S)) {// ��S������
			double Min = Integer.MAX_VALUE;// ����ɸѡ��С��distance
			// ѡ����С��Distance,��Ӧ�Ľڵ�Ϊnext
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// �ӷ�S������ѡ��
					if (Distance[i] < Min) {
						Min = Distance[i];
						next = i;
					}
				}
			}

			// ��next���ɳ�·��
			S[next] = true;
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// �ɳڷ�S�����еĶ���
					if (Distance[next] + Edge[next][i] < Distance[i]) {
						Distance[i] = Distance[next] + Edge[next][i];
						pre[i] = next;
					}
				}
			}
		}
		// �ҳ����յ��·��,���ݷ�
		List<Integer> path = new ArrayList<Integer>();
		while (pre[end] != -1) {
			path.add(end);
			end = pre[end];
		}
		path.add(start);// ������ʼ�ڵ�,�Լ�˼��...
		// ÿ�ε��ø÷���ʱ�򣬶��ᴥ�����ô��������Ĳ���
		CallDijkstraTime++;
		return path;// ���Ƿ����·�����������36-32-18-2��ʵ��·����2-18-32-36�����������ǽڵ��ţ���Ŵ�0��ʼ��
	}

	/**
	 * 
	 * Function: 
	 * 		�ж�S�����ǲ����Ѿ����ˣ��÷�����Ϊ�ϸ�DijkstraOfPath�����ĸ��������� 
	 * Details:
	 * 		S���ϱ�ʾ�Ѿ����ʵĽڵ㼯�ϡ������ò�������ģ�⼯�ϡ� ���ҽ������нڵ㶼��S�У������� 
	 * Remark: 2018��9��18�� ����10:26:34
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
	 * 		 ���Ƕ���һ�����磬ͬʱ����������·�����ֱ��Ƕ�Ӧ·���ϵ�c��С��d��С��
	 * 		Ҳ����˵�����������ϵ������˵㡣
	 * Details:
	 * 		��pre1��pre2�ֱ��¼c��С��d��С·����ǰ���ڵ�,Distance��¼�ߵ�����Ȩ�أ�����Ȩ����������ġ�
	 * 		���ɳڵ�ʱ���������������д��� ����ɳڽ����ͬ�������c���Ը��������c�����d���Ը��������d��
	 * 		����ɳڽ������ͬ����������С���������ָpre1,pre2,Disatcen) 
	 * Remark: 2018��9��19�� ����10:31:32
	 */
	public List<List<Integer>> AdjustDijkstraOfPath(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink,
			int start, int end) {
		final int NumberOfNode = Node.length;
		int next = -1;// ��¼ѡ�����һ�����
		boolean[] S = new boolean[NumberOfNode];// S����
		int[] pre1 = new int[NumberOfNode];// ��¼ǰ���ڵ�c��С
		int[] pre2 = new int[NumberOfNode];// ��¼ǰ���ڵ�d��С
		double[] Distance = new double[NumberOfNode];// �����������еĴ��۾���
		double[] c = new double[NumberOfNode];// ����ԭ�����еĴ��۾���
		double[] d = new double[NumberOfNode];// ����ԭ�����е���ʱ����

		// ��ʼ��
		for (int i = 0; i < pre1.length; i++) {
			pre1[i] = -1;
			pre2[i] = -1;
		}
		for (int i = 0; i < NumberOfNode; i++) {
			Distance[i] = Edge[start][i];
			c[i] = IdLink[start][i] == -1 ? Constant.MAX_VALUE : Id[IdLink[start][i]][2];// IdLink�����ֵΪ-1��˵������ֱ��û����·
																							// ,����ο�README
			d[i] = IdLink[start][i] == -1 ? Constant.MAX_VALUE : Id[IdLink[start][i]][3];
			pre1[i] = start;// �ڴ˼�¼ǰ��
			pre2[i] = start;
		}
		c[start] = d[start] = 0;
		S[start] = true;
		pre1[start] = -1;
		pre2[start] = -1;

		// ����
		while (!isFullOfTrue(S)) {
			// ����ɸѡ��С��distance��������Сdistance��Ӧ�Ľڵ���Ϊnext
			double Min = Integer.MAX_VALUE;
			for (int i = 0; i < NumberOfNode; i++)
				if (!S[i]) {// �ӷ�S������ѡ��
					if (Distance[i] < Min) {
						Min = Distance[i];
						next = i;
					}
				}

			// ��next���ɳ�·��
			S[next] = true;
			for (int i = 0; i < NumberOfNode; i++) {
				if (!S[i]) {// �ɳڷ�S�����еĶ���
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

		// �ҳ����յ��·��
		List<List<Integer>> paths = new ArrayList<>();
		List<Integer> pathc = new ArrayList<>();
		List<Integer> pathd = new ArrayList<>();
		pathc = SinglePath(pre1, start, end);// ʵ�������ｫ������·��ת�Ƶ�SinglePath������
		pathd = SinglePath(pre2, start, end);
		paths.add(pathc);
		paths.add(pathd);

		return paths;// ���Ƿ����·��
	}

	/**
	 * 
	 * Function: 
	 * 		���ݸ�������ʼ�ڵ�ͽ����ڵ��ǰ�����󣬽��л������·����
	 * Details:
	 * 		��������AdjustDijkstraOfPath�����ĸ���������ʵ�ִ������á� 
	 * Remark: 2018��9��19�� ����10:41:33
	 */
	protected static List<Integer> SinglePath(int[] pre, int start, int end) {
		List<Integer> path = new ArrayList<Integer>();
		while (pre[end] != -1) {
			path.add(end);
			end = pre[end];
		}
		path.add(start);
		return path;// ���Ƿ����·��
	}

	/**
	 * 
	 * Function:
	 *		�����ڸ��������������ڽ����������������������������������������·��
	 * Details:
	 *		�÷�����Ҫ����Ϊ�����е�SearchTriangle��ĸ�������
	 * Remark: 2018��9��19�� ����10:59:00
	 */
	// �����ⷽ����������ַ�������������,������Ļ����������޸�
	public List<List<Integer>> YenWithSearch(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink, int start,
			int end, double baseCost, double theta, double delayConstriant) {
		// baseCost����Ե�cost��lambda��б��
		// ��ȷҪʹ������������һ������װ��ѡ·����һ��װ���ն�������·����һ��װ������·��
		List<List<Integer>> A = new ArrayList<>();
		List<List<Integer>> B = new ArrayList<>();
		List<List<Integer>> C = new ArrayList<>();
		int loop = 0;// ��ʼ��ѭ������
		List<Integer> shortestPath = DijkstraOfPath(Node, Edge, start, end);// ����dijkstra�㷨������·��
		double[][] subEdge = Common.deepCloneEdge(Edge);
		while (loop < Constant.notExistsPathForYenKValue) {
			// �жϵ�ǰ���·���Ƿ���������,������������������ж��Ƿ񳬳�������notExistsPathForValueֵ��
			// ͬʱ���������baseCost+Constant.esp������baseCost�ǳ�ʼ��������ʱ��ֵ�߶εĽ��㣬Ȼ��ý���
			// ��б��tan(theta)��ֱ����y�ύ����Ǹ���ֵ������baseCost��
			// ֮���ÿ���㶼����ô����current��current�������baseCost��ͬ��
			Edge = Common.deepCloneEdge(subEdge);
			double shortCost = 0;
			for (int i = shortestPath.size() - 1; i >= 1; i--) {
				int node1 = shortestPath.get(i);
				int node2 = shortestPath.get(i - 1);
				shortCost += Edge[node1][node2];
			}
			// ��������˸��µ��Ͻ磬��ô��ͣ�����ӿ������ٶ�
			if (shortCost > Constant.notExistsPathForValue || shortCost > baseCost + Constant.esp) {// ����5000���ݾ����������
				return A;
			}

			// ���㵱ǰ����Ͻ�ֵ��Ȼ��͸��µ��Ͻ���бȽϣ������Ƿ�Ҫ���������Ͻ�
			double c = Common.Ctheta(shortestPath, Id, IdLink);
			double d = Common.Ptheta(shortestPath, Id, IdLink);
			double current = Math.cos(theta) * c + delayConstriant * Math.sin(theta);
			if (current < baseCost && d <= delayConstriant) {// ��ǰ���µ�ǰ���Ǹõ����ʱ����ʱ���ȵ�������ʱ��ֵ
				baseCost = current;
			}

			double MAX_NUMBER = Constant.ExistsPathForValue;// ����ɸѡ���·��
			A.add(shortestPath);// ��һ��������ض�������·��
			// ���濪ʼ����ɾ����,��������YenBasedDelayConstraint�����ò�����˼��ͬ
			List<Integer> rootPath = new ArrayList<>();
			for (int i = shortestPath.size() - 1; i >= 1; i--) {
				int relateI = shortestPath.size() - 1 - i;// ��Ա���i��0��size-1
				rootPath.add(shortestPath.get(i));
				int spurNode = shortestPath.get(i);
				Edge = Common.deepCloneEdge(subEdge);// ʹ��δ��ĸ���
				// ������ɾ������spurNode֮������нڵ�
				for (int j = 0; j < rootPath.size(); j++) {
					if (rootPath.get(j) != spurNode) {
						for (int m = 0; m < Edge.length; m++) {
							Edge[rootPath.get(j)][m] = Constant.MAX_VALUE;// ɾ����
							Edge[m][rootPath.get(j)] = Constant.MAX_VALUE;// ɾ����
							// ���ϱ�ʾ�Ѿ�ɾ����spur�ڵ�
						}
					}
				}
				// �õ����º��Edge
				// ������spurPath
				int leftNode = shortestPath.get(i);// Ҫɾ���ߵĸ���
				List<Integer> nodes = new ArrayList<>();// Ҫɾ���ߵ���һ���ڵ�ļ���
				for (int j = 0; j < A.size(); j++) {// ���α���A�����ж�����������
					if (relateI >= A.get(j).size() - 1)// �Ѿ��Ķ�
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
				// �����Ѿ��õ�������leftNode������A�е�
				// �ص㣺��ʼ����ɾ��
				for (int j = 0; j < nodes.size(); j++) {
					int rightNode = nodes.get(j);
					Edge[leftNode][rightNode] = Constant.MAX_VALUE;
				}
				List<Integer> spurpath = DijkstraOfPath(Node, Edge, spurNode, end);// ����ɾ���ߺ�õ����·��
				List<Integer> path = conj(rootPath, spurpath);
				if ((!A.contains(path)) && (!B.contains(path)))// ��֤��·����֮ǰ�Ĳ��ظ�
					B.add(path);
			}

			if (B.size() == 0) {
				return A;// ���û�к�ѡ�ľ�����
			}

			Edge = Common.deepCloneEdge(subEdge);
			int u = -1;// �������µ���С��·��
			for (int i = 0; i < B.size(); i++) {
				double cost = 0;// ���ڱȽ���С��cost
				for (int j = B.get(i).size() - 1; j >= 1; j--) {
					int foreNode = B.get(i).get(j);
					int nextNode = B.get(i).get(j - 1);
					cost += Edge[foreNode][nextNode];
				} // ����ó����ĵ�һ��·���Ĵ���
				if (cost < MAX_NUMBER) {
					MAX_NUMBER = cost;
					shortestPath = Common.deepCloneList(B.get(i));
					u = i;
				}
			}
			B.remove(u);// ����·����������ɾ��֮
			C.add(shortestPath);// ����ɾ����·��
			loop++;
		}
		return A;
	}

	/**
	 * 
	 * Function:
	 * 		 ������������·���������ӳ�һ��·��
	 * Details:
	 * 		 ����Yen�㷨�Ĺ涨��������·��������Yen�㷨�ĸ���������
	 * 		���Ƚ�spurPath��ͷ��β�ӵ��µ��б��У�Ȼ��rootPath��β��ͷ�ӵ��б���档 
	 * Remark: 2018��9��19�� ����11:12:35
	 */
	private List<Integer> conj(List<Integer> rootPath, List<Integer> spurPath) {
		List<Integer> path = new ArrayList<>();
		for (int i = 0; i < spurPath.size() - 1; i++) {
			path.add(spurPath.get(i));
		}
		for (int j = rootPath.size() - 1; j >= 0; j--) {
			path.add(rootPath.get(j));
		}
		return path;// �����
	}

	/**
	 * 
	 * Function: 
	 * 		�÷����Ƿ��ص���Dijkstra�Ĵ��� 
	 * Details:
	 * 		����ÿ�ε���DijkstraOfPath�����������е�CallDijkstraTimeȫ�ֱ����ͻ�������
	 * 		���ﵱ������ɺ󣬵��ø÷����������������dijkstra�Ĵ����� 
	 * Remark: 2018��9��19�� ����11:16:16
	 */
	public int getCallDijkstraTime() {
		return CallDijkstraTime;
	}
}
