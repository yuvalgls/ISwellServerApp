import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.io.FileUtils;


public class filehandler {
		    
	    public static String DownLoadFile(String FileName, String Path, URL url) throws IOException{
	    	File path = new File(Path);
	    	if(!path.exists()){
	    		path.mkdir();
	    	}
	    	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(60000);
			urlConnection.setReadTimeout(60000);
			BufferedReader breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while((line = breader.readLine()) != null) {
				stringBuilder.append(line);
			}
			path = new File(Path + FileName);
			FileUtils.writeStringToFile(path, stringBuilder.toString(), "UTF-8");
			//System.out.println(stringBuilder.toString());
//			BufferedWriter Writer = new BufferedWriter( new FileWriter( Path + FileName ));
//			System.out.println(stringBuilder.toString());
//		    Writer.write(stringBuilder.toString() );
//		    Writer.close( );
			return Path + FileName;
		}
	    
		public static void download(String Source, String file) throws IOException{
//			System.out.println(Source);
//			System.out.println(file);
			String path = file.substring(0,file.length()-16);
//			System.out.println(path);
			File File = new File(path);
			if(!File.exists()){
				File.mkdirs();
	    	}
			URL website = new URL(Source);
	    	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		}

}
