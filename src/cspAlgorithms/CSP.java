package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		�ýӿ�Ϊ����CSP�㷨�ṩ�淶�����к�����Ҫ������CSP�㷨ʵ��OptimalPath������
 * 		���⻹�ж���LARACMethodWithStoring��MutliOptimalPaths,
 * 		֮���Խ��˷�������Ϊdefault��ʵ�֣�ʵ��������ȥ���ڳ����������ʵ�֡���Ϊ�ܶ�ʱ��÷������ᱻ���ã�
 * 		��ֻ��LARACMethodWithStoring�ᱻ��д��
 * 		Ȼ������·���ϵĴ��ۡ���·���ϵ���ʱ����ȡ��ѵ�theta����ȡ����Dijkstra�������ĸ�������
 */
public interface CSP {
	/**
	 * 
	 * Function:
	 * 		 �÷�����ÿ���㷨�ĺ��ġ���ɸ������������ˣ�һ����Node,Id,IDLink�����������),
	 * 		�Լ���������ʱ��ֵԼ���������,���ش�start�ڵ㵽end�ڵ�����·��
	 * Details:
	 * 		 ����ʵ����ÿ���㷨����ϸע����
	 * Remark: 2018��9��18�� ����10:11:54
	 */
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end);

	/**
	 * 
	 * Function: 
	 * 		�÷���Ĭ�Ϸ���null,���ҽ���ΪLARACMethodWithStoring��ʱ�����д�÷����� 
	 * Details:
	 * 		����ʵ���ڶ�Ӧ�㷨ע����
	 * Remark: 2018��9��18�� ����10:13:24
	 */
	default public List<List<Integer>> MutliOptimalPaths(int[] Node, double[][] Edge, double[][] Id, int[][] IdLink,
			int delayConstraint, int start, int end[]) {
		return null;
	}

	/**
	 * 
	 * Function:
	 * 		 ����һ��·���������·���ϵ��ܴ���
	 * Details: 
	 * 		����·���ϵ�ÿ����·������Щ��·�ϵĴ����ۼ���������󷵻��ۼƵĴ���
	 * Remark: 2018��9��18�� ����10:14:16
	 */
	public double Ctheta(List<Integer> path, double[][] Id, int[][] IdLink);

	/**
	 * 
	 * Function: 
	 * 		����һ��·���������·���ϵ�����ʱ 
	 * Details: 
	 * 		����·���ϵ�ÿ����·������Щ��·�ϵ���ʱ�ۼ���������󷵻��ۼƵ���ʱ
	 * Remark: 2018��9��18�� ����10:15:11
	 */
	public double Ptheta(List<Integer> path, double[][] Id, int[][] IdLink);

	/**
	 * 
	 * Function: 
	 * 		�����㷨������ɺ�����theta 
	 * Details:
	 * 		��ʼ��ʱ��thetaΪ-1����ʱ�㷨������������theta����Ϊ-1��˵���㷨���г���
	 * 		����theta�Ǳ�ʾ�㷨�������������б�ʶ�Ӧ�ĽǶȡ�
	 * 		��thetaһ���������Ϊ�ο�����һ���滹���Խ�������㷨(�����е�SearchTriangle��)��
	 * 		���������ڵĵ�����������Ӷ��ҵ����Ž⡣ExactBiLAD���Ƕ�BiLAD���е�������
	 * Remark: 2018��9��18�� ����10:16:12
	 */
	public double getTheta();

	/**
	 * 
	 * Function: 
	 * 		�����㷨������ɺ�ĵ���Dijkstra������ 
	 * Details:
	 * 		һ�����,ÿ����һ��dijkstra�㷨�ͻ�õ�һ���µ�·��������Щ��·����
	 * 		���·�����м���̡���Ҳ��Щdijkstra�㷨�ļ���ֻ��Ϊ����Щ����������
	 * 		����Ҫ������Yen�㷨�С����漰��Yen�㷨���ڲ�ԭ��������ο�ά���ٿ�Yen�㷨���ܡ�
	 * Remark: 2018��9��18�� ����10:18:18
	 */
	public int getCallDijkstraTime();
}
