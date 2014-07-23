import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class xmlhandler {
	
	/*public static void DownLoadFile(String FileName, String Path, URL url){
		System.out.println("Downloading file " + FileName);
		String cd = datehandler.getDate();
		try {
	        File XMLFile = new File(Path + "\\" + FileName);
	        FileUtils.copyURLToFile(url, XMLFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		String  XMLFile = (Path + "\\" + cd + ".xml");
		xmlhandler.XMLParser(XMLFile);
	}*/	
	
	public static void XMLParser (String XmlFile){
		System.out.println("Starting parsering XML file " + XmlFile);
		File fXmlFile = new File(XmlFile);
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
				System.out.println(aList.item(a));
				//System.out.println("==================================");
				String LocationName = element.getElementsByTagName("LocationNameEng").item(0).getTextContent();
				String FromTime0 = element.getElementsByTagName("DateTimeFrom").item(0).getTextContent();
				String ToTime0 = element.getElementsByTagName("DateTimeTo").item(0).getTextContent();
				//check if its morning or night xml
				String SeaHeight0 = null, SeaTemp = null , WindDir0 = null , SeaHeight1 = null , WindDir1 = null;
				switch (FromTime0.toLowerCase().substring(FromTime0.length()-5)){
					case "08:00":{
						SeaHeight0 = element.getElementsByTagName("ElementValue").item(0).getTextContent();
						SeaTemp = element.getElementsByTagName("ElementValue").item(1).getTextContent();
						WindDir0 = element.getElementsByTagName("ElementValue").item(2).getTextContent();
						SeaHeight1 = element.getElementsByTagName("ElementValue").item(3).getTextContent();
						WindDir1 = element.getElementsByTagName("ElementValue").item(4).getTextContent();
						break;}
					case "20:00":{
						SeaHeight0 = element.getElementsByTagName("ElementValue").item(0).getTextContent();
						WindDir0 = element.getElementsByTagName("ElementValue").item(1).getTextContent();
						SeaHeight1 = element.getElementsByTagName("ElementValue").item(2).getTextContent();
						SeaTemp = element.getElementsByTagName("ElementValue").item(3).getTextContent();
						WindDir1 = element.getElementsByTagName("ElementValue").item(4).getTextContent();
					break;}
					default:
						System.out.println("ERROR XMLParser");
				}
				String FromTime1 = element.getElementsByTagName("DateTimeFrom").item(1).getTextContent();
				String ToTime1 = element.getElementsByTagName("DateTimeTo").item(1).getTextContent();
				//System.out.println(" LocationName " + LocationName + " FromTime0 " + FromTime0 + " ToTime0 " + ToTime0 +
				//		" SeaHeight0 " + SeaHeight0 + " SeaTemp " + SeaTemp + " WindDir0 " + WindDir0 + " SeaHeight1 " + 
				//		SeaHeight1 + " WindDir1 " + WindDir1 + " FromTime1 " + FromTime1 + " ToTime1 " + ToTime1 );
					
				dbhandler.WriteToDB(LocationName,FromTime0,ToTime0,SeaHeight0,SeaTemp,WindDir0,FromTime1,ToTime1,SeaHeight1,WindDir1,XmlFile);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
