package cspAlgorithms;

import java.util.List;

/**
 * 
 * OverView: 
 * 		������ҪLARACMethod������������MD����
 */
public class LARACMethodWithMD extends LARACMethod {
	@Override
	public List<Integer> OptimalPath(int[] Node, double[][] Id, int[][] IdLink, int delayConstraint, int start,
			int end) {
		// TODO Auto-generated method stub
		super.improvement2 = true;// ָ��ʹ��MD����
		return super.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
	}
}
