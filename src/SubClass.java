import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SubClass {
    public static boolean shouldanalyzexml(String XMLFile) throws ClassNotFoundException, SQLException, SAXException, IOException{
    	String FromTime = dbhandler.Simplesql("iswell", "xmlparser", "FromTime0");
    	String FromTime0 = null;
    	File fXmlFile = new File(XMLFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList aList = doc.getElementsByTagName("Location");
			Element element;
			for(int a = 0; a < aList.getLength(); a++){
				element = (Element) aList.item(a);
				//System.out.println(aList.item(a));
				FromTime0 = element.getElementsByTagName("DateTimeFrom").item(0).getTextContent();
			}
		}catch (ParserConfigurationException e) {
			e.printStackTrace();
			}
		if(Integer.valueOf(FromTime0.substring(11,12)) == Integer.valueOf(FromTime.substring(11,12))){
			return false;
		}else{
			return true;
		}
		
    }
}
