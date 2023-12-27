
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFileOpener {
	
	public static String constructLogFolderPath(String userInput) {

		String logsDirectory = "logs/" + userInput ;

        return logsDirectory;
    }

	public static void openLogFilesInFolder(String folderPath, String regex) {
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                Pattern pattern = Pattern.compile(regex);

                for (File file : files) {
                    if (file.isFile()) {
                        Matcher matcher = pattern.matcher(file.getName());
                        if (matcher.matches()) {
                            openLogFile(file.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
//            System.out.println("Invalid folder path: " + folderPath);
        	
        	System.out.println("The files against this input does not exist.");
        	System.out.print("Thanks for using our system. See you next time!! ");
        }
    }

    private static void openLogFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Opening log file: " + filePath);
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
