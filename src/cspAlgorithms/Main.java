package cspAlgorithms;

import java.util.Arrays;
import java.util.List;

import fileInput.IdFile;
import randomTopology.Constant;
import randomTopology.Topology;

public class Main {
	private AbstractCSPMethods abstractCSPMethods;
	private int callTime;
	private int start = 2;
	private int end = 8;
	
	public Main() {
		//Created constructor stubs
		this(false);
	}
	
	public Main(boolean enhanced) {
		if (enhanced) {
			abstractCSPMethods = new SearchTriangle(new BiLAD());
		} else {
			abstractCSPMethods = new BiLAD();
		}
	}
	
	public double[] compute(String filename) {
		// 指定文件
		double[][] Id = IdFile.GetId(false);
		double maxIndex = -1;
		for (double[] d : Id) {
			if (Math.max(d[0], d[1]) > maxIndex) {
				maxIndex = Math.max(d[0], d[1]);
			}
		}
		int[] Node = new int[(int) Math.round(maxIndex + 1)];
		for (int s = 0; s < Node.length; s++) {
			Node[s] = s;
		}
		int[][] IdLink = IdFile.GetIdLink(Id);
		double minDelay = abstractCSPMethods.GetMinDelay(Node, Id, IdLink, start, end);
		int delayConstraint = (int)(minDelay + Math.random() * 20 + 1);
		List<Integer> paths = abstractCSPMethods.OptimalPath(Node, Id, IdLink, delayConstraint, start, end);
		double[] result = new double[2];
		result[0] = abstractCSPMethods.Ctheta(paths, Id, IdLink);
		result[1] = abstractCSPMethods.Ptheta(paths, Id, IdLink);
		callTime = abstractCSPMethods.getCallDijkstraTime();
		return result;
	}
	
	public double[] compute(Integer nodeNum) {
		// 自动生成
		Constant.step = (Constant.numNodes = nodeNum);
		new Topology().ProduceTopology();
		String filename = Constant.idFile.replace(".", "_" + Constant.WriteFile_TimeFor + ".");
		return compute(filename);
	}
	
	public int getCallTime() {
		return callTime;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		String filename = Constant.idFile.replace(".", "_" + 1 + ".");
		main.setStart(0);
		main.setEnd(3);
		System.out.println(Arrays.toString(main.compute(filename)));
	}
}
