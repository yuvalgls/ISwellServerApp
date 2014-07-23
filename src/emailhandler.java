import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class emailhandler {
	/*
	public static void PrepperEmailComtent(String EMail, String location) throws Exception{		
		String[] Data = dbhandler.ReadWaveDataFromDataBase(location);
		
		System.out.println("prepering email for  " + Data[0]);
		//System.out.println("");
		final String username = "ISwell.israel@gmail.com";
		final String password = "gls27100";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	            	return new PasswordAuthentication(username, password);
	            }
	        });
		
		Data[4] = dbhandler.GetWaveTranslate(Data[4]);
		String[] WindData = dbhandler.GetWindTranslate(Data[6]);
		String Body = "Today surf report for " + Data[0] + "\nIt will be "+ Data[4] + " around " + Data[3] + " cm.\nWith water temperature of " + Data[5] +
				" degrees celsius \nThe wind will be around " + Data[7] + " In Direction of ";
		String temp = "";
				if(WindData.length>0){
					for(int i=0 ; i <= WindData.length-1 ; i++){
						if(WindData[i] != null){
							temp = temp + " " + WindData[i];
						}
					}
				};
				temp = temp + " \nthats all 4 now\nthank you";
				Body = Body + temp;
		//System.out.println(Body);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ISwell.israel@gmail.com"));
			//SEND TO ME
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMail));
			message.setSubject("Surf Report");
			//message.setDataHandler(arg0);
			message.setText(Body);
			//message.setContent(Body, "text/plain");
			//message.setContent("בדיקה 1 2 3 4", "text/UTF-8");
			Transport.send(message);
			System.out.println("finished sending emails to : " + EMail + " about " + Data[0]);
			dbhandler.WriteLastSend(EMail);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
				}
		}
	
	*/
	public static void SendEmailAsHTML(String EMail, String location) throws Exception, Exception{
		String[] Data = dbhandler.ReadWaveDataFromDataBase(location);
		//for(int i=0 ; i<Data.length ; i++){
		//	System.out.println("Data["+ i + "] = " +  Data[i]);
		//}
		String Starting = Data[3].substring(Data[3].length()-2) ;
		String Ending = Data[10].substring(Data[10].length()-2);
		String Status=null;
		int sum = Integer.valueOf(Starting) - Integer.valueOf(Ending);
		if(sum>0){
			 Status = "בסך הכל הים יהיה במגמת ירידה";
		}
		if(sum<0){
			 Status = "בסך הכל הים יהיה במגמת עליה";
		}
		if(sum==0){
			 Status = "בסך הכל הים לא משתנה לאורך היום";
		}
			
				
		System.out.println("prepering email for  " + Data[0]);
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
		
		
		String message = "<p align=\"right\">  עבור " + HeblocationName + "  הים יהיה  " + Data[4] + "  בגובה של " +  Data[3] + "</p>";
		
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
		message += "<p align=\"right\">" + " במידה ואינך מעוניין/ת לקבל מייל זה יותר אנא שלח/י מייל בחוזר ותוסר/י בהקדם האפשרי " + "</p>";
		
		message += "<p  align=\"right\"><script async src=\"pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>	"
				+ "<!-- IswellEmails -->"
				+ "					<ins class=\"adsbygoogle\""
				+ "					style=\"display:inline-block;width:728px;height:90px\""
				+ "				data-ad-client=\"ca-pub-5571446606079800\""
				+ "							data-ad-slot=\"5839141350\"></ins>"
				+ "<script>"
				+ "(adsbygoogle = window.adsbygoogle || []).push({});"
				+ "</script> </p>";
		/* 
		 * 
		 * <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
			<!-- IswellEmails -->
			<ins class="adsbygoogle"
     		style="display:inline-block;width:728px;height:90px"
     		data-ad-client="ca-pub-5571446606079800"
     		data-ad-slot="5839141350"></ins>
			<script>
			(adsbygoogle = window.adsbygoogle || []).push({});
			</script>
		 * 
		 * */
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
        dbhandler.WriteLastSend(EMail);
	}
}
