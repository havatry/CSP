package randomTopology;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
		// ���л�
		String basePath = XMLHelper.getValue("allInfo/file/@basePath");
		String nodeOutput = basePath + XMLHelper.getValue("allInfo/file/nodeFile");
		//���ڵ�������л����ļ��У����ļ��Ƕ����Ƶ�
		try (ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(new File(nodeOutput.replace(".", "_" + Constant.WriteFile_TimeFor + "."))))) {
			out.writeObject(points);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return points;
	}

	/**
	 * 
	 * Function:
	 *		��ȡָ���ļ��еĽڵ����
	 * Details:
	 *		ʹ�÷����л�����ȡ
	 * Remark: 2018��9��19�� ����1:13:09
	 */
	public Point[] getPoints(int time) {// ���ڼ������ļ�,������
		String basePath = XMLHelper.getValue("allInfo/file/@basePath");
		String nodeOutput = basePath + XMLHelper.getValue("allInfo/file/nodeFile");
		//�����л�
		try (ObjectInputStream out = new ObjectInputStream(
				new FileInputStream(new File(nodeOutput.replace(".", "_" + time + "."))))) {
			Point[] points = (Point[]) out.readObject();
			return points;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
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
