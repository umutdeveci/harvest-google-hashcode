package harvest;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FileWriterUtil {

	public static void write(String path, String value) {
		//get some content to write to a file
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(path))){
		  buffer.write(value);
		  buffer.newLine();
		  buffer.write(value+value);
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}	
}
