package randomTopology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 
 * OverView: 
 * 		������Ҫ���һ��������������ɡ� �ṩ
 */
public class Topology {
	private Point[] points;// �ڵ����
	private Graph graph;// ��ǰ����ͼ
	private List<Integer> coreNodes;// ���Ľڵ����б�

	public Topology() {
		// TODO Auto-generated constructor stub
		points = new RandomPoint().getPoints(Constant.numNodes, Constant.W);// ������ɽڵ����
		graph = new HamiltonGraph(Constant.numNodes).connectHamilton(); // ���ӹ��ܶ���Ȧ
		coreNodes = new CoreNode().findCoreNodes(Constant.numNodes);// �Һ��Ľڵ�
	}

	/**
	 * 
	 * Function: 
	 * 		���ķ����������������� 
	 * Details: 
	 * 		�÷����������Ѿ��ǹ��ܶ���Ȧ������ͼ�У���������һЩ�ߣ��õ����Ƶ����ˡ�
	 * 		���Ž������еĽڵ���󡢺��Ľڵ��š����������������·����д�뵽ָ���ļ��С�
	 * Remark: 2018��9��19�� ����1:16:48
	 */
	public void ProduceTopology() {
		ConnectEdges();
		writeNodeToFile();
		writeCoreNodeToFile(coreNodes);
		writeEdgeLinkToFile();
		writeIdToFile();
	}

	/**
	 * 
	 * Function: 
	 * 		���������ļ����Թ��ܶ���Ȧ�����磬�������ӱߣ���������
	 * Details:
	 * 		���������ļ��������������ڵ����߸���probility�� Ȼ��ݴ������������ж��������ڵ��Ƿ�����ӱߡ�
	 * Remark: 2018��9��19�� ����1:19:33
	 */
	public void ConnectEdges() {// �ļ�����
		for (int i = 0; i < Constant.numNodes; i++) {
			boolean isCore = coreNodes.contains(i);
			for (int j = 0; j < Constant.numNodes; j++) {
				if (i == j)
					continue;// ͬһ����
				double probility = 0.0;
				if (isCore) {// �����������
					if (coreNodes.contains(j)) {// ���ĶԺ���
						probility = Constant.coreToCoreProbility;
					} else {// ���ĶԱ�Ե
						probility = Constant.coreToNormalProbility;
					}
				} else {// ��Ե�Ժ���
					if (coreNodes.contains(j)) {
						probility = Constant.coreToNormalProbility;
					}
				}
				if (Math.random() < probility) {// �����������ĸ��ʣ�����ӱ�
					graph.addUndirectedEdge(i, j);
				}
			}
		}
	}

	/**
	 * 
	 * Function: 
	 * 		����·����д���ļ��� 
	 * Details: 
	 * 		ʹ�����ȶ��У�����·������뵽���ȶ����У���ɶ�ÿ����·�������ʱ���и�ֵ��
	 * 		���������и�ֵ����·����д���ļ��С�
	 * Remark: 2018��9��19�� ����1:21:32
	 */
	public void writeIdToFile() {
		// ��������ɵ��������˼���ʱ�ʹ���
		PriorityQueue<Pair> pq = new PriorityQueue<>(new Pair());// ���ȶ���
		for (Node node : graph.nodes) {// ����ÿ���ڵ�
			for (Integer value : node.getNeighbors()) {
				Pair tmp = new Pair();
				tmp.setStart(node.getIdentifier());
				tmp.setEnd(value);
				double distance = Math.sqrt(Math.pow(points[node.getIdentifier()].getX() - points[value].getX(), 2)
						+ Math.pow(points[node.getIdentifier()].getY() - points[value].getY(), 2));
				tmp.setDistance(distance);
				int cost = (int) (Math.random() * 30) + 1;// 1-15
				tmp.setCost(cost);
				// ����һ����ص�ɾ������Ϊһ���߶�Ӧ����������·����������·���������յ�ǡ���෴��
				// ����Ķ���ͬ��ʵ�����������ɾ�������յ�͵�ǰ�����·ǡ���෴���Ǹ���·
				// Ҳ���ǽ������յ���ھӵ�����
				graph.nodes.get(value).removeEdgeTo(node.getIdentifier());
				pq.offer(tmp);// ����ǰ����·������뵽���ȶ����У�������·����û�ж���ʱ��ֵ
			}
		}
		// ��ǰ75%������������ʱ1-5,��5%����20-30,��������5-8
		int pre = (int) (pq.size() * 0.75);
		int post = (int) (pq.size() * 0.95);
		int size = pq.size();
		PrintWriter idout = null;
		try {
			idout = new PrintWriter(Constant.idFile.replace(".", "_" + Constant.WriteFile_TimeFor + "."));// ͨ��
			idout.println("id\t���\t�յ�\t����\t����\t��ʱ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��д��ǰ��ɶ���ʱ�ĸ�ֵ��һ��д��
		for (int i = 0; i < size; i++) {
			int delay;
			if (i < pre)
				delay = (int) (Math.random() * 10) + 1;// 1-5
			else if (i >= pre && i < post)
				delay = (int) (Math.random() * 11) + 10;// 5-8
			else
				delay = (int) (Math.random() * 11) + 20;// 20-30
			Pair p = pq.poll();
			p.setDelay(delay);
			idout.println(2 * i + "\t" + p.getStart() + "\t" + p.getEnd() + "\t"
					+ String.format("%.1f", p.getDistance()) + "\t" + p.getCost() + "\t" + p.getDelay());
			idout.println((2 * i + 1) + "\t" + p.getEnd() + "\t" + p.getStart() + "\t"
					+ String.format("%.1f", p.getDistance()) + "\t" + p.getCost() + "\t" + p.getDelay());
		}
		idout.close();
	}

	/**
	 * 
	 * Function: 
	 * 		����������Ϣд�뵽�ļ��� 
	 * Details: 
	 * 		д�뵽���ļ����ڻ���ͼ�ν����ʱ����õ�
	 * Remark: 2018��9��19�� ����1:27:04
	 */
	public void writeEdgeLinkToFile() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(Constant.edgeFile.replace(".", "_" + Constant.WriteFile_TimeFor + "."));
			out.println(graph);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * 
	 * Function: 
	 * 		���ڵ����д�뵽�ļ���
	 * Details: 
	 * 		д����ļ����������˺ͻ���ͼ�ν����ʱ�򶼻��õ�����������ڻ���ͼ�ε�ʱ���õ���
	 * Remark: 2018��9��19�� ����1:28:16
	 */
	public void writeNodeToFile() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				new File(Constant.nodeFile.replace(".", "_" + Constant.WriteFile_TimeFor + "."))))) {
			out.writeObject(points);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Function: 
	 * 		�����Ľڵ�д�뵽�ļ��� 
	 * Details: 
	 * 		д����ļ�������ڻ���ͼ�ν����ʱ���õ� 
	 * Remark: 2018��9��19�� ����1:29:17
	 */
	public void writeCoreNodeToFile(List<Integer> coreNodeLists) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(Constant.coreNodeFile.replace(".", "_" + Constant.WriteFile_TimeFor + "."));
			for (int i = 0; i < coreNodeLists.size(); i++)
				out.println("node: " + coreNodeLists.get(i));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
