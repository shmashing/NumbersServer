package numberserver.logging;

import numberserver.caching.CacheUtils;

import java.io.IOException;

public class LoggingService implements ILoggingService {

    private ILoggingAdapter _loggingAdapter;

    public LoggingService(ILoggingAdapter loggingAdapter) {
        _loggingAdapter = loggingAdapter;
    }

    public void logNewInputs() throws IOException {
        var newInputs = CacheUtils.getUnloggedInputs();
        _loggingAdapter.writeBatchToLogFile(newInputs);
    }

    public void createLogFile() {
        _loggingAdapter.createLogFileIfNotExists();
    }

}
