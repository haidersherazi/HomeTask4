
import java.io.*;

public class DataExchangeSimulator {

	public static void simulateByteStream(String sourceFile, String destinationFile) {
		// both source and destination files should be in .dat format
        try (FileInputStream fis = new FileInputStream("input.dat");
             FileOutputStream fos = new FileOutputStream("output.dat")) {
            
        	// Read from fis
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                // write it to the output file
                fos.write(buffer, 0, bytesRead);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simulateCharacterStream(String sourceFile, String destinationFile) {
    	// both source and destination files should be in .txt format
        try (FileReader fr = new FileReader(sourceFile);
             FileWriter fw = new FileWriter(destinationFile)) {
            
        	// Read from fr
        	char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = fr.read(buffer)) != -1) {
                // write it to the output file
                fw.write(buffer, 0, charsRead);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
