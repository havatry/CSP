package randomTopology;

import java.io.Serializable;

/**
 * 
 * OverView:
 *		�����ǽڵ��ࡣ�ṩ�ڵ����Ϣ��
 */
public class Point implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private int x;// �ڵ������
	private int y;// �ڵ�������
	private int lable;// �ڵ���

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
