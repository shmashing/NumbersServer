package numberserver.clienttests;

import numberserver.caching.CacheUtils;
import numberserver.client.ClientHandler;
import numberserver.connection.ConnectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ClientHandlerTests {

    private Socket _socket;
    private Scanner _inputScanner;

    private ClientHandler _clientHandler;

    @Before public void Setup() {
        CacheUtils.initializeCaches();
        _socket = mock(Socket.class);

        var goodInput = "123123123";
        var inputStream = new ByteArrayInputStream(goodInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);
    }

    @Test public void Run_CorrectlyParsesInput() throws IOException {
        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 1;
        verify(_socket, times(1)).close();
    }

    @Test public void Run_CorrectlyParsesInputWithLeadingZeros() throws IOException {
        var badInput = "000123123";
        var inputStream = new ByteArrayInputStream(badInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);

        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 1;
        verify(_socket, times(1)).close();
    }

    @Test public void Run_TerminatesWithLessThanNineDigits() throws IOException {
        var badInput = "123123";
        var inputStream = new ByteArrayInputStream(badInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);

        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 0;
        verify(_socket, times(1)).close();
    }

    @Test public void Run_TerminatesWithNonIntegerInput() throws IOException {
        var badInput = "123123abc";
        var inputStream = new ByteArrayInputStream(badInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);

        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 0;
        verify(_socket, times(1)).close();
    }

    @Test public void Run_TerminatesServerWhenCommandEntered() throws IOException {
        var terminateInput = "terminate";
        var inputStream = new ByteArrayInputStream(terminateInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);

        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 0;
        assert ConnectionUtils.TerminateServer;
        verify(_socket, times(1)).close();
    }

    @Test public void Run_DuplicateEntriesOnlyGetRecordedOnce() throws IOException {
        var duplicateInput = "123123123\n123123123\n234234234\n234234234";
        var inputStream = new ByteArrayInputStream(duplicateInput.getBytes());
        _inputScanner = new Scanner(inputStream);

        _clientHandler = new ClientHandler(_socket, _inputScanner);

        _clientHandler.run();

        var newInput = CacheUtils.getInputCount();
        assert newInput == 2;
        assert CacheUtils.getDuplicates() == 2;
        verify(_socket, times(1)).close();
    }
}
