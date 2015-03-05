import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class emailhandler {

	public static void sentmsg(String Email, String msgmsg , String msgsub) throws AddressException, MessagingException{
//		System.out.println("Emailing : " + Email);
		String host = "smtp.gmail.com";
        String port = "587";
        String subject = msgsub;
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Iswell.israel@gmail.com", "gls27100");
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("Iswell.israel@gmail.com"));
        InternetAddress[] toAddresses = { new InternetAddress(Email) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setContent(msgmsg, "text/html; charset=utf-8");
        Transport.send(msg);
	}
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
		int a, b, c, d, Starting, Ending = 0;
		Data[3] = Data[3].replaceAll("\\s","");
		Data[10] = Data[10].replaceAll("\\s","");
		a = Integer.valueOf(Data[3].substring(0,2));
		b = Integer.valueOf(Data[3].substring(Data[3].length()-2));
		c = Integer.valueOf(Data[10].substring(0,2));
		d = Integer.valueOf(Data[10].substring(Data[10].length()-2));
		Starting = (a+b)/2 ;
		Ending = (c+d)/2;
		String Status = null;
		int sum = Integer.valueOf(Starting) - Integer.valueOf(Ending);
		//System.out.println((Data[1].substring(Data[1].length()-5,Data[1].length()-3)));
		if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
			if(sum>0){
				Status = "לאורך היום הים  במגמת עליה";
			}
			if(sum<0){
				 Status = "לאורך היום הים במגמת ירידה";
			}
			if(sum==0){
				 Status = "לאורך היום הים לא משתנה ";
			}
		}else{
			if(sum>0){
				Status = "לאורך הלילה  הים  במגמת עליה";
			}
			if(sum<0){
				 Status = "לאורך הלילה  הים  במגמת ירידה";
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
		
		String message = "<html><body>";
		message +="<p align=\"right\">: ! ! ! הדיוור היום באדיבות</p>";
		message +="<p align=\"center\"><img src=\"http://www.terem.co.il/images/trash.jpg\" style=”display:block” alt= \"Sponser\"  width=\"750\" height=\"300\"> </p>";
		message +="<p align=\"right\">  הירקון 15 תל אביב טלפון 035104232 </p>";
		message +="<p align=\"right\">  ____________________________________________ </p>";
		message +="<p align=\"right\">  עבור " + HeblocationName + "  הים יהיה  " + Data[4] + "  בגובה של " +  Data[3] + "</p>";
		String temp = "";
		String[] WindData = dbhandler.GetWindTranslate(Data[6]);
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
			String imsupdate = SubClass.validateimsurl(datehandler.getDate().substring(2,8));
			if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "00.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "06.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "12.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))) + "18.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
			}else{
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "00.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "06.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "12.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "18.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
			}
		}
		message += " ";
		Data[11] = dbhandler.GetWaveTranslate(Data[11]);
		if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
			if(sum>0){
				Status = "לאורך הלילה  ";
			}
			if(sum<0){
				 Status = "לאורך הלילה  ";
			}
			if(sum==0){
				 Status = "לאורך הלילה   ";
			}
		}else{
			if(sum>0){
				Status = "לאורך היום ";
			}
			if(sum<0){
				 Status = "לאורך היום ";
			}
			if(sum==0){
				 Status = "לאורך היום  ";
			}
		}
		message +="<p align=\"right\">" + Status + "  הים יהיה  " + Data[11] + "  בגובה של " +  Data[10] + "</p>";
		WindData = dbhandler.GetWindTranslate(Data[12]);
		if(WindData.length>0){
			for(int i=0 ; i <= WindData.length-1 ; i++){
				if(WindData[i] != null){
					temp = temp + " " + WindData[i];
				}
			}
		};
		message += "<p align=\"right\"> הרוח תהיה" + temp + " במהירות של " + Data[13]  + " קשר " + "</p>";
		
		if(Data[0].equals("Southern Coast")|| Data[0].equals("Northern Coast")){
			String imsupdate = SubClass.validateimsurl(datehandler.getDate().substring(2,8));
			if(Integer.valueOf(Data[1].substring(Data[1].length()-5,Data[1].length()-3)) == 8){
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "00.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "06.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "12.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+1) + "18.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
			}else{
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+2) + "00.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+2) + "06.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+2) + "12.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
				message += "<p  align=\"center\" href=\"www.terem.co.il\"><img src=\"http://isramar.ocean.org.il/isramar2009/wave_model/wave_maps/wam/" + imsupdate + "0000/coarse/"+ (Integer.valueOf(datehandler.getDate().substring(2,8))+2) + "18.windir.gif\" style=”display:block” alt= \"התמונה איננה זמינה כרגע\"> </p>";
			}
		}
		
		
		message +="<p align=\"right\">  התחזית מעודכנת מתאריך : " + datehandler.reformat(Data[14]) + "</p>";
		message += "<p align=\"right\">" + " במידה ואינך מעוניין/ת לקבל מייל זה יותר אנא שלח/י מייל בחוזר ותוסר/י בהקדם האפשרי " + "</p>";
		message += "</body></html>";
		System.out.println("");
		System.out.println(message);
		System.out.println("");
		return message;
		
	}
}
