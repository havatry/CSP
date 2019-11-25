package randomTopology;

import java.util.Comparator;

/**
 * 
 * OverView:
 *		�����ṩ����·��Ϣ������ʵ��������
 *		��Ҫ���������·�ĳ���������ͬ��delay��
 */
public class Pair implements Comparator<Pair> {// pairҲ�����Ϊ��·
	private int start;// ��·���
	private int end;// ��·�յ�
	private double distance;// ��·����
	private int delay;// ��·��ʱ
	private int cost;// ��·����

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public int compare(Pair o1, Pair o2) {
		// TODO Auto-generated method stub
		// ʵ������Ĺ���������·�ĳ���
		if (o1.getDistance() - o2.getDistance() > 0)
			return 1;
		else if (o1.getDistance() - o2.getDistance() == 0)
			return 0;
		else
			return -1;
	}
}
