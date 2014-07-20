import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class emailhandler {
	
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
			message.setSubject("תחזית גלים");
			message.setText(Body);
			Transport.send(message);
			System.out.println("finished sending emails to : " + EMail + " about " + Data[0]);
			dbhandler.WriteLastSend(EMail);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
				}
		}

}
