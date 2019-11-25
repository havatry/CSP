package cspAlgorithms;

import java.util.ArrayList;
import java.util.List;

import randomTopology.Constant;

/**
 * 
 * OverView: 
 * 		该类作为LARACMethdWithMD、LARACMethodWithStoring、SearchTriangle类的基类。
 * 		在LARACMethod加了一些判断条件，供子类来实现。 提供了给LARACMethodWithStoring类的给定上下界求最佳路径的方法
 */
public class LARACMethod extends AbstractCSPMethods {
	protected boolean improvement2 = false;// 是否加上MD条件:CthetaTop>(1+MD)*CthetaBelow
	// 是否加上safeGuard条件:Math.abs(theta2Current-(thetaTop+thetaBelow)/2)>=(0.5-gama)*(thetaTop-thetaBelow)
	protected boolean safeGuard = false;
	protected double gama = Constant.gama;// safeGuard条件的参数，一般取0.05较佳
	protected double MD = Constant.MD;// MD条件中的md值
	// 保存求解过程的产生的theta值，主要用于LaracMethodWithStoring方法
	protected List<Double> thetaList = new ArrayList<Double>();

	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		// 下面的含义参考本包中的BiLAD方法
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

		while ((!improvement2) || (CthetaTop > (1 + MD) * CthetaBelow)) {// 如果加上MD条件，则后面的条件生效。否则就忽略MD条件进行循环
			if (CthetaTop == CthetaBelow) {
				theta = 0.0;
				return pathTop; // special
			}
			double theta2Current = Math.abs(Math.atan(Math.abs((CthetaTop - CthetaBelow) / (PthetaBelow - PthetaTop))));// 保证atan里面为正数,理论上也是正的
			if (safeGuard
					&& Math.abs(theta2Current - (thetaTop + thetaBelow) / 2) >= (0.5 - gama) * (thetaTop - thetaBelow))
				theta2Current = (thetaTop + thetaBelow) / 2;
			thetaList.add(theta2Current);// 加上产生的theta
			List<Integer> path2Current = getPath(Node, Id, theta2Current, start, end);
			double Ctheta2Current = Ctheta(path2Current, Id, IdLink);
			double Ptheta2Current = Ptheta(path2Current, Id, IdLink);
			// 如果沿着斜线往下还是找到之前的两个点之一，那么说明直线往下已经没有点了，这时算法可以停机了。
			if (((Math.abs(Ctheta2Current - CthetaBelow) < esp) && (Math.abs(Ptheta2Current - PthetaBelow) < esp))
					|| ((Math.abs(Ctheta2Current - CthetaTop) < esp) && (Math.abs(Ptheta2Current - PthetaTop) < esp))) {
				// 找到的点还是在原来的线上，说明下面没有符合条件的最优点
				theta = theta2Current;// 要不要加到列表中,待检查 2018 08 24
				// 下面这个返回。实际上并不是最精确的。因为可能是在这条线上有很多点，而pathTop是满足约束的最差解。
				// 如果这条线上还有其他满足延时阈值的点，那么应该返回距离延时阈值那条线最近的点。然后这里并没有进行
				// 进一步的处理，因为本身这种情况发生的概率较低，而且LARACMethod也的确不是用来求最佳的路径。因为在
				// 理论上证明，LARACMethod存在Gap。因此这里只是简单返回满足约束的pathTop。
				return Ptheta2Current > (delayConstraint + esp) ? pathTop : path2Current;// 相对论文这里有点改动
			} else if ((Ptheta2Current - delayConstraint) <= esp) {// d(r)<=D
				// 当找到的点满足延时阈值，则更新上界（注意这里的更新上界和本包中Dijkstra类的搜索中的更新上界不是一个概念)
				// 这里是更新原先的上界点位当前点
				CthetaTop = Ctheta2Current;
				PthetaTop = Ptheta2Current;
				pathTop = path2Current;
				thetaTop = theta2Current;// safe add
			} else {// d(r)>D
					// 当找到的点不满满足延时阈值，则更新下界
					// 这里是更新原先的下界点位当前点
				CthetaBelow = Ctheta2Current;
				PthetaBelow = Ptheta2Current;
				pathBelow = path2Current;
				thetaBelow = theta2Current;// safe add
			}
		}
		// 当进行MD条件退出的时候，返回满足约束的pathTop
		if (improvement2)
			return pathTop;
		else
			return null;// 这步是满足编译器机制加的
	}

	/**
	 * 
	 * Function: 
	 * 		该方法实际上和上面的OptimalPath方法是一样的。
	 * Details: 
	 * 		这里指定了上界theta和下界theta。
	 * 		而上面的OptimalPath方法的上界theta为PI/2,下界theta为0。 具体参考上面OptimalPath方法。
	 * Remark: 2018年9月19日 上午11:34:24
	 */
	protected List<Integer> ActualPath(int[] Node, double[][] Id, int[][] IdLink, double theta1, double thet2,
			double delayConstraint, int start, int end) {
		// 初始化
		double thetaTop = thet2;
		double thetaBelow = theta1;
		// 计算
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
			double theta2Current = Math.abs(Math.atan(Math.abs((CthetaTop - CthetaBelow) / (PthetaBelow - PthetaTop))));// 保证atan里面为正数,理论上也是正的
			List<Integer> path2Current = getPath(Node, Id, theta2Current, start, end);
			double Ctheta2Current = Ctheta(path2Current, Id, IdLink);
			double Ptheta2Current = Ptheta(path2Current, Id, IdLink);
			if (((Math.abs(Ctheta2Current - CthetaBelow) < esp) && (Math.abs(Ptheta2Current - PthetaBelow) < esp))
					|| ((Math.abs(Ctheta2Current - CthetaTop) < esp) && (Math.abs(Ptheta2Current - PthetaTop) < esp))) {
				// 找到的点还是在原来的线上，说明下面没有符合条件的最优点
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
