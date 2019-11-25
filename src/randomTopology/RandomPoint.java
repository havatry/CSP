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
 * Description: 在给定的方形区域内放置点
 * <p>
 * @author dedong
 * <p>
 * @date 2018年7月4日/ 下午1:36:01
 * 
 * OverView: 
 * 		补充:该类完成了在给定的正方形区域内随机生成节点。
 * 			该类提供生成随机节点矩阵、获取指定文件的节点矩阵方法。
 */
public class RandomPoint {
   /**
	* 
    * Function:
	*		核心方法。在给定节点数和边长大小的正方形区域内生成节点矩阵
	* Details:
	*		在正方形区域内先随机生成节点横坐标和纵坐标。
	*		然后保证该点必须要和其余所有已经生成的点的距离至少是W/(2*Math.sqrt(n))
	*		其中W是边长，n是节点数
	* Remark: 2018年9月19日 下午1:09:56
	*/
	public Point[] getPoints(int n, double W) {
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = (int) (Math.random() * 1000);
			int y = (int) (Math.random() * 500);
			if (!Avaliable(points, i - 1, x, y, W / (2 * Math.sqrt(n)))) {// 保证距离
				i--;
				continue;
			}
			points[i] = new Point();
			points[i].setLable(i);
			points[i].setX(x);
			points[i].setY(y);
		}
		// 序列化
		String basePath = XMLHelper.getValue("allInfo/file/@basePath");
		String nodeOutput = basePath + XMLHelper.getValue("allInfo/file/nodeFile");
		//将节点矩阵序列化到文件中，该文件是二进制的
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
	 *		获取指定文件中的节点矩阵
	 * Details:
	 *		使用反序列化来获取
	 * Remark: 2018年9月19日 下午1:13:09
	 */
	public Point[] getPoints(int time) {// 读第几个的文件,不常用
		String basePath = XMLHelper.getValue("allInfo/file/@basePath");
		String nodeOutput = basePath + XMLHelper.getValue("allInfo/file/nodeFile");
		//反序列化
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
	 *		判断给定点和其余已经生成点的距离是否都保存着标准距离，标准距离为W/(2*Math.sqrt(n))
	 * Details:
	 *		比较当前节点和节点矩阵p，判断是否对应已经生成的点是否都满足距离关系
	 * Remark: 2018年9月19日 下午1:13:48
	 */
	private boolean Avaliable(Point[] p, int end, int x, int y, double stardDistance) {
		// 判断该点是否满足最小距离W/2sqrt(n)
		for (int i = 0; i <= end; i++) {
			if (Math.sqrt(Math.pow(p[i].getX() - x, 2) + Math.pow(p[i].getY() - y, 2)) < stardDistance) {
				return false;
			}
		}
		return true;
	}
}
