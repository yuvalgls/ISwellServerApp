import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JTextArea;
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
		if(FromTime0.equals(FromTime)){
			return false;
		}else{
			return true;
		}
    }
    
    public static void chackforwarnings(JTextArea textAreaEmailLog, JTextArea txtDataLog) throws IOException, ParserConfigurationException, SAXException, AddressException, MessagingException, ClassNotFoundException, SQLException{
		String Path;
		URL url;
		InetAddress IP = InetAddress.getLocalHost();
		String cd = datehandler.getDate();
		//alert update
		if(IP.getHostName().equals("Yuval-PC")){
			Path = "C:\\ISwell\\DownloadFolder\\isramar\\isr_alert\\";
		}else{
			Path = "/root/ISwell/DownloadFolder/isramar/isr_alert/";
		}
		url = new URL("http://www.ims.gov.il/ims/rss/alert_feed10.xml");
		filehandler.download(String.valueOf(url), Path + cd + ".xml");
//		File file = new File(Path + cd + ".xml");
//		long filesize = file.length();
//		System.out.println(filesize);
		xmlhandler.WarningXMLParser(Path + cd + ".xml", textAreaEmailLog, txtDataLog);
	}
    
	public static String validateimsurl(String imsupdate) throws IOException{
		String url = "http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "00.windir.gif";
//		System.out.println(url);
//		System.out.println(SubClass.getResponseCode(url));
		if(SubClass.getResponseCode(url) == 404){
			imsupdate = SubClass.validateimsurl(String.valueOf(Integer.valueOf(imsupdate)-1)); 
		}else{
			return imsupdate;
		}
		return imsupdate;
	}
	
	public static int getResponseCode(String urlString) throws IOException {
	    URL u = new URL(urlString); 
	    HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
	    huc.setRequestMethod("GET"); 
	    huc.connect(); 
	    return huc.getResponseCode();
	}
	
	public static void checkLongRangForcast(JTextArea txtDataLog, JTextArea txtEmailLog) throws IOException, AddressException, MessagingException, ClassNotFoundException, SQLException{
		 //get date from msw
        String urlstring = "http://magicseaweed.com/Hof-Maravi-Surf-Report/3663/";
        String source = "";
        
        InetAddress IP = InetAddress.getLocalHost();
		String Path = null;
//		System.out.println(IP.getHostName());
//		System.out.println(IP.getHostName().equals("Yuval-PC"));
			//sea updates
		if(IP.getHostName().equals("Yuval-PC")){
			Path = "C:\\ISwell\\DownloadFolder\\msw\\";
		}else{
			Path = "/root/ISwell/DownloadFolder/msw/";
		}
		String cd = datehandler.getDate();
		File mswforcast = new File(Path + cd +".html");
		FileWriter writer = new FileWriter(mswforcast, true);  
		PrintWriter out = new PrintWriter(mswforcast);
        //read url and print it out
        URL oracle = new URL(urlstring);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            source = source + inputLine.replaceAll("\\s", "");
            out.append(inputLine + "\n");
        }
        in.close();
        writer.close();
        out.close();
//        System.out.println(source);
        
        //find elements
        String[] days = new String[100];
        String[] wave = new String [100];
        int a =0;
        int surflocation = 0;
        int daylocation = 0;
        String surfstring = "msw-fc-fps msw-fc-ps text-center\"><spanclass=\"h4nomargin\">".replaceAll("\\s", "");
        String daystring = "msw-fc-day h6 nomargin pull-left".replaceAll("\\s", "");
        do{
//            System.out.println("");
            daylocation = source.lastIndexOf(daystring, surflocation);
            surflocation = source.indexOf(surfstring , surflocation+1);
            if(surflocation<0){
                break;
            }
//            System.out.println("daylocation : " + daylocation + " surflocation " + surflocation);
            if(daylocation>0){
                days[a] = "XXXXXXXXXX";
                daylocation = source.indexOf("=\"",daylocation);
                days[a] = source.substring(source.indexOf("=\"",daylocation)+2 ,source.indexOf("\">",daylocation));
//                System.out.println(source.substring(source.indexOf("=\"",daylocation)+2 ,source.indexOf("\">",daylocation)));
//                System.out.println("a = " + a + " days[a] = " + days[a]);
            }
            if(surflocation >0){
                wave[a] = "XXXXXXXXXX";
                wave[a] = source.substring(source.indexOf("n\">",surflocation)+3,source.indexOf("<smallclass=",surflocation));
//                System.out.println("a = " + a + " wave[a] = " + wave[a]);
//                System.out.println(source.substring(source.indexOf("n\">",surflocation)+3,source.indexOf("<smallclass=",surflocation)));
            }
            a++;
        }while(surflocation > 0);
        dbhandler.writemswtodb(wave, days, cd, txtDataLog);
        dbhandler.SendLongRange(wave, days, cd, txtEmailLog);
	}
	public static boolean IsProgrammer() throws UnknownHostException{
		InetAddress IP = InetAddress.getLocalHost();
			//sea updates
		if(IP.getHostName().equals("Yuval-PC")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean shouldanalyzeweatherxml(String xMLFile) {
		// TODO Auto-generated method stub
		return false;
	}
}
