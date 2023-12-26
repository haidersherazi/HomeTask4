
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnergyManagementLogger {

	public static void log(String message) {
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = currentDateTime.format(dateformatter);
		
		try (FileWriter writer = new FileWriter("logs/" + formattedDate + "/EnergyManagementLog.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
