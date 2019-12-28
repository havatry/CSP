package randomTopology;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 
 * OverView: 生成拓扑的入口
 */
public class GenerateRandomNetwork_MainProgram {
	/**
	 * 
	 * Function:
	 *		生成给定网络拓扑
	 * Details:
	 *		依据配置文件中的组数group和每组的个数copy
	 *		依次生成符合要求的拓扑，借助本包中的Topology类。
	 * Remark: 2018年9月19日 下午1:00:22
	 */
	public static void main(String[] args) {
		for (int i = 0; i < Constant.group; i++) {
			XMLHelper.setValue("//allInfo/node/number", Constant.step * (i + 1) + "");// 里面有更新变量
			for (int j = 0; j < Constant.copy; j++) {
				new Topology().ProduceTopology();
				Constant.WriteFile_TimeFor++;// 更新，重要
			}
		}
	}
	
	/**
	 * 
	 * Function:
	 *		将本程序的关键配置数据写入到文件中
	 * Details:
	 *		同上
	 * Remark: 2018年9月19日 下午2:22:46
	 */
	public static void writeCharacteristicToFile() {
		//写入核心节点概率、核心与核心连边概率、核心与边缘连边概率、边长大小、group、copy、step大小、写入日期
		PrintWriter out=null;
		try {
			out=new PrintWriter(Constant.CharacteristicFile);//一个目录下只有一个特征文件
			out.println("核心节点概率: "+Constant.coreProbility);
			out.println("核心与核心连边概率: "+Constant.coreToCoreProbility);
			out.println("核心与边缘连边概率: "+Constant.coreToNormalProbility);
			out.println("区域边长: "+Constant.W);
			out.println("所有组数: "+Constant.group);
			out.println("每组个数: "+Constant.copy);
			out.println("每组相差节点数: "+Constant.step);
			out.println("写入日期: "+new Date());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}finally {
			out.close();
		}
	}
}
