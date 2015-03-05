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
	public static String reformat(String date){
//		System.out.println(date);
//		System.out.println("0123456789");
		String dd = date.substring(6,8);
		String MM = date.substring(4,6);
		String yyyy = date.substring(0,4);
		String hh = date.substring(8,10);
		String mm = date.substring(10,12);
		String redate = dd + "/" + MM + "/" + yyyy + " לשעה " + hh + ":" + mm;
//		System.out.println();
		return  redate;
	}
}
