import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;


public class filehandler {
	
	public static boolean ShouldDownLoadNewFile(String cd) throws Exception{
		System.out.println("Check if i should download a new file");
		boolean ans = false;
		String DB_URL = "jdbc:mysql://localhost/ISwell";
		String USER = "root";
		String PASS = "MySQLPassWord";
		int dbid ;
		String dbXmlFile = null;
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    String sql1 = "select id , FileName from XMLParser order by id desc limit 1";
	    ResultSet rs1 = stmt.executeQuery(sql1);
	    while(rs1.next()){
	    	dbid = rs1.getInt("id");
	    	dbXmlFile = rs1.getString("FileName");
	    }
	    int x = Integer.parseInt(dbXmlFile.substring(0,10));
	    int y = Integer.parseInt(cd.substring(0,10));
	    int z = y - x;
	    System.out.println("last file was downloaded " + z + " hours ago" + " xml file date : " + dbXmlFile + " cd date " + cd);
	    if(z >= 6 ){//&& z<24){
	    	System.out.println("Download new file!!! (returning true)");
	    	return true;
	    }else{
	    	System.out.println("Should NOT Download new file (returning false)...");
	    	return false;
	    }
	}
	    
	    public static void DownLoadFile(String FileName, String Path, URL url){
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
		}

}
