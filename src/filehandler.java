import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
			return Path + FileName;
		}
	    
		public static String download(String Source, String file) throws IOException{
			String path = file.substring(0,file.length()-16);
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
			return file;
		}
		
		public static String[] FindSubFiles(String Folder) {
			File file = new File(Folder);
			String[] SubFiles = file.list(new FilenameFilter() {
				public boolean accept(File current, String name) {
					return new File(current, name).isFile();
				}
			});
			return SubFiles;
		}
		
		public static boolean SaveTXTFile(String File, String text) {
			try {
//				Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(File) ,"UTF-8"));
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(File, true), "UTF-8"));
//				Writer out = new BufferedWriter(new FileWriter(File, true));
				out.write(text + "\r\n");
//				out.append(text + "\r\n");
				out.close();
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
	    }
		public static String ReadTXTFile(String file) throws IOException {
	        BufferedReader reader = new BufferedReader(new FileReader(file));
	        String line = null;
	        StringBuilder stringBuilder = new StringBuilder();
	        String ls = System.getProperty("line.separator");

	        while ((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }
	        reader.close();
	        return stringBuilder.toString();
	    }


}
