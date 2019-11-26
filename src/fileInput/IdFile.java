package fileInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		������Ҫͨ���ļ�����ȡID���� �ṩ��ȡ��·���󡢻�ȡ��Ȩ�ؾ��󡢻�ȡ�����Ӿ��󷽷���
 */
public class IdFile {
	/**
	 * 
	 * Function:
	 *		ͨ�������ļ�����ȡ�ļ��յ���·��Ϣ��
	 *		�ļ���ʽΪÿ�ж�Ӧ��㡢�յ㡢���ۺ���ʱ
	 * Details:
	 *		ͨ����ȡ�ļ�������������·����
	 *		Ȼ���ȡ�ض��ļ���д����·����
	 * Remark: 2018��9��19�� ����12:48:31
	 */
	public static double[][] GetId() {
		int linenum = FileLine.GetLineNumber(Constant.idFile.replace(".", "_" + Constant.TimeForTest + "."));
		double[][] id = new double[linenum - 1][4];// 0-4�ֱ��ʾ��㣬�յ㣬���ۣ���ʱ
		try {
			Scanner in = new Scanner(new File(Constant.idFile.replace(".", "_" + Constant.TimeForTest + ".")));
			in.nextLine();// �ȳ�ȥ��һ�б�ע
			while (in.hasNextLine()) {
				String[] parts = in.nextLine().split("\t");
				int currentId = Integer.parseInt(parts[0]);
				id[currentId][0] = Integer.parseInt(parts[1]);// ���
				id[currentId][1] = Integer.parseInt(parts[2]);// �յ�
				id[currentId][2] = Integer.parseInt(parts[3]);// ����
				id[currentId][3] = Integer.parseInt(parts[4]);// ��ʱ
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 
	 * Function:
	 *		��ȡ��Ȩ�ؾ���
	 * Details:
	 *		�ڸ�������·����ʱ��������·����ĵ����о��Ǳ��ϵ�Ȩ��
	 *		��˿��Խ������ڵ��Ӧ��·�ϵĵ�����ֵ�����������Ȩ��
	 * Remark: 2018��9��19�� ����12:49:49
	 */
	public static double[][] GetEdge(double[][] Id) {
		int nodeNum = Constant.numNodes;
		int[][] idlink = GetIdLink(Id);
		double[][] edge = new double[nodeNum][nodeNum];
		for (int i = 0; i < nodeNum; i++)
			Arrays.fill(edge[i], Constant.MAX_VALUE);
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					edge[i][j] = 0;
				} else {
					edge[i][j] = Id[idlink[i][j]][2];//���Ĳ���
				}
			}
		}
		return edge;
	}

	/**
	 * 
	 * Function:
	 *		��ȡ�����Ӿ���
	 * Details:
	 *		�����Ӿ�����ָͨ��������Ψһ��λ��һ����·�ϡ�
	 *		���������֮��û����·����ô�����Ӿ����ֵΪ-1��ͨ�������жϡ�
	 * Remark: 2018��9��19�� ����12:51:00
	 */
	public static int[][] GetIdLink(double[][] Id) {
		int nodeNum = Constant.numNodes;
		int[][] IdLink = new int[nodeNum][nodeNum];
		for (int i = 0; i < IdLink.length; i++)
			Arrays.fill(IdLink[i], -1);
		for (int i = 0; i < Id.length; i++)
			IdLink[(int) Id[i][0]][(int) Id[i][1]] = i;// Id�ţ����Ĳ���
		return IdLink;
	}
}
