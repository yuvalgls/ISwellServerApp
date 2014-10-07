import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class emailhandler {
	
	public static void SendEmailAsHTML(String EMail, String location) throws Exception, Exception{
		String message = emailhandler.prpmsg(location);
		// SMTP server information
        String host = "smtp.gmail.com";
        String port = "587";
        //String mailFrom = "Iswell.israel@gmail.com";
        //String password = "gls27100";
 
        // outgoing message information
        String subject = "Surf Report ! ! !";
        
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Iswell.israel@gmail.com", "gls27100");
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress("Iswell.israel@gmail.com"));
        InternetAddress[] toAddresses = { new InternetAddress(EMail) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        //msg.setSentDate();
        // set plain text message
        msg.setContent(message, "text/html; charset=utf-8");
        // sends the e-mail
        Transport.send(msg);
        
	}
	public static String prpmsg(String location) throws Exception{
		String[] Data = dbhandler.ReadWaveDataFromDataBase(location);
//		for(int i=0 ; i<Data.length ; i++){
//			System.out.println("Data["+ i + "] = " +  Data[i]);
//		}
		String Starting = Data[3].substring(Data[3].length()-2) ;
		String Ending = Data[10].substring(Data[10].length()-2);
		String Status=null;
		int sum = Integer.valueOf(Starting) - Integer.valueOf(Ending);
		//System.out.println((Data[1].substring(Data[1].length()-5,Data[1].length()-3)));
		if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
			if(sum>0){
				 Status = "לאורך היום הים במגמת ירידה";
			}
			if(sum<0){
				 Status = "לאורך היום הים  במגמת עליה";
			}
			if(sum==0){
				 Status = "לאורך היום הים לא משתנה ";
			}
		}else{
			if(sum>0){
				 Status = "לאורך הלילה  הים  במגמת ירידה";
			}
			if(sum<0){
				 Status = "לאורך הלילה  הים  במגמת עליה";
			}
			if(sum==0){
				 Status = "לאורך הלילה  הים לא משתנה ";
			}
		}
		
			
				
		//System.out.println("prepering email for  " + Data[0]);
		//het heb translation for location
		String HeblocationName=null;
		switch (Data[0]){
			case "Southern Coast": HeblocationName = "החוף הדרומי";
								break;
			case "Northern Coast": HeblocationName = "החוף הצפוני";
								break;
			case "Sea of Galilee": HeblocationName = "הכנרת";
								break;
			case "Gulf of Elat": HeblocationName = "מפרץ אילת";
								break;
			default : HeblocationName = "לא ידוע";
		}
		
		Data[4] = dbhandler.GetWaveTranslate(Data[4]);
		String[] WindData = dbhandler.GetWindTranslate(Data[6]);
		String message = "<html><body>";
		message +="<p align=\"right\">: ! ! ! הדיוור היום באדיבות</p>";
		message +="<p align=\"center\"><img src=\"http://www.terem.co.il/images/trash.jpg\" style=”display:block” alt= \"Sponser\"  width=\"750\" height=\"300\"> </p>";
		message +="<p align=\"right\">  הירקון 15 תל אביב טלפון 035104232 </p>";
		message +="<p align=\"right\">  ____________________________________________ </p>";
		message +="<p align=\"right\">  התחזית מעודכנת מתאריך : " + Data[1] + "</p>";
		message +="<p align=\"right\">  עבור " + HeblocationName + "  הים יהיה  " + Data[4] + "  בגובה של " +  Data[3] + "</p>";
		String temp = "";
		if(WindData.length>0){
			for(int i=0 ; i <= WindData.length-1 ; i++){
				if(WindData[i] != null){
					temp = temp + " " + WindData[i];
				}
			}
		};
		
		message += "<p align=\"right\"> הרוח תהיה" + temp + " במהירות של " + Data[7]  + " קשר " + "</p>";
		message += "<p align=\"right\"> בטמפרטורה של " + Data[5] + " מעלות </p>";
		message += "<p align=\"right\">" + Status + "</p>";
		message += " ";
		if(Data[0].equals("Southern Coast")|| Data[0].equals("Northern Coast")){
			if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + (Integer.valueOf(datehandler.getDate().substring(2,8))-1) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "00.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_00.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + (Integer.valueOf(datehandler.getDate().substring(2,8))-1) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "06.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_06.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + (Integer.valueOf(datehandler.getDate().substring(2,8))-1) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "12.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_12.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + (Integer.valueOf(datehandler.getDate().substring(2,8))-1) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "18.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_18.gif\"> </p>";
			}else{
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + datehandler.getDate().substring(2,8) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "00.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_00.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + datehandler.getDate().substring(2,8) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "06.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_06.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + datehandler.getDate().substring(2,8) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "12.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_12.gif\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + datehandler.getDate().substring(2,8) + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "18.windir.gif\" style=”display:block” alt= \"MediterraneanWaveForecast_" + datehandler.getDate().substring(2,8) + "_18.gif\"> </p>";
			}
			
		}
		message += "<p align=\"right\">" + " במידה ואינך מעוניין/ת לקבל מייל זה יותר אנא שלח/י מייל בחוזר ותוסר/י בהקדם האפשרי " + "</p>";
		message += "</body></html>";

		return message;
		
	}
}
