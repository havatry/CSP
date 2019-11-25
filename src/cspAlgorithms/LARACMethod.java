package cspAlgorithms;

import java.util.ArrayList;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		������ΪLARACMethdWithMD��LARACMethodWithStoring��SearchTriangle��Ļ��ࡣ
 * 		��LARACMethod����һЩ�ж���������������ʵ�֡� �ṩ�˸�LARACMethodWithStoring��ĸ������½������·���ķ���
 */
public class LARACMethod extends AbstractCSPMethods {
	protected boolean improvement2 = false;// �Ƿ����MD����:CthetaTop>(1+MD)*CthetaBelow
	// �Ƿ����safeGuard����:Math.abs(theta2Current-(thetaTop+thetaBelow)/2)>=(0.5-gama)*(thetaTop-thetaBelow)
	protected boolean safeGuard = false;
	protected double gama = Constant.gama;// safeGuard�����Ĳ�����һ��ȡ0.05�ϼ�
	protected double MD = Constant.MD;// MD�����е�mdֵ
	// ���������̵Ĳ�����thetaֵ����Ҫ����LaracMethodWithStoring����
	protected List<Double> thetaList = new ArrayList<Double>();

	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		// ����ĺ���ο������е�BiLAD����
		double thetaBelow = 0;
		thetaList.add(thetaBelow);
		List<Integer> pathBelow = getPath(Node, Id, thetaBelow, start, end);// c-minimal
																			// path
		double CthetaBelow = Ctheta(pathBelow, Id, IdLink);
		double PthetaBelow = Ptheta(pathBelow, Id, IdLink);
		if (PthetaBelow <= delayConstraint) {
			theta = thetaBelow;
			return pathBelow;// if d(pc)<=D then return Pc
		}
		double thetaTop = (double) Math.PI / 2;
		thetaList.add(thetaTop);
		List<Integer> pathTop = getPath(Node, Id, thetaTop, start, end);// d-minimal
																		// path
		double CthetaTop = Ctheta(pathTop, Id, IdLink);
		double PthetaTop = Ptheta(pathTop, Id, IdLink);
		if (PthetaTop > delayConstraint)
			return null;// there is no solution

		while ((!improvement2) || (CthetaTop > (1 + MD) * CthetaBelow)) {// �������MD������������������Ч������ͺ���MD��������ѭ��
			if (CthetaTop == CthetaBelow) {
				theta = 0.0;
				return pathTop; // special
			}
			double theta2Current = Math.abs(Math.atan(Math.abs((CthetaTop - CthetaBelow) / (PthetaBelow - PthetaTop))));// ��֤atan����Ϊ����,������Ҳ������
			if (safeGuard
					&& Math.abs(theta2Current - (thetaTop + thetaBelow) / 2) >= (0.5 - gama) * (thetaTop - thetaBelow))
				theta2Current = (thetaTop + thetaBelow) / 2;
			thetaList.add(theta2Current);// ���ϲ�����theta
			List<Integer> path2Current = getPath(Node, Id, theta2Current, start, end);
			double Ctheta2Current = Ctheta(path2Current, Id, IdLink);
			double Ptheta2Current = Ptheta(path2Current, Id, IdLink);
			// �������б�����»����ҵ�֮ǰ��������֮һ����ô˵��ֱ�������Ѿ�û�е��ˣ���ʱ�㷨����ͣ���ˡ�
			if (((Math.abs(Ctheta2Current - CthetaBelow) < esp) && (Math.abs(Ptheta2Current - PthetaBelow) < esp))
					|| ((Math.abs(Ctheta2Current - CthetaTop) < esp) && (Math.abs(Ptheta2Current - PthetaTop) < esp))) {
				// �ҵ��ĵ㻹����ԭ�������ϣ�˵������û�з������������ŵ�
				theta = theta2Current;// Ҫ��Ҫ�ӵ��б���,����� 2018 08 24
				// ����������ء�ʵ���ϲ������ȷ�ġ���Ϊ�����������������кܶ�㣬��pathTop������Լ�������⡣
				// ����������ϻ�������������ʱ��ֵ�ĵ㣬��ôӦ�÷��ؾ�����ʱ��ֵ����������ĵ㡣Ȼ�����ﲢû�н���
				// ��һ���Ĵ�����Ϊ����������������ĸ��ʽϵͣ�����LARACMethodҲ��ȷ������������ѵ�·������Ϊ��
				// ������֤����LARACMethod����Gap���������ֻ�Ǽ򵥷�������Լ����pathTop��
				return Ptheta2Current > (delayConstraint + esp) ? pathTop : path2Current;// ������������е�Ķ�
			} else if ((Ptheta2Current - delayConstraint) <= esp) {// d(r)<=D
				// ���ҵ��ĵ�������ʱ��ֵ��������Ͻ磨ע������ĸ����Ͻ�ͱ�����Dijkstra��������еĸ����Ͻ粻��һ������)
				// �����Ǹ���ԭ�ȵ��Ͻ��λ��ǰ��
				CthetaTop = Ctheta2Current;
				PthetaTop = Ptheta2Current;
				pathTop = path2Current;
				thetaTop = theta2Current;// safe add
			} else {// d(r)>D
					// ���ҵ��ĵ㲻��������ʱ��ֵ��������½�
					// �����Ǹ���ԭ�ȵ��½��λ��ǰ��
				CthetaBelow = Ctheta2Current;
				PthetaBelow = Ptheta2Current;
				pathBelow = path2Current;
				thetaBelow = theta2Current;// safe add
			}
		}
		// ������MD�����˳���ʱ�򣬷�������Լ����pathTop
		if (improvement2)
			return pathTop;
		else
			return null;// �ⲽ��������������Ƽӵ�
	}

	/**
	 * 
	 * Function: 
	 * 		�÷���ʵ���Ϻ������OptimalPath������һ���ġ�
	 * Details: 
	 * 		����ָ�����Ͻ�theta���½�theta��
	 * 		�������OptimalPath�������Ͻ�thetaΪPI/2,�½�thetaΪ0�� ����ο�����OptimalPath������
	 * Remark: 2018��9��19�� ����11:34:24
	 */
	protected List<Integer> ActualPath(int[] Node, double[][] Id, int[][] IdLink, double theta1, double thet2,
			double delayConstraint, int start, int end) {
		// ��ʼ��
		double thetaTop = thet2;
		double thetaBelow = theta1;
		// ����
		List<Integer> pathBelow = getPath(Node, Id, thetaBelow, start, end);// c-minimal
																			// path

		double CthetaBelow = Ctheta(pathBelow, Id, IdLink);
		double PthetaBelow = Ptheta(pathBelow, Id, IdLink);

		if (PthetaBelow <= delayConstraint)
			return pathBelow;// if d(pc)<=D then return Pc

		List<Integer> pathTop = getPath(Node, Id, thetaTop, start, end);// d-minimal
																		// path
		double CthetaTop = Ctheta(pathTop, Id, IdLink);
		double PthetaTop = Ptheta(pathTop, Id, IdLink);

		if (PthetaTop > delayConstraint)
			return null;// there is no solution

		while (true) {
			double theta2Current = Math.abs(Math.atan(Math.abs((CthetaTop - CthetaBelow) / (PthetaBelow - PthetaTop))));// ��֤atan����Ϊ����,������Ҳ������
			List<Integer> path2Current = getPath(Node, Id, theta2Current, start, end);
			double Ctheta2Current = Ctheta(path2Current, Id, IdLink);
			double Ptheta2Current = Ptheta(path2Current, Id, IdLink);
			if (((Math.abs(Ctheta2Current - CthetaBelow) < esp) && (Math.abs(Ptheta2Current - PthetaBelow) < esp))
					|| ((Math.abs(Ctheta2Current - CthetaTop) < esp) && (Math.abs(Ptheta2Current - PthetaTop) < esp))) {
				// �ҵ��ĵ㻹����ԭ�������ϣ�˵������û�з������������ŵ�
				return PthetaBelow > (delayConstraint + esp) ? pathTop : pathBelow;
			} else if ((Ptheta2Current - delayConstraint) <= esp) {// d(r)<=D
				// thetaTop=theta2Current;
				CthetaTop = Ctheta2Current;
				PthetaTop = Ptheta2Current;
				pathTop = path2Current;
			} else {// d(r)>D
				CthetaBelow = Ctheta2Current;
				PthetaBelow = Ptheta2Current;
				pathBelow = path2Current;
			}
		}
	}
}
