package randomTopology;

import java.io.Serializable;

/**
 * 
 * OverView:
 *		该类是节点类。提供节点的信息。
 */
public class Point implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private int x;// 节点横坐标
	private int y;// 节点纵坐标
	private int lable;// 节点编号

	public Point() {
		// TODO Auto-generated constructor stub
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getLable() {
		return lable;
	}

	public void setLable(int lable) {
		this.lable = lable;
	}

	public int getX() {
		return x;
	}

	public void setX(double x) {
		this.x = (int) x;
	}

	public int getY() {
		return y;
	}

	public void setY(double y) {
		this.y = (int) y;
	}
}
