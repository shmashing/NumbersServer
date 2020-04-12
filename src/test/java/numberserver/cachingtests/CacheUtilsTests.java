package numberserver.cachingtests;

import numberserver.caching.CacheUtils;
import numberserver.client.ClientHandler;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.util.Scanner;

import static org.mockito.Mockito.mock;

public class CacheUtilsTests {
    private Socket _socket;

    private Scanner _inputScanner;
    private ClientHandler _clientHandler;

    @Before public void Setup() {
        CacheUtils.initializeCaches();

        _socket = mock(Socket.class);

        var input = getInputScannerString(0, 40);
        var inputStream = new ByteArrayInputStream(input.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);
        _clientHandler.run();
    }

    @Test public void GetCacheSnapshot_ReturnCorrectMetrics() {
        var snapshot = CacheUtils.getCacheSnapshot();
        Assert.assertEquals(40, snapshot.TotalUniqueNumbers);
        Assert.assertEquals(40, snapshot.NewUniqueNumbers);
        Assert.assertEquals(0, snapshot.Duplicates);

        var input = getInputScannerString(0, 40);
        var inputStream = new ByteArrayInputStream(input.getBytes());
        _inputScanner = new Scanner(inputStream);
        _clientHandler = new ClientHandler(_socket, _inputScanner);
        _clientHandler.run();

        var nextSnapshot = CacheUtils.getCacheSnapshot();
        Assert.assertEquals(40, nextSnapshot.TotalUniqueNumbers);
        Assert.assertEquals(0, nextSnapshot.NewUniqueNumbers);
        Assert.assertEquals(40, nextSnapshot.Duplicates);
    }

    @Test public void GetUnloggedInputs_ReturnsCorrectInputs() {
        var inputsToLog = CacheUtils.getUnloggedInputs();
        Assert.assertEquals(40, inputsToLog.size());

        var input = getInputScannerString(40, 80);
        var inputStream = new ByteArrayInputStream(input.getBytes());
        _inputScanner = new Scanner(inputStream);
        _clientHandler = new ClientHandler(_socket, _inputScanner);
        _clientHandler.run();

        var newInputsToLog = CacheUtils.getUnloggedInputs();
        Assert.assertEquals(40, newInputsToLog.size());

        Assert.assertThat(inputsToLog, IsNot.not(IsEqual.equalTo(newInputsToLog)));
    }

    private String getInputScannerString(int start, int stop) {
        var input = "";
        for (var i = start; i < stop; i ++ ) {
            var newInputInt = 100000000 + i;
            input = input + newInputInt + "\n";
        }

        return input;
    }
}
