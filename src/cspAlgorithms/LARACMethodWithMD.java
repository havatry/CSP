package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		该类主要LARACMethod基础上增加了MD条件
 */
public class LARACMethodWithMD extends LARACMethod {
	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		super.improvement2 = true;// 指定使用MD条件
		return super.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
	}
}
