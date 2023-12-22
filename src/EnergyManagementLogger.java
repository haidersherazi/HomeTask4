
import java.io.*;

public class EnergyManagementLogger {

	public static void log(String message) {
		try (FileWriter writer = new FileWriter("logs/EnergyManagementLog.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
