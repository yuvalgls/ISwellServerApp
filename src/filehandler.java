import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;


public class filehandler {
		    
	    public static String DownLoadFile(String FileName, String Path, URL url){
			//System.out.println("Downloading file " + FileName);
			try {
		        File XMLFile = new File(Path + "\\" + FileName);
		        FileUtils.copyURLToFile(url, XMLFile);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			String  XMLFile = (Path + "\\" + FileName);
			return XMLFile;
		}

}
