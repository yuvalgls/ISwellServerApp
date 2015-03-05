import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JTextArea;


public class dbhandler {
	
	public static String[] ReadWaveDataFromDataBase(String location) throws Exception{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from xmlparser order by id desc limit 4";
	    ResultSet rs = stmt.executeQuery(sql);
	    location = GetLocationTranslate(location);
	    String[] dbdata = new String[15];
	    while(rs.next()){
	    	dbdata[0] = rs.getString("LocationName");
	    	dbdata[1] = rs.getString("FromTime0");
	    	dbdata[2] = rs.getString("ToTime0");
	    	dbdata[3] = rs.getString("SeaHeight0");
	    	dbdata[4] = rs.getString("SeaHeightDesc0");
	    	dbdata[5] = rs.getString("SeaTemp");
	    	dbdata[6] = rs.getString("WindDir0");
	    	dbdata[7] = rs.getString("WindBlow0");
	    	dbdata[8] = rs.getString("FromTime1");
	    	dbdata[9] = rs.getString("ToTime1");
	    	dbdata[10] = rs.getString("SeaHeight1");
	    	dbdata[11] = rs.getString("SeaHeightDesc1");
	    	dbdata[12] = rs.getString("WindDir1");
	    	dbdata[13] = rs.getString("WindBlow1");
	    	dbdata[14] = rs.getString("FileName");
	    	if(dbdata[0].equals(location)){
	    		return dbdata;
	    	}
	    }
	    return null;
	}
	
	public static void WriteToDB (String LocationName,String FromTime0,String ToTime0,String SeaHeight0,String SeaTemp,String WindDir0,String FromTime1,String ToTime1,String SeaHeight1,String WindDir1,String XmlFile){
		   String DB_URL = "jdbc:mysql://localhost/iswell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   int location = SeaHeight0.indexOf("/");
		   String SeaHeightDesc0 = SeaHeight0.substring(0,location-1);
		   SeaHeight0 = SeaHeight0.substring(location+1);
		   location = SeaHeight1.indexOf("/");
		   String SeaHeightDesc1 = SeaHeight1.substring(0,location-1);
		   SeaHeight1 = SeaHeight1.substring(location+1);
		   location = WindDir0.indexOf("/");
		   String WindBlow0 = WindDir0.substring(location+1);
		   WindDir0 = WindDir0.substring(0,location);
		   location = WindDir1.indexOf("/");
		   String WindBlow1 = WindDir1.substring(location+1);
		   WindDir1 = WindDir1.substring(0,location);
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      stmt = conn.createStatement();
		      String sql2 = "INSERT INTO xmlparser(ID, LocationName, FromTime0, ToTime0,SeaHeight0,SeaHeightDesc0,SeaTemp,WindDir0,WindBlow0,FromTime1,ToTime1,SeaHeight1,SeaHeightDesc1,WindDir1,WindBlow1,FileName) SELECT MAX(ID)+1,"
		      		+ "('"+LocationName +"'),('" +FromTime0 +"'),('"+ToTime0+"'),('"+SeaHeight0+"'),('"+SeaHeightDesc0+"'),('"+SeaTemp+"'),('"+WindDir0+"'),('"+WindBlow0+"'),('"+FromTime1+"'),('"+ToTime1+
		      		"'),('"+SeaHeight1+"'),('"+SeaHeightDesc1+"'),('"+WindDir1+"'),('"+WindBlow1+"'),('"+(XmlFile.substring(XmlFile.length()-16))+"')FROM xmlparser";
		      stmt.executeUpdate(sql2);
		      conn.close();
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e1){
		      e1.printStackTrace();
		   }finally{
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		}

	public static void ReadEmailListFromDataBase(JTextArea textAreaEmailLog , JTextArea textAreaSMSLog) throws Exception{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from clients order by id desc";
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	    	String dbusername = rs.getString("username");
//	    	if(dbusername.toLowerCase() == "Yuval".toLowerCase()){
//	    		System.out.println("Stoping");
//	    	}
	    	String dbemail = rs.getString("email");
	    	String dbLastTimeSent = rs.getString("LastTimeSent");
	    	String dbToSend = rs.getString("ToSend");
	    	String dblocation = rs.getString("location");
	    	String dbfromheight = rs.getString("SendFromHeight");
	    	String dbPreferTimeToGetEmail = rs.getString("PreferTimeToGetEmail");
	    	String SendToPhone = rs.getString("SendToPhone");
	    	String PhoneNum = rs.getString("PhoneNum");
//	    	System.out.println(" ");
//	    	System.out.println("Handeling : " + dbusername + " from height " + dbfromheight);
	    		String cd = datehandler.getDate();
		    	String currenttime = cd.substring(8,10);
		    	String currentdate = cd.substring(6,8);
		    	String PreferTime =dbPreferTimeToGetEmail.substring(0,2);
		    	String LastTime =dbLastTimeSent.substring(8,10);
		    	if(Integer.parseInt(dbToSend)==1 || Integer.valueOf(SendToPhone) ==1){
		    		if(Integer.parseInt(PreferTime) <= Integer.parseInt(currenttime) && (Integer.parseInt(currentdate)!=Integer.parseInt(LastTime))  ){
		    			int location = dblocation.indexOf(",");
		    			if(location==(-1)){
		    		    	String sql1 = "SELECT * FROM iswell.xmlparser WHERE LocationName='"+ dbhandler.GetLocationTranslate(dblocation) +"' order by id desc limit 1;";
//		    		    	System.out.println(sql1);
		    		    	stmt1 = conn.createStatement();
		    			    ResultSet rs1 = stmt1.executeQuery(sql1);
		    			    rs1.next();
		    		    	String dbheight = rs1.getString("SeaHeight0");
		    		    	dbheight = dbheight.substring(dbheight.indexOf("-")+1);
		    		    	System.out.println(dbusername + " wants from = " + dbfromheight + " and the height  is " + dbheight);
		    		    	if(Integer.valueOf(dbheight)>Integer.valueOf(dbfromheight)){
		    		    		if(Integer.parseInt(dbToSend) == 1){
		    		    			textAreaEmailLog.insert(datehandler.getDate() + " Emailing update: " + dbemail + "\n", 0);
			    		    		textAreaEmailLog.update(textAreaEmailLog.getGraphics());
			    		    		emailhandler.SendEmailAsHTML(dbemail, dblocation);
			    		    		dbhandler.WriteLastSend(dbemail);
		    		    		}
		    		    		if(Integer.parseInt(SendToPhone) == 1){
		    		    			textAreaSMSLog.insert(datehandler.getDate() +  " SMSing : " + dbusername + " " + PhoneNum + "\n", 0);
			    		    		textAreaSMSLog.update(textAreaSMSLog.getGraphics());
		    		    			smshandler.sendsms(PhoneNum, dblocation);
		    		    			dbhandler.WriteLastSend(dbemail);
		    		    		}
		    		    	}else{
//		    		    		System.out.println(Integer.valueOf(dbheight) + " >= " + Integer.valueOf(dbfromheight));
		    		    	}
		    				
		    			}else{
		    				String sql1 = "SELECT * FROM iswell.xmlparser WHERE LocationName='"+ dbhandler.GetLocationTranslate(dblocation.substring(location-1,location)) +"' order by id desc limit 1;";
		    		    	stmt1 = conn.createStatement();
		    			    ResultSet rs1 = stmt1.executeQuery(sql1);
		    			    rs1.next();
		    		    	String dbheight = rs1.getString("SeaHeight0");
		    		    	dbheight = dbheight.substring(dbheight.indexOf("-")+1);
//		    		    	System.out.println("dbfromheight = " + dbfromheight + " dbheight = " + dbheight);
		    		    	if(Integer.valueOf(dbheight) >= Integer.valueOf(dbfromheight)){
		    		    		if(Integer.parseInt(dbToSend) == 1){
		    		    			textAreaEmailLog.insert(datehandler.getDate() + " Emailing Update: " + dbemail + "\n", 0);
			    		    		textAreaEmailLog.update(textAreaEmailLog.getGraphics());
			    		    		emailhandler.SendEmailAsHTML(dbemail, dblocation.substring(location-1,location));
			    		    		dbhandler.WriteLastSend(dbemail);
		    		    		}
		    		    		if(Integer.parseInt(SendToPhone) == 1){
		    		    			textAreaSMSLog.insert(datehandler.getDate() +  " SMSing : " + dbusername + " " + PhoneNum + "\n", 0);
			    		    		textAreaSMSLog.update(textAreaSMSLog.getGraphics());
		    		    			smshandler.sendsms(PhoneNum, dblocation.substring(location-1,location));
		    		    			dbhandler.WriteLastSend(dbemail);
		    		    		}
			    				
		    		    	}else{
//		    		    		System.out.println(Integer.valueOf(dbheight) + " >= " + Integer.valueOf(dbfromheight));
		    		    	}
		    			}
		    			while(location >= 0){
		    				String sql1 = "SELECT * FROM iswell.xmlparser WHERE LocationName='"+ dbhandler.GetLocationTranslate(dblocation.substring(location+1,location+2)) +"' order by id desc limit 1;";
		    		    	stmt1 = conn.createStatement();
		    			    ResultSet rs1 = stmt1.executeQuery(sql1);
		    			    rs1.next();
		    		    	String dbheight = rs1.getString("SeaHeight0");
		    		    	dbheight = dbheight.substring(dbheight.indexOf("-")+1);
		    		    	if(Integer.valueOf(dbheight) >= Integer.valueOf(dbfromheight)){
		    		    		if(Integer.parseInt(dbToSend) == 1){
		    		    			textAreaEmailLog.insert(datehandler.getDate() + " Emailing update : " + dbemail + "\n", 0);
			    		    		textAreaEmailLog.update(textAreaEmailLog.getGraphics());
			    		    		emailhandler.SendEmailAsHTML(dbemail, dblocation.substring(location+1,location+2));
			    		    		dbhandler.WriteLastSend(dbemail);
		    		    		}
		    		    		if(Integer.parseInt(SendToPhone) == 1){
		    		    			textAreaSMSLog.insert(datehandler.getDate() +  " SMSing : " + dbusername + " " + PhoneNum + "\n", 0);
			    		    		textAreaSMSLog.update(textAreaSMSLog.getGraphics());
		    		    			smshandler.sendsms(PhoneNum, dblocation.substring(location+1,location+2));
		    		    			dbhandler.WriteLastSend(dbemail);
		    		    		}
		    		    		
		    		    	}else{
//		    		    		System.out.println(Integer.valueOf(dbheight) + " >= " + Integer.valueOf(dbfromheight));
		    		    	}
		    		    	location = dblocation.indexOf(",",location+1);
		    		    	if(location==(-1)){
		    					break;
		    				}
		    		    	
		    			}
		    			
		    		}else{
//		    			System.out.println("PreferTime <= currenttime : " + Integer.parseInt(PreferTime) + " <= " +  Integer.parseInt(currenttime));
//			    		System.out.println("currentdate != LastTime : " + Integer.parseInt(currentdate) + " != " + Integer.parseInt(LastTime));
		    		}
		    	}else{
//		    		System.out.println(dbusername + " doesnt want to get an email or SMS");
		    	}
	    }
	}
	
	public static String GetWaveTranslate(String SeaHeight) throws Exception{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from seastatecodes order by id";
	    ResultSet rs = stmt.executeQuery(sql);
	    String result = null;
	    SeaHeight = SeaHeight.replaceAll("\\s+", "");
	    while(rs.next()){
	    	String Code = rs.getString("Code");
	    	Code.trim();
	    	String SeaStatusHeb = rs.getString("SeaStatusHeb");
	    	if(Code.equals(SeaHeight)){
	    		result = SeaStatusHeb.trim();
	    		break;
	    	}
	    }
	    return result;
	   
	}

	public static String[] GetWindTranslate(String Wind) throws Exception{
		String[] result = new String[2];
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from winddirection order by id";
	    ResultSet rs = stmt.executeQuery(sql);
	    String a = Wind;
	    int location = Wind.indexOf("-");
	    if(location!=(-1)){
	    	a = Wind.substring(0,location);
	    	a = a.replaceAll("\\s+", "");
	    	String b = Wind.substring(location+1);
	    	b = b.replaceAll("\\s+", "");
	    	while(rs.next()){
	    		String Code = rs.getString("Code");
	    		Code.trim();
	    		if(Code.equals(b)){
	    			String WindDirectionEng = rs.getString("WindDirectionHeb");
	    			result[1] = WindDirectionEng.trim();
	    		}
	    		if(Code.equals(a)){
		    		String WindDirectionEng = rs.getString("WindDirectionHeb");
		    		result[0] = WindDirectionEng.trim();
		    	}
	    
	    	}
	    }
		return result;
		
	}
	
	public static String GetLocationTranslate(String location) throws Exception{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from locationlist order by id";
	    ResultSet rs = stmt.executeQuery(sql);
	    String result = null;
	    location = location.replaceAll("\\s+", "");
	    while(rs.next()){
	    	String Code = rs.getString("Code");
	    	Code.trim();
	    	String Location = rs.getString("Location");
	    	if(Code.equals(location)){
	    		result = Location.trim();
	    		break;
	    	}
	    }
	    return result;
	   
	}
	public static String Simplesql(String db, String table, String field) throws SQLException, ClassNotFoundException{
		String DB_URL = "jdbc:mysql://localhost/" + db;
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select " + field + " from " + table +" order by id desc limit 4";
	    ResultSet rs = stmt.executeQuery(sql);
	    String result = null;
	    while(rs.next()){
	    	result = rs.getString(field);
	    }
	    return result;
	    }

	public static void WriteLastSend(String Email) throws Exception{
		String ch = datehandler.getHour();
		   String DB_URL = "jdbc:mysql://localhost/iswell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      stmt = conn.createStatement();
		      String sql2 = "UPDATE clients SET LastTimeSent = \"" + ch + "\" WHERE email = \"" + Email + "\"";
		      stmt.executeUpdate(sql2);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e1){
		      e1.printStackTrace();
		   }finally{
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		   }
	public static void WriteLastwarningSent(String Email, String Warningdate){

		String DB_URL = "jdbc:mysql://localhost/iswell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      stmt = conn.createStatement();
		      String sql2 = "UPDATE clients SET LastWarningSent = \"" + Warningdate + "\" WHERE email = \"" + Email + "\"";
		      stmt.executeUpdate(sql2);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e1){
		      e1.printStackTrace();
		   }finally{
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	public static void WriteLastlongrange(String Email, String LastLongRangeSent){

		String DB_URL = "jdbc:mysql://localhost/iswell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      stmt = conn.createStatement();
		      String sql2 = "UPDATE clients SET LastLongRangeSent = \"" + LastLongRangeSent + "\" WHERE email = \"" + Email + "\"";
		      stmt.executeUpdate(sql2);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e1){
		      e1.printStackTrace();
		   }finally{
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	public static int sendwarning(String msg, String msgdate, JTextArea textAreaEmailLog) throws SQLException, ClassNotFoundException{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from clients order by id desc";
	    ResultSet rs = stmt.executeQuery(sql);
	    int del = 0;
	    
	    while(rs.next()){
	    	String email = rs.getString("email");
	    	String LastWarningSent = rs.getString("LastWarningSent");
	    	String ToSend = rs.getString("ToSend");
	    	if(LastWarningSent == null){
	    		LastWarningSent = "000000000000000000000";
	    	}
	    	if(Integer.valueOf(ToSend) == 1){
	    		String Last_dd = LastWarningSent.substring(8,10);
	    		String Last_hh = LastWarningSent.substring(11,13);
	    		String Last_mm = LastWarningSent.substring(14,16);
	    		String Now_dd = msgdate.substring(8,10);
	    		String Now_hh = msgdate.substring(11,13);
	    		String Now_mm = msgdate.substring(14,16);
//		    		System.out.print(Last_dd + "<" + Now_dd + " -> ");
//		    		System.out.println(Integer.valueOf(Last_dd) < Integer.valueOf(Now_dd));
//		    		System.out.print(Last_dd + "=" + Now_dd + " & " + Now_hh + ">" + Last_hh + " -> ");
//		    		System.out.println(Integer.valueOf(Last_dd) == Integer.valueOf(Now_dd) && Integer.valueOf(Now_hh)>Integer.valueOf(Last_hh));
//		    		System.out.print(Last_dd + "=" + Now_dd + " & " + Now_hh + ">" + Last_hh + " "+ Now_mm + ">" + Last_mm + " -> ");
//		    		System.out.println(Integer.valueOf(Last_dd) == Integer.valueOf(Now_dd) && Integer.valueOf(Now_hh) == Integer.valueOf(Last_hh) && Integer.valueOf(Now_mm)>Integer.valueOf(Last_mm));
	    		//                anouther day                       ||                         anouther hour                                                                    ||                                                                         anouther min                                          
	    		if(((Integer.valueOf(Last_dd) < Integer.valueOf(Now_dd))||(Integer.valueOf(Last_dd) == Integer.valueOf(Now_dd) && Integer.valueOf(Now_hh)>Integer.valueOf(Last_hh)) || (Integer.valueOf(Last_dd) == Integer.valueOf(Now_dd) && Integer.valueOf(Now_hh) == Integer.valueOf(Last_hh) && Integer.valueOf(Now_mm)>Integer.valueOf(Last_mm)))){
	    			try {
//		    				System.out.println("emailing : " + email);
	    				if(email.equals("yuval.glasman@gmail.com")){
							emailhandler.sentmsg( email,msg, "Emailing warning : " + msgdate);
							dbhandler.WriteLastwarningSent(email, msgdate);
							textAreaEmailLog.insert(datehandler.getDate() + " Emailing Warning : " + email + "\n", 0);
	    		    		textAreaEmailLog.update(textAreaEmailLog.getGraphics());
							del++;
	    				}
//							System.out.println("inside if ! ! ! del = " + del);
					} catch (AddressException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
						}
	    			}
	    		}
	    	}
	    return del;
	    }
	public static void writemswtodb(String[] wave, String[] days, String filename, JTextArea txtDataLog){
		String DB_URL = "jdbc:mysql://localhost/iswell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      stmt = conn.createStatement();
		      for(int i = 0 ; i < 100 ; i++){
		            if(days[i] != null && wave[i] != null){
		            	if((Double.valueOf(wave[i])*100 > 0)){
		            		String sql = "INSERT INTO mswdata (ID, Day, Height, FileName) SELECT MAX(ID)+1,('" + days[i] + "'),('" +wave[i] + "'),('" + filename +".html') FROM mswdata";
		      		      	stmt.executeUpdate(sql);
		            	}
		            }
		        }
		      
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e1){
		      e1.printStackTrace();
		   }finally{
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		   txtDataLog.insert("LongRange updated on : " + datehandler.getDate() + "\n", 0);
		txtDataLog.update(txtDataLog.getGraphics());
	}
	public static void SendLongRange(String[] wave, String[] days, String filename, JTextArea textAreaEmailLog) throws ClassNotFoundException, SQLException, AddressException, MessagingException{
		String DB_URL = "jdbc:mysql://localhost/iswell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql = "select * from clients order by id desc";
	    ResultSet rs = stmt.executeQuery(sql);
	    
	    while(rs.next()){
	    	String LastLongRangeSent = rs.getString("LastLongRangeSent");
	    	String ToSend = rs.getString("ToSend");
	    	String email = rs.getString("email");
	    	String FromHeight = rs.getString("SendFromHeight");
	    	if(LastLongRangeSent == null){
	    		LastLongRangeSent = "2014-01-01 01:02:03";
	    	}
	    	System.out.println("for email : " + email + " LastLongRangeSent = " +  LastLongRangeSent);
	    	String maxday = "0";
	    	String maxheight = "0";
	    	if(Integer.valueOf(ToSend) == 1){
	    		for(int i = 0 ; i < 100 ; i++ ){
	    			if(days[i] != null && wave[i] != null){
		            	if(Double.valueOf(maxheight) < Double.valueOf(wave[i])){
		            		maxheight = wave[i];
		            		maxday = days[i];
		            	}
		            }
	    		}
	    		System.out.println("sea will max out on : " + maxday + " and it will be : " + maxheight);
	    		String sentmonth = LastLongRangeSent.substring(5,7);
	    		String sentday = LastLongRangeSent.substring(8,10);
	    		System.out.println("sentday = " + sentday + " sentmonth = " + sentmonth);
	    		String month = maxday.substring(maxday.length()-4,maxday.length()-2);
	    		String day = maxday.substring(maxday.length()-2,maxday.length());
	    		System.out.println("day = " + day + " month = " + month);
	    		System.out.println("Integer.valueOf(sentday) < Integer.valueOf(day)+4 " + Integer.valueOf(sentday) +"<"+ (Integer.valueOf(day) + 4)  );
	    		//this is it!!!
	    		System.out.println("Integer.valueOf(sentmonth) != Integer.valueOf(month) " + Integer.valueOf(sentmonth) +"!="+ Integer.valueOf(month));
	    		if(Integer.valueOf(sentday) < (Integer.valueOf(day)+4) || Integer.valueOf(sentmonth) != Integer.valueOf(month)){
	    			System.out.println("Double.valueOf(FromHeight) = " + Double.valueOf(FromHeight));
	    			System.out.println("Double.valueOf(maxheight)*100 = " + Double.valueOf(maxheight)*100);
	    			if(Double.valueOf(FromHeight) <= Double.valueOf(maxheight)*100){
	    				System.out.println(maxday.substring(0,maxday.length()-4));
	    				DecimalFormat df = new DecimalFormat("#.##");
	    				String msg = "get ready, its going to be : " + df.format(Double.valueOf(maxheight)/3) + " on the : " + day + "\\" + month + " which is next " + maxday.substring(0,maxday.length()-4);
	    				String sub = "Long Range Forcast";
	    				if(email.equals("yuval.glasman@gmail.com")){
		    				emailhandler.sentmsg(email, msg, sub);
		    				textAreaEmailLog.insert(datehandler.getDate() + " Emailing LongRange : " + email + "\n", 0);
	    		    		textAreaEmailLog.update(textAreaEmailLog.getGraphics());
		    				dbhandler.WriteLastlongrange(email, "2014" + "-" + month + "-" + day + " 00:00:00" );
		    				}
	    				}
	    			}
	    		}
	    	}
	    }
	}
