package randomTopology;

/**
 * 
 * OverView:
 *		通过本包中XMLHelp类来获取setting.xml配置文件中所有常量
 *		具体含义参靠setting.xml中说明
 */
public class Constant {
	// 手动更新的变量
	public static int WriteFile_TimeFor = 1;
	public static int TimeForTest = 1;
	public static boolean specFile = false; // 是否是指定文件导入
	public static String specIdFile; // 指定的文件名
	public static int start = 2; // 指定起点
	public static int end = 16; // 指定终点
	public static int specDelay; // 指定时延
	public static String excelFile;
	public static boolean clearBeforeProduce = true;

	// 程序更新的变量
	public static int numNodes = Integer.parseInt(XMLHelper.getValue("//allInfo/node/number"));
	public static double gama = Double.parseDouble(XMLHelper.getValue("//allInfo/test/gama"));
	public static double MD = Double.parseDouble(XMLHelper.getValue("//allInfo/test/md"));

	// 输入变量
	public static int group = Integer.parseInt(XMLHelper.getValue("//allInfo/input/group"));
	public static int copy = Integer.parseInt(XMLHelper.getValue("//allInfo/input/copy"));
	public static int step = Integer.parseInt(XMLHelper.getValue("//allInfo/input/step"));
	
	// 常量
	public static final int W = Integer.parseInt(XMLHelper.getValue("//allInfo/node/area"));
	public static final double coreProbility = Double.parseDouble(XMLHelper.getValue("//allInfo/node/coreProbility"));
	public static final double coreToCoreProbility = Double
			.parseDouble(XMLHelper.getValue("//allInfo/node/coreToCoreProbility"));
	public static final double coreToNormalProbility = Double
			.parseDouble(XMLHelper.getValue("//allInfo/node/coreToNormalProbility"));
	public static final String basePath = XMLHelper.getValue("//allInfo/file/@basePath");
	public static final String idFile = basePath + XMLHelper.getValue("//allInfo/file/idFile");
	public static final String edgeFile = basePath + XMLHelper.getValue("allInfo/file/edgeFile");
	public static final String nodeFile = basePath + XMLHelper.getValue("allInfo/file/nodeFile");
	public static final String coreNodeFile = basePath + XMLHelper.getValue("//allInfo/file/coreNodeFile");
	public static final String CharacteristicFile=basePath+XMLHelper.getValue("//allInfo/file/characteristicFile");
	public static final int MAX_VALUE = Integer
			.parseInt(XMLHelper.getValue("//allInfo/program/notExistsEdgeForValueOfYen"));
	public static final double esp = Double.parseDouble(XMLHelper.getValue("//allInfo/input/esp"));// LARAC和二分法精度
	public static final int MIN = Integer.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForValue"));
	public static final double ExistsPathForValue = Double
			.parseDouble(XMLHelper.getValue("//allInfo/program/ExistsPathForValue"));
	public static final int notExistsPathForYenKValue = Integer
			.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForYenKValue"));
	public static final int notExistsPathForValue = Integer
			.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForValue"));
}
