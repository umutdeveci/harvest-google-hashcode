package harvest;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileReaderUtil {
	public static String read(String FilePath) {
	
	    String str = null;
	    StringBuilder strb = new StringBuilder();
	    // the following line means the try block takes care of closing the resource
	    try (BufferedReader br = new BufferedReader(new FileReader(FilePath))) {
	        while ((str = br.readLine()) != null) {
	            strb.append(str).append("\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return strb.toString();
	}
}

