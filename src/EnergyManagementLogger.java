
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnergyManagementLogger {

	public static void log(String message) {
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = currentDateTime.format(dateformatter);
		DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss");
		
		try (FileWriter writer = new FileWriter("logs/" + formattedDate + "/EnergyManagementLogs.txt", true)) {
            writer.write(currentDateTime.format(dateTimeformatter) + "     " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
