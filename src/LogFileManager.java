
import java.io.*;
import java.nio.file.*;

class LogFileManager {
    private static final String logStr = "logs";

    // Create a directory for logs if it doesn't exist
    static {
        try {
            Files.createDirectories(Paths.get(logStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createLogFile(String fileName) {
        try {
            Files.createFile(Paths.get(logStr, fileName));
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