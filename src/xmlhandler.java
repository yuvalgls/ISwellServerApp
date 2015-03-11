import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
	public static void WarningXMLParser(String XmlFile, JTextArea textAreaEmailLog, JTextArea txtDataLog) throws ParserConfigurationException, SAXException, IOException, AddressException, MessagingException, ClassNotFoundException, SQLException{
		String Source = XmlFile;
		File fXmlFile = new File(Source);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
	 
		String description = null;
		String msgdate = null;
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("channel");
		int del = 0;
//		System.out.println("starting warning del = " + del);
		for (int temp = 0; temp < nList.getLength(); temp++) { 
			Node nNode = nList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
//				System.out.println("title : " + eElement.getAttribute("title"));
//				System.out.println("link : " + eElement.getElementsByTagName("link").item(0).getTextContent());
//				System.out.println("description : " + eElement.getElementsByTagName("description").item(0).getTextContent());
//				System.out.println("language : " + eElement.getElementsByTagName("language").item(0).getTextContent());
//				System.out.println("lastBuildDate : " + eElement.getElementsByTagName("lastBuildDate").item(0).getTextContent());
//				System.out.println("ttl : " + eElement.getElementsByTagName("ttl").item(0).getTextContent());
				NodeList nList1 = eElement.getElementsByTagName("item");
				for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {
					Node nNode1 = nList1.item(temp1);
//					System.out.println("\nCurrent Element :" + nNode1.getNodeName());
					if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement1 = (Element) nNode1;
//						System.out.println("title : " + eElement1.getElementsByTagName("title").item(0).getTextContent());
//						System.out.println("link" + eElement1.getElementsByTagName("link").item(0).getTextContent());
//						System.out.println("description : " + eElement1.getElementsByTagName("description").item(0).getTextContent());
//						System.out.println("pubDate : " + eElement1.getElementsByTagName("pubDate").item(0).getTextContent());
						description = eElement1.getElementsByTagName("description").item(0).getTextContent();
						msgdate = eElement1.getElementsByTagName("pubDate").item(0).getTextContent();
						description = description.replaceAll("<[^>]*>", "");
						description = description.substring(0,description.indexOf("הנרשם לשירות"));
						msgdate = msgdate.replace("T", " ");
						msgdate = msgdate.replace("Z", " ");
						del = dbhandler.sendwarning(description, msgdate, textAreaEmailLog);
					}
				}
			}
		}
//		System.out.println("before if - del = " + del);
		if(del == 0 ){
			fXmlFile.delete();
		}else{
			System.out.println(XmlFile);
			txtDataLog.insert(datehandler.getDate() + " Warning : " + XmlFile.substring(XmlFile.lastIndexOf("\\")) + "\n", 0);
			txtDataLog.update(txtDataLog.getGraphics());
		}
	}
	public static void SeaXMLParser (String XmlFile){
		//System.out.println("Starting parsering XML file " + XmlFile);
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
//				System.out.println(aList.item(a));
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
//						System.out.println("ERROR XMLParser");
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
	public static void weatherXMLParser(String xMLFile, String Path, JTextArea txtDataLog) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(xMLFile);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		findsubnodes(nodeList, Path, txtDataLog);
		if(flg == 3){
			File file = new File(xMLFile);
			file.delete();
		}
	}
	
	static String Date= null, ElementNameHebrew = null,  ElementNameEnglish = null, ElementValueHebrew = null, ElementValueEnglish = null;
	static int flg = 0; //1 is for heb, 2 english, 3 break all
	static String IssueDate;
	public static void findsubnodes(NodeList nodeList, String Path, JTextArea txtDataLog) throws UnknownHostException {
		for (int itr = 0; itr < nodeList.getLength(); itr++) {
			Node node = nodeList.item(itr);
			if(node.getNodeName().equals("IssueDateTime")){
				IssueDate = node.getTextContent();
				IssueDate = IssueDate.replaceAll(":", "-");
				String[] files = filehandler.FindSubFiles(Path);
				for(int a = 0 ; a<files.length ; a++){
					if((files[a].substring(0,files[a].length()-4)).equals(IssueDate)){
						flg = 3;
						return;
					}
				}
				txtDataLog.insert("Weather update on : " + datehandler.getDate() + "\n", 0);
				txtDataLog.update(txtDataLog.getGraphics());
			}
			if(node.getNodeName().equals("Date")){
				Date = node.getTextContent();
			}
			if(node.getNodeName().equals("ElementValue") && node.getTextContent().indexOf("RSS") < 0){
				if(flg == 1){
					ElementValueHebrew = node.getTextContent();
				}else{
					ElementValueEnglish = node.getTextContent();
				}
			}
			if(node.getTextContent().equals("Weather in Hebrew")){
				flg = 1;
			}
			if(node.getTextContent().equals("Weather in English")){
				flg = 2;
			}
			if(Date != null && (ElementValueEnglish != null && ElementValueHebrew != null) && ElementValueEnglish.indexOf("RSS") < 0){
//				System.out.println(Date + " " + ElementValueHebrew);
				if(SubClass.IsProgrammer()){
					filehandler.SaveTXTFile(Path + IssueDate + ".txt", Date + " " + ElementValueHebrew);
				}else{
					filehandler.SaveTXTFile(Path + IssueDate + ".txt", Date + " " + ElementValueHebrew);
				}
				
				Date = null;
				ElementValueHebrew = null;
				ElementValueEnglish = null;
			}
			NodeList SubnodeList = node.getChildNodes();
			if (SubnodeList.getLength() > 1) {
				if(flg == 3){
					return;
				}
				findsubnodes(SubnodeList, Path, txtDataLog);
			}
		}
	}

}
