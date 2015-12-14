import java.io.*;
import java.util.*;


public class FileManager {
	
	public FileManager(Vector<Data> theList, String fileName){
		Writer writer = null;
		try {
			if (!fileName.endsWith(".csv")){
				fileName = fileName+".csv"; //add the correct extension to the file
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			writer.write("Sample number;Temperature;Date\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i=0; i < theList.size(); i++){
			String line = Integer.toString(theList.get(i).getSampleNumber()) + ";" + Float.toString(theList.get(i).getTemperature()) + ";" + theList.get(i).getDate() + "\n";
			try {
				writer.write(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
