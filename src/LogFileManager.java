
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class LogFileManager {
    private static final String logStr = "logs/";

    // Create a directory for logs if it doesn't exist
    static {
        try {
            Files.createDirectories(Paths.get(logStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createPerDayDirectory(String formattedDate) {
    	
		
		// Check if the directory already exists
        if (!Files.exists(Paths.get(logStr + formattedDate))) {
            try {
                // Create the directory if it doesn't exist
                Files.createDirectories(Paths.get(logStr + formattedDate));
//                System.out.println("Directory created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//            System.out.println("Directory already exists.");
        }
    }
    
    public static void createLogFile(String fileName, Boolean createOnlyOnce) {
        try {
        	
        	String pathName = "";
        	
        	if (createOnlyOnce) {
        		
        		pathName = logStr;
        		
        	} else {
        		LocalDateTime currentDateTime = LocalDateTime.now();
        		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        		String formattedDate = currentDateTime.format(dateformatter);
                
        		createPerDayDirectory(formattedDate);
        		
        		pathName = logStr + formattedDate;
        	}
        	
    		
        	Path logFilePath = Paths.get(pathName, fileName);
        	
        	if (!Files.exists(logFilePath)) {
                Files.createFile(logFilePath);
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveLogFile(String sourceFileName, String destinationFileName) {
        try {
            Files.move(
                    Paths.get(logStr, sourceFileName),
                    Paths.get(logStr, destinationFileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLogFile(String fileName) {
        try {
            Files.delete(Paths.get(logStr, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void archiveLogFile(String fileName) {
        String archivedFileName = "archive_" + fileName;
        moveLogFile(fileName, archivedFileName);
    }
}