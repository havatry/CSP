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
 * 		该类主要完成一个网络的拓扑生成。 提供
 */
public class Topology {
	private Point[] points;// 节点矩阵
	private Graph graph;// 当前网络图
	private List<Integer> coreNodes;// 核心节点编号列表

	public Topology() {
		// TODO Auto-generated constructor stub
		points = new RandomPoint().getPoints(Constant.numNodes, Constant.W);// 随机生成节点矩阵
		graph = new HamiltonGraph(Constant.numNodes).connectHamilton(); // 连接哈密尔顿圈
		coreNodes = new CoreNode().findCoreNodes(Constant.numNodes);// 找核心节点
	}

	/**
	 * 
	 * Function: 
	 * 		核心方法，用来生成拓扑 
	 * Details: 
	 * 		该方法首先在已经是哈密尔顿圈的网络图中，继续连接一些边，得到完善的拓扑。
	 * 		接着将网络中的节点矩阵、核心节点编号、边连接情况、和链路矩阵写入到指定文件中。
	 * Remark: 2018年9月19日 下午1:16:48
	 */
	public void ProduceTopology() {
		ConnectEdges();
		writeIdToFile();
	}

	/**
	 * 
	 * Function: 
	 * 		按照配置文件来对哈密尔顿圈的网络，继续连接边，进行完善
	 * Details:
	 * 		按照配置文件，来决定两个节点连边概率probility。 然后据此来决定网络中对这两个节点是否继续加边。
	 * Remark: 2018年9月19日 下午1:19:33
	 */
	public void ConnectEdges() {// 文件操作
		for (int i = 0; i < Constant.numNodes; i++) {
			boolean isCore = coreNodes.contains(i);
			for (int j = 0; j < Constant.numNodes; j++) {
				if (i == j)
					continue;// 同一个点
				double probility = 0.0;
				if (isCore) {// 考虑四种情况
					if (coreNodes.contains(j)) {// 核心对核心
						probility = Constant.coreToCoreProbility;
					} else {// 核心对边缘
						probility = Constant.coreToNormalProbility;
					}
				} else {// 边缘对核心
					if (coreNodes.contains(j)) {
						probility = Constant.coreToNormalProbility;
					}
				}
				if (Math.random() < probility) {// 如果满足上面的概率，则添加边
					graph.addUndirectedEdge(i, j);
				}
			}
		}
	}

	/**
	 * 
	 * Function: 
	 * 		将链路矩阵写入文件中 
	 * Details: 
	 * 		使用优先队列，将链路对象加入到优先队列中，完成对每个链路对象的延时进行赋值。
	 * 		最后将完成所有赋值的链路对象写入文件中。
	 * Remark: 2018年9月19日 下午1:21:32
	 */
	public void writeIdToFile() {
		// 下面给生成的网络拓扑加延时和代价
		PriorityQueue<Pair> pq = new PriorityQueue<>(new Pair());// 优先队列
		for (Node node : graph.nodes) {// 遍历每个节点
			for (Integer value : node.getNeighbors()) {
				Pair tmp = new Pair();
				tmp.setStart(node.getIdentifier());
				tmp.setEnd(value);
				double distance = Math.sqrt(Math.pow(points[node.getIdentifier()].getX() - points[value].getX(), 2)
						+ Math.pow(points[node.getIdentifier()].getY() - points[value].getY(), 2));
				tmp.setDistance(distance);
				int cost = (int) (Math.random() * 30) + 1;// 1-15
				tmp.setCost(cost);
				// 将另一个相关的删除，因为一个边对应的是两个链路。这两个链路除了起点和终点恰好相反外
				// 其余的都相同。实际上这里就是删除起点和终点和当前这个链路恰好相反的那个链路
				// 也就是将起点和终点的邻居调整下
				graph.nodes.get(value).removeEdgeTo(node.getIdentifier());
				pq.offer(tmp);// 将当前的链路对象加入到优先队列中，这里链路对象还没有对延时赋值
			}
		}
		// 对前75%个进行设置延时1-5,后5%设置20-30,其余设置5-8
		int pre = (int) (pq.size() * 0.75);
		int post = (int) (pq.size() * 0.95);
		int size = pq.size();
		PrintWriter idout = null;
		try {
			idout = new PrintWriter(Constant.idFile.replace(".", "_" + Constant.WriteFile_TimeFor + "."));// 通信
			idout.println("id\tsource\ttarget\tcost\tdelay");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 在写入前完成对延时的赋值，一并写入
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
					+ p.getCost() + "\t" + p.getDelay());
			idout.println((2 * i + 1) + "\t" + p.getEnd() + "\t" + p.getStart() + "\t"
					+ p.getCost() + "\t" + p.getDelay());
		}
		idout.close();
	}

	/**
	 * 
	 * Function: 
	 * 		将边连接信息写入到文件中 
	 * Details: 
	 * 		写入到的文件，在绘制图形界面的时候会用到
	 * Remark: 2018年9月19日 下午1:27:04
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
	 * 		将节点矩阵写入到文件中
	 * Details: 
	 * 		写入的文件在生成拓扑和绘制图形界面的时候都会用到。大多数是在绘制图形的时候用到。
	 * Remark: 2018年9月19日 下午1:28:16
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
	 * 		将核心节点写入到文件中 
	 * Details: 
	 * 		写入的文件大多数在绘制图形界面的时候用到 
	 * Remark: 2018年9月19日 下午1:29:17
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
