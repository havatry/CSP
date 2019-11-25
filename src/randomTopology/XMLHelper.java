package randomTopology;

import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * OverView: 
 * 		ͨ��DOM4J��ʵ�ֶ������ļ�setting.xml�Ļ�ȡֵ�����޸�ֵ��
 * 		 ����DOM4J�����ο����Ͻ̡̳�
 * 		�ṩ��ȡxmlֵ���޸�xmlֵ�ķ�����
 */
public class XMLHelper {
	private final static String XMLFILE = "resource/set/setting.xml";// �����ļ�

	/**
	 * 
	 * Function: 
	 * 		��ȡxml��ֵ 
	 * Details: 
	 * 		ʹ��dom4j��xpath��������ȡxml�ļ��е��ض��ڵ��ֵ 
	 * Remark: 2018��9��19�� ����1:31:43
	 */
	public static String getValue(String xpath) {
		SAXReader sax = new SAXReader();
		try {
			Document dom = sax.read(XMLFILE);
			Node node = dom.selectSingleNode(xpath);// ���Ĳ���
			return node.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Function: 
	 * 		�޸�xmlֵ 
	 * Details:
	 * 		 ʹ��dom4j��xpath�������޸�xml�ļ��е��ض��ڵ��ֵ 
	 * Remark: 2018��9��19�� ����1:32:16
	 */
	public static void setValue(String xpath, String text) {
		SAXReader sax = new SAXReader();
		try {
			Document dom = sax.read(XMLFILE);
			Node node = dom.selectSingleNode(xpath);
			node.setText(text);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileOutputStream(XMLFILE), format);
			writer.write(dom);
			writer.flush();
			writer.close();
			// ����Contant���ж��Ǿ�̬��������Щ����ֻ��ʼ��һ�Σ���������ļ�����Ҳ�����������»�ȡ�µ�ֵ
			// ���������Ҫ�ֶ�����ĳЩConstant���еı���
			Constant.numNodes = Integer.parseInt(getValue("//allInfo/node/number"));// ���±���
			Constant.gama = Double.parseDouble(XMLHelper.getValue("//allInfo/test/gama"));
			Constant.MD = Double.parseDouble(XMLHelper.getValue("//allInfo/test/md"));
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
