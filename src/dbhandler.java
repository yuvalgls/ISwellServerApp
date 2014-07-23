import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextArea;


public class dbhandler {
	
	public static String[] ReadWaveDataFromDataBase(String location) throws Exception{
		//System.out.println("Reading From DataBase");
		String DB_URL = "jdbc:mysql://localhost/ISwell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    //String sql1 = "select max(id) from XMLParser";
	    String sql = "select * from XMLParser order by id desc limit 4";
	    ResultSet rs = stmt.executeQuery(sql);
	    location = GetLocationTranslate(location);
	    String[] dbdata = new String[15];
	    while(rs.next()){
	    	//int dbid = rs.getInt("id");
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
	    	//String UpOrDown = null;
	    	if(dbdata[0].equals(location)){
	    		return dbdata;
	    	}
	    }
	    return null;
	}
	
	public static void WriteToDB (String LocationName,String FromTime0,String ToTime0,String SeaHeight0,String SeaTemp,String WindDir0,String FromTime1,String ToTime1,String SeaHeight1,String WindDir1,String XmlFile){
		   // JDBC driver name and database URL
		   //final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		   String DB_URL = "jdbc:mysql://localhost/ISwell";
		   String USER = "root";
		   String PASS = "MySQLPassWord";
		   Connection conn = null;
		   Statement stmt = null;
		   int location = SeaHeight0.indexOf("/");
		   //System.out.println("SeaHeight0 " + SeaHeight0 + " WindDir0 " + WindDir0 + " location " + location);
		   String SeaHeightDesc0 = SeaHeight0.substring(0,location-1);
		   SeaHeight0 = SeaHeight0.substring(location+1);
		   location = SeaHeight1.indexOf("/");
		   //System.out.println("SeaHeight1 " + SeaHeight1 + " WindDir1 " + WindDir1 + " location " + location);
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
		      String sql2 = "INSERT INTO XMLParser(ID, LocationName, FromTime0, ToTime0,SeaHeight0,SeaHeightDesc0,SeaTemp,WindDir0,WindBlow0,FromTime1,ToTime1,SeaHeight1,SeaHeightDesc1,WindDir1,WindBlow1,FileName) SELECT MAX(ID)+1,"
		      		+ "('"+LocationName +"'),('" +FromTime0 +"'),('"+ToTime0+"'),('"+SeaHeight0+"'),('"+SeaHeightDesc0+"'),('"+SeaTemp+"'),('"+WindDir0+"'),('"+WindBlow0+"'),('"+FromTime1+"'),('"+ToTime1+
		      		"'),('"+SeaHeight1+"'),('"+SeaHeightDesc1+"'),('"+WindDir1+"'),('"+WindBlow1+"'),('"+(XmlFile.substring(XmlFile.length()-16))+"')FROM XMLParser";
		      stmt.executeUpdate(sql2);
		      
		      //emailhandler.PrepperEmailComtent(LocationName,FromTime0,ToTime0,SeaHeight0,SeaTemp,WindDir0,FromTime1,ToTime1,SeaHeight1,WindDir1,XmlFile);
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
		   //System.out.println("Finished writing : " + LocationName);
		}

	public static void ReadEmailListFromDataBase(JTextArea textAreaEmailLog) throws Exception{
		//System.out.println("Reading From DataBase : ISwell , clients");
		String DB_URL = "jdbc:mysql://localhost/ISwell";
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
	    	//int dbid = rs.getInt("id");
	    	String dbusername = rs.getString("username");
	    	String dbemail = rs.getString("email");
	    	String dbLastTimeSent = rs.getString("LastTimeSent");
	    	String dbToSend = rs.getString("ToSend");
	    	String dblocation = rs.getString("location");
	    	System.out.println(" ");
	    	System.out.println("Handeling : " + dbusername);
	    	String dbPreferTimeToGetEmail = rs.getString("PreferTimeToGetEmail");
	    	if(Integer.valueOf(dbToSend) == 1){
	    		String cd = datehandler.getDate();
		    	String currenttime = cd.substring(8,10);
		    	String currentdate = cd.substring(6,8);
		    	String PreferTime =dbPreferTimeToGetEmail.substring(0,2);
		    	String LastTime =dbLastTimeSent.substring(8,10);
		    	//if(Integer.parseInt(dbToSend)==1){
		    		if(Integer.parseInt(PreferTime) <= Integer.parseInt(currenttime) && (Integer.parseInt(currentdate)>Integer.parseInt(LastTime))  ){
		    			int location = dblocation.indexOf(",");
		    			//System.out.println(dblocation.substring(0,1));
		    			//emailhandler.PrepperEmailComtent(dbemail, dblocation.substring(0,1));
		    			emailhandler.SendEmailAsHTML(dbemail, dblocation.substring(location-1,location));
		    			while(location >= 0){
		    				//update textAreaEmailLog
		    		    	textAreaEmailLog.append(datehandler.getDate() + " Emailing : " + dbemail + "/n ");
		    		    	textAreaEmailLog.update(textAreaEmailLog.getGraphics());
		    		    	
		    				//emailhandler.PrepperEmailComtent(dbemail, dblocation.substring(location+1,location+2));
		    				emailhandler.SendEmailAsHTML(dbemail, dblocation.substring(location+1,location+2));
		    				location = dblocation.indexOf(",",location+1);
		    				//System.out.println(dblocation.substring(location+1,location+2));
		    				if(location==(-1)){
		    					break;
		    				}
		    			}
		    		}else{
		    			System.out.println("PreferTime <= currenttime : " + Integer.parseInt(PreferTime) + " <= " +  Integer.parseInt(currenttime));
			    		System.out.println("currentdate <= LastTime " + Integer.parseInt(currentdate) + " > " + Integer.parseInt(LastTime));
		    		}
		    	}else{
		    		System.out.println(dbusername + " doesnt want to get an email");
		    	}
	    	}
	    }
	

	public static String GetWaveTranslate(String SeaHeight) throws Exception{
		//System.out.println("Translating SeaHeight " + SeaHeight);
		String DB_URL = "jdbc:mysql://localhost/ISwell";
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
	    	//int dbid = rs.getInt("id");
	    	String Code = rs.getString("Code");
	    	Code.trim();
	    	//String SeaStatusEng = rs.getString("SeaStatusEng");
	    	String SeaStatusHeb = rs.getString("SeaStatusHeb");
	    	if(Code.equals(SeaHeight)){
	    		//result = SeaStatusEng.trim();
	    		result = SeaStatusHeb.trim();
    			//System.out.println(result);
	    		break;
	    	}
	    }
	    return result;
	   
	}

	public static String[] GetWindTranslate(String Wind) throws Exception{
		String[] result = new String[2];
		//System.out.println("Translating Wind " + Wind);
		String DB_URL = "jdbc:mysql://localhost/ISwell";
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
	    		//int dbid = rs.getInt("id");
	    		String Code = rs.getString("Code");
	    		Code.trim();
	    		//String WindDirectionHeb = rs.getString("WindDirectionHeb");
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
		//System.out.println("Translating SeaHeight " + location);
		String DB_URL = "jdbc:mysql://localhost/ISwell";
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
	    	//int dbid = rs.getInt("id");
	    	String Code = rs.getString("Code");
	    	Code.trim();
	    	String Location = rs.getString("Location");
	    	//String SeaStatusHeb = rs.getString("SeaStatusHeb");
	    	if(Code.equals(location)){
	    		result = Location.trim();
	    		//System.out.println(result);
	    		break;
	    	}
	    }
	    return result;
	   
	}

	public static void WriteLastSend(String Email) throws Exception{
		String ch = datehandler.getHour();
		   String DB_URL = "jdbc:mysql://localhost/ISwell";
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
}