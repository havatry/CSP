package randomTopology;

import java.util.Comparator;

/**
 * 
 * OverView:
 *		该类提供了链路信息。并且实现了排序。
 *		主要完成依据链路的长度来赋不同的delay。
 */
public class Pair implements Comparator<Pair> {// pair也可理解为链路
	private int start;// 链路起点
	private int end;// 链路终点
	private double distance;// 链路长度
	private int delay;// 链路延时
	private int cost;// 链路代价

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
		// 实现排序的规则，依据链路的长度
		if (o1.getDistance() - o2.getDistance() > 0)
			return 1;
		else if (o1.getDistance() - o2.getDistance() == 0)
			return 0;
		else
			return -1;
	}
}
