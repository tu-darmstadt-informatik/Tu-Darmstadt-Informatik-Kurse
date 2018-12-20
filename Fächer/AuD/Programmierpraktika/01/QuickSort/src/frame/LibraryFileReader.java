package frame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Do NOT change anything in this class!
 * 
 * This class contains a method for reading the input files into a Vector
 * structure.
 * 
 * @author Stefan Kropp
 */
public class LibraryFileReader {

	private String filename = null;
	private ArrayList<String[]> data = null;

	/**
	 * The file should be in the same directory as the java application. if not,
	 * you have to provide the absolute or relative path information within the
	 * filename string.
	 * 
	 * @param filename
	 *            the name of the file to read
	 */
	public LibraryFileReader(String filename) {
		this.filename = filename;
		this.data = new ArrayList<String[]>();
	}

	/**
	 * Reads a file, specified in the private field filename and returns the
	 * information read. The file should have the same format as specified in
	 * the first lab.
	 * 
	 * @return Returns a Vector which holds 3-dimensional String arrays with
	 *         following format: BookSerialNumber, ReaderID, Status. In the case
	 *         an error occured null is returned.
	 */
	public ArrayList<String[]> readFile() {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader in = new BufferedReader(fr);

			String line;
			while ((line = in.readLine()) != null) {
				data.add(line.split(";"));
			}

			in.close();
			fr.close();

			return data;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
