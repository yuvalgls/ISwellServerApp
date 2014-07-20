import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class datehandler {
	
	public static String getDate(){
		//System.out.println("getting date");
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String currentdate = df.format(date);
		return currentdate;
	}
	public static String getHour(){
		//System.out.println("getting date");
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentdate = df.format(date);
		return currentdate;
	}
}
