package numberserver.loggingtests;

import numberserver.logging.ILoggingAdapter;
import numberserver.logging.LoggingAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

public class LoggingAdapterTests {
    private static final String _logFileName = "numbers.log";
    private ILoggingAdapter _loggingAdapter;

    @Before public void Setup() {
        _loggingAdapter = new LoggingAdapter();
    }

    @After public void TearDown() {
        var file = new File(_logFileName);

        if (file.exists()) {
            file.delete();
        }
    }

    @Test public void CreateLogFileIfNotExists_CreatesEmptyLogFile() {
        _loggingAdapter.createLogFileIfNotExists();

        var file = new File(_logFileName);
        Assert.assertTrue(file.exists());
    }

    @Test public void CreateLogFileIfNotExists_ClearsLogFileIfExists() throws IOException {
        var file = new File(_logFileName).createNewFile();
        var fileWriter = new BufferedWriter(new FileWriter(_logFileName, true));
        fileWriter.append("newline!\n");
        fileWriter.append("anotherNewline!");
        fileWriter.close();

        var fileReader = new BufferedReader(new FileReader(_logFileName));
        var contents = fileReader.lines();

        Assert.assertEquals(2, contents.count());

        _loggingAdapter.createLogFileIfNotExists();

        fileReader = new BufferedReader(new FileReader(_logFileName));
        contents = fileReader.lines();

        Assert.assertEquals(0, contents.count());

        fileReader.close();
    }

    @Test public void WriteInputs_CorrectlyWritesAllInputsOnNewLines() throws IOException {
        _loggingAdapter.createLogFileIfNotExists();

        var inputs = new ArrayList<String>();
        for (var i = 0; i < 10; i ++) {
            inputs.add("newstring"+i);
        }

        _loggingAdapter.writeBatchToLogFile(inputs);

        var fileReader = new BufferedReader(new FileReader(_logFileName));
        var contents = fileReader.lines();

        Assert.assertEquals(10, contents.count());

        fileReader.close();
    }
}
