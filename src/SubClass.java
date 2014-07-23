import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextField;


public class SubClass {
	
	private static Timer timer;
    public static int count = 0;
	public static void StartHere(Object txtTimesRun) {
		timer = new Timer();
        timer.schedule(new SubClass.TimerListenerDownload(), 1000, 3600000);
        //timer.schedule(new TimerListener(), 1000);
    }
	public static class TimerListenerDownload extends TimerTask {
        JTextField txtTimesRun;
        public void run(){
			count++;
            System.out.println("This is the count  : " + count + " " + datehandler.getDate()) ;
    		String cd = datehandler.getDate();
    		//check if you should download a new file
    		boolean NewFile;
			try {
				NewFile = filehandler.ShouldDownLoadNewFile(cd);
				if(NewFile == true ){
	    			String Path = "C:\\ISwell\\DownloadFolder";
	    			URL url = null;
	    			try {
	    				url = new URL("http://www.ims.gov.il/ims/PublicXML/isr_sea.xml");
	    			} catch (MalformedURLException e) {
	    				e.printStackTrace();
	    			}
	    			//download the file and store the data on the mysql server
	    			filehandler.DownLoadFile(cd + ".xml", Path , url);
	    			System.out.println("Xml was downloaded to : " + Path + "\\" + cd + ".xml");
	    			//dbhandler.ReadFromDataBase();
	    		}
				dbhandler.ReadEmailListFromDataBase(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
        }
    }
}
