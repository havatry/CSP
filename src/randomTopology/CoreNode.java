package randomTopology;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * OverView: 
 * 		生成核心节点 该类提供随机生成核心节点的方法
 */
public class CoreNode {
	/**
	 * 
	 * Function:
	 *		按照给定的节点数和配置文件中的核心节点概率，来生成核心节点
	 * Details:
	 *		同上
	 * Remark: 2018年9月19日 下午12:59:10
	 */
	protected List<Integer> findCoreNodes(int numNodes) {
		List<Integer> coreNodeLists = new ArrayList<Integer>();
		for (int i = 0; i < numNodes; i++) {
			if (Math.random() <= Constant.coreProbility) {
				coreNodeLists.add(i);
			}
		}
		return coreNodeLists;
	}
}
