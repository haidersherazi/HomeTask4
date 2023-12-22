
import java.io.*;

public class ChargingStationLogger {

	public static void log(String message) {
		try (FileWriter writer = new FileWriter("logs/ChargingStationLog.txt", true)) {
			writer.write(message + "\n");
		} catch (IOException e) {
        	e.printStackTrace();
        }
    }
}
