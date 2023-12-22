
import java.io.*;

public class SystemLogger {

	public static void log(String message) {
		try (FileWriter writer = new FileWriter("logs/SystemLog.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
