package numberserver.logging;

import java.io.IOException;

public interface ILoggingService {
    void logNewInputs() throws IOException;
    void createLogFile();
}
