package randomTopology;

/**
 * 
 * OverView:
 *		ͨ��������XMLHelp������ȡsetting.xml�����ļ������г���
 *		���庬��ο�setting.xml��˵��
 */
public class Constant {
	// �ֶ����µı���
	public static int WriteFile_TimeFor = 1;
	public static int TimeForTest = 1;
	public static boolean specFile = false; // �Ƿ���ָ���ļ�����
	public static String specIdFile; // ָ�����ļ���
	public static int start = 2; // ָ�����
	public static int end = 16; // ָ���յ�
	public static int specDelay; // ָ��ʱ��
	public static String excelFile;
	public static boolean clearBeforeProduce = true;

	// ������µı���
	public static int numNodes = Integer.parseInt(XMLHelper.getValue("//allInfo/node/number"));
	public static double gama = Double.parseDouble(XMLHelper.getValue("//allInfo/test/gama"));
	public static double MD = Double.parseDouble(XMLHelper.getValue("//allInfo/test/md"));

	// �������
	public static int group = Integer.parseInt(XMLHelper.getValue("//allInfo/input/group"));
	public static int copy = Integer.parseInt(XMLHelper.getValue("//allInfo/input/copy"));
	public static int step = Integer.parseInt(XMLHelper.getValue("//allInfo/input/step"));
	
	// ����
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
	public static final double esp = Double.parseDouble(XMLHelper.getValue("//allInfo/input/esp"));// LARAC�Ͷ��ַ�����
	public static final int MIN = Integer.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForValue"));
	public static final double ExistsPathForValue = Double
			.parseDouble(XMLHelper.getValue("//allInfo/program/ExistsPathForValue"));
	public static final int notExistsPathForYenKValue = Integer
			.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForYenKValue"));
	public static final int notExistsPathForValue = Integer
			.parseInt(XMLHelper.getValue("//allInfo/program/notExistsPathForValue"));
}
