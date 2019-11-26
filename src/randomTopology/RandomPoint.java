package randomTopology;

/**
 * 
 * <p>
 * Title: RandomPoint.java
 * </p>
 * <p>
 * Description: �ڸ����ķ��������ڷ��õ�
 * <p>
 * @author dedong
 * <p>
 * @date 2018��7��4��/ ����1:36:01
 * 
 * OverView: 
 * 		����:����������ڸ�����������������������ɽڵ㡣
 * 			�����ṩ��������ڵ���󡢻�ȡָ���ļ��Ľڵ���󷽷���
 */
public class RandomPoint {
   /**
	* 
    * Function:
	*		���ķ������ڸ����ڵ����ͱ߳���С�����������������ɽڵ����
	* Details:
	*		����������������������ɽڵ������������ꡣ
	*		Ȼ��֤�õ����Ҫ�����������Ѿ����ɵĵ�ľ���������W/(2*Math.sqrt(n))
	*		����W�Ǳ߳���n�ǽڵ���
	* Remark: 2018��9��19�� ����1:09:56
	*/
	public Point[] getPoints(int n, double W) {
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = (int) (Math.random() * 1000);
			int y = (int) (Math.random() * 500);
			if (!Avaliable(points, i - 1, x, y, W / (2 * Math.sqrt(n)))) {// ��֤����
				i--;
				continue;
			}
			points[i] = new Point();
			points[i].setLable(i);
			points[i].setX(x);
			points[i].setY(y);
		}
		return points;
	}

	/**
	 * 
	 * Function:
	 *		�жϸ�����������Ѿ����ɵ�ľ����Ƿ񶼱����ű�׼���룬��׼����ΪW/(2*Math.sqrt(n))
	 * Details:
	 *		�Ƚϵ�ǰ�ڵ�ͽڵ����p���ж��Ƿ��Ӧ�Ѿ����ɵĵ��Ƿ���������ϵ
	 * Remark: 2018��9��19�� ����1:13:48
	 */
	private boolean Avaliable(Point[] p, int end, int x, int y, double stardDistance) {
		// �жϸõ��Ƿ�������С����W/2sqrt(n)
		for (int i = 0; i <= end; i++) {
			if (Math.sqrt(Math.pow(p[i].getX() - x, 2) + Math.pow(p[i].getY() - y, 2)) < stardDistance) {
				return false;
			}
		}
		return true;
	}
}
