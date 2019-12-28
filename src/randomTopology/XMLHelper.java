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
 * 		通过DOM4J来实现对配置文件setting.xml的获取值或者修改值。
 * 		 具体DOM4J方法参考网上教程。
 * 		提供获取xml值、修改xml值的方法。
 */
public class XMLHelper {
	private final static String XMLFILE = "resource/set/setting.xml";// 设置文件

	/**
	 * 
	 * Function: 
	 * 		获取xml的值 
	 * Details: 
	 * 		使用dom4j和xpath技术来获取xml文件中的特定节点的值 
	 * Remark: 2018年9月19日 下午1:31:43
	 */
	public static String getValue(String xpath) {
		SAXReader sax = new SAXReader();
		try {
			Document dom = sax.read(XMLFILE);
			Node node = dom.selectSingleNode(xpath);// 核心操作
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
	 * 		修改xml值 
	 * Details:
	 * 		 使用dom4j和xpath技术来修改xml文件中的特定节点的值 
	 * Remark: 2018年9月19日 下午1:32:16
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
			// 由于Contant类中都是静态变量，这些变量只初始化一次，即便改了文件，其也不是主动重新获取新的值
			// 因此这里需要手动更新某些Constant类中的变量
			Constant.numNodes = Integer.parseInt(getValue("//allInfo/node/number"));// 更新变量
			Constant.gama = Double.parseDouble(XMLHelper.getValue("//allInfo/test/gama"));
			Constant.MD = Double.parseDouble(XMLHelper.getValue("//allInfo/test/md"));
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
