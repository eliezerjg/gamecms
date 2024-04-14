import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;

import static javax.swing.JOptionPane.showMessageDialog;

public class LogUtils {

    public static final String logFilePath = "/GAMECMS.LOG";

    public enum LogType{
        ERROR, SUCCESS, WARNING
    }

    public static void write(LogType type, String message){
        try {
            doWrite(type.name().toUpperCase() + " - [" + Date.from(Instant.now()) + "]: " + message);
        } catch (IOException e) {
            showMessageDialog(JGameCMSUpdater.getInstance().getRootPane(), "Nao foi possível gravar a log. Reporte a gamecms code #1. Details: " + e.getMessage());
        }
    }

    private static void doWrite(String message) throws IOException {
        Path logFile = Paths.get(JGameCMSUpdater.getRootFolder() + logFilePath);
        if(!Files.exists(logFile)){
            Files.createFile(logFile);
        }
        String logFileContent = new String(Files.readAllBytes(logFile));
        String logToWrite = logFileContent + "\n"+ message;
        Files.write(logFile, logToWrite.getBytes());
    }
}
