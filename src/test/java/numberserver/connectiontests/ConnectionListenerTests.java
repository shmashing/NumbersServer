package numberserver.connectiontests;

import numberserver.client.ClientHandler;
import numberserver.client.ClientHandlerFactory;
import numberserver.client.IClientHandlerFactory;
import numberserver.connection.ConnectionListener;
import numberserver.connection.ConnectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ConnectionListenerTests {

    private Scanner _inputScanner;
    private IClientHandlerFactory _clientFactory;

    private ConnectionListener _connectionListener;

    private Thread _mainThread;

    @Before public void Setup() throws IOException {
        _inputScanner = new Scanner("");
        Socket _socket = mock(Socket.class);
        _clientFactory = mock(ClientHandlerFactory.class);

        when(_clientFactory.getClient(_socket, _inputScanner)).thenReturn(mock(ClientHandler.class));

        _connectionListener = new ConnectionListener(9999, 3, _clientFactory) {
            @Override
            public Socket getSocket() {
                return mock(Socket.class);
            }

            @Override
            public Scanner getInputScanner(Socket socket) {
                return _inputScanner;
            }

            @Override
            public void startClientThread(ClientHandler client) {
                return;
            }
        };
    }

    @After public void TearDown() {
        if (_mainThread.isAlive() && !_mainThread.isInterrupted()) {
            _mainThread.interrupt();
        }
    }

    @Test public void Run_OnlyAccepts_MaxConnections() throws IOException {
        ConnectionUtils.ActiveConnections.set(0);

        _mainThread = new Thread(_connectionListener);
        _mainThread.start();

        var sockets = new ArrayList<Socket>();
        for (var i = 0; i < 10; i ++) {
            var socket = new Socket("localhost", 9999);
            socket.setKeepAlive(true);
            sockets.add(socket);
        }

        _mainThread.interrupt();
        _connectionListener.closeSocketAndExit();

        verify(_clientFactory, times(3)).getClient(any(Socket.class), eq(_inputScanner));

        sockets.forEach(s -> {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

