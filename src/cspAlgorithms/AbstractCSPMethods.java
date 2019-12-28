package cspAlgorithms;

import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		��������CSP�㷨�Ļ��ࡣ�û���ʵ����CSP�ӿڣ������ṩ��һЩ����������������Щ��������
 * 		����thetaʱ��ȡ�������·������ȡ��С�ȡ���ȡƽ���ȡ�
 */
public abstract class AbstractCSPMethods implements CSP {
	protected double esp = Constant.esp;// ���ַ���LARAC�ľ���
	protected double theta = -1.0;// ������ʾû���ҵ�·��
	protected int CallDijkstraTime = 0;// ���ص���Dijkstra�㷨�Ĵ���

	@Override
	public double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink) {// �������·���ϵĴ���
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
	public double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink) {// �������·���ϵ���ʱ
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
	 * 		�ڸ���������������£�ͨ��theta���¹���ߵ�Ȩ�ء�
	 * 		����Ȩ�ض���Ϊc=cos(theta)*c+sin(theta)*d,c��ԭ��
	 * 		�����е�Ȩ�أ�d��ԭ�������е���ʱ�������µĴ��۵������У���start�ڵ㵽end�ڵ����һ��·����
	 * Details:
	 * 		��·���ķ�����dijkstra�㷨���ú���ÿ�α����õ�ʱ��dijkstra�㷨Ҳ�����á�
	 * 		����ܵĵ���dijkstra���� ����Ӧ������
	 * Remark: 2018��9��18�� ����9:14:35
	 */
	protected List<Integer> getPath(int[] Node, double[][] Id, double theta, int start, int end) {
		// �����������Ȩ��
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

		// ����dijkstra�㷨����
		CallDijkstraTime++;
		return new Dijkstra().DijkstraOfPath(Node, Edge, start, end);
	}

	/**
	 * 
	 * Function: 
	 * 		��ʵ��������thetaΪPI/2��ʱ�����·������·����Ӧ����ʱ��Ϊ��С��ʱ��
	 * Details:
	 * 		��thetaΪPI/2��ʱ���µ�����Ȩ�ؾͱ��ԭ���������ʱ��
	 * 		��ʱ���·�����Ƕ�Ӧԭ���������С��ʱ��
	 * Remark: 2018��9��18�� ����9:27:11
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
	 * 		 ��ȡ�����ƽ���ȡ� 
	 * Detail:
	 * 		ƽ���ȵ����������е����б��������ܵĽڵ���������Ķ�ʵ�����ǰ������Ⱥ���ȡ�
	 * 		����˵����һ���߶�Ӧ�Ķ���2����������Excel�л����2��
	 * 		Id�ľ��庬����ο�randomTopology����Topology����ܡ�
	 * Remark: 2018��9��18��  ����9:29:32
	 */
	public static double getAverageDegree(int[] Node, double[][] Id) {
		return (double) Id.length / Node.length;
	}
}
