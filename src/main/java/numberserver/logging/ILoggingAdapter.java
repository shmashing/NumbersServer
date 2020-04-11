package numberserver.logging;

import java.io.IOException;
import java.util.List;

public interface ILoggingAdapter  {
    void writeBatchToLogFile(List<String> numberToWrite) throws IOException;
    void createLogFileIfNotExists();
    void clearLogFile();
}
