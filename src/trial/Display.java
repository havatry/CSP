package trial;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;

import fileInput.CoreNodeFile;
import fileInput.NodeFile;
import randomTopology.Point;
import randomTopology.RandomPoint;
import randomTopology.Constant;

/**
 * 
 * OverView:
 *		��ָ���ļ���Ż���ͼ�ν���
 */
public class Display extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private List<Integer> core;
	private Point[] points;

	public Display(int time) {
		Constant.TimeForTest = time;// ���Ե��ļ����
//		points = new RandomPoint().getPoints(Constant.TimeForTest);// ��ȡ���ļ��Ľڵ����
		points = NodeFile.GetNodes(Constant.TimeForTest);
		core = CoreNodeFile.GetCoreNodeIndex();// ��ȡ���ļ��ĺ��Ľڵ���
		setTitle("չʾͼ��");
		setSize(1300, 700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		int count = 0;
		// �������нڵ㣬���к��Ľڵ��ú�ɫ����
		for (Point p : points) {
			if (core.contains(p.getLable()))
				g.setColor(Color.red);
			g.fillOval((int) p.getX() + 100, (int) p.getY() + 100, 4, 4);
			g.drawString((count++) + "", (int) p.getX() + 100 + 6, (int) p.getY() + 100 + 6);
			g.setColor(Color.black);
		}
		// ����ÿ����֮�������
		Scanner in = null;
		try {
			// �Ӹ��ļ���ȡ��������Ϣ
			in = new Scanner(new File("resource/file/LinkInfo_" + Constant.TimeForTest + ".txt"));
			while (in.hasNext()) {
				String[] parts = in.nextLine().split(" ");
				g.drawLine(points[Integer.parseInt(parts[0])].getX() + 100,
						points[Integer.parseInt(parts[0])].getY() + 100,
						points[Integer.parseInt(parts[1])].getX() + 100,
						points[Integer.parseInt(parts[1])].getY() + 100);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
	}
}
