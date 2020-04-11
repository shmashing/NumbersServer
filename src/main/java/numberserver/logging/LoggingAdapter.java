package numberserver.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LoggingAdapter implements ILoggingAdapter{
    private static String _logFileName = "numbers.log";
    public LoggingAdapter() { }

    public void createLogFileIfNotExists() {
        try {
            var logFile = new File(_logFileName);
            var fileNotExists = logFile.createNewFile();

            if (!fileNotExists) {
                clearLogFile();
            }
        }
        catch (Exception ex)
        {
            System.out.println("An error occurred creating rolling log file.");
            ex.printStackTrace();
        }
    }

    public void writeBatchToLogFile(List<String> numberToWrite) throws IOException {
        var fileWriter = new BufferedWriter(new FileWriter(_logFileName, true));
        numberToWrite.forEach(num -> {
            try {
                fileWriter.append(num);
                fileWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }

    public void clearLogFile() {
        try {
            var logFile = new FileWriter(_logFileName);
            logFile.write("");
            logFile.close();
        }
        catch (Exception ex) {
            System.out.println("An error occurred clearing rolling log file.");
            ex.printStackTrace();
        }
    }
}
