package randomTopology;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * OverView: 
 * 		���ɺ��Ľڵ� �����ṩ������ɺ��Ľڵ�ķ���
 */
public class CoreNode {
	/**
	 * 
	 * Function:
	 *		���ո����Ľڵ����������ļ��еĺ��Ľڵ���ʣ������ɺ��Ľڵ�
	 * Details:
	 *		ͬ��
	 * Remark: 2018��9��19�� ����12:59:10
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
