package numberserver.client;

import numberserver.caching.CacheUtils;
import numberserver.connection.ConnectionUtils;
import numberserver.stringutils.StringUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket _clientSocket;
    private Scanner _inputScanner;

    public ClientHandler(Socket clientSocket, Scanner inputScanner) {
        _clientSocket = clientSocket;
        _inputScanner = inputScanner;
    }

    @Override
    public void run() {
        var terminateServer = false;

        while(_inputScanner.hasNextLine()) {
            var userInput = _inputScanner.nextLine();

            if(userInput.toLowerCase().trim().equals("terminate")) {
                terminateServer = true;
                break;
            }

            var parseResult = StringUtils.tryParseInteger(userInput);
            if (userInput.length() != 9)
            {
                break;
            }

            if (!parseResult)
            {
                break;
            }

            if (CacheUtils.inputCacheContainsKey(userInput))
            {
                CacheUtils.incrementDuplicates();
                continue;
            }

            CacheUtils.putInput(userInput);
        }

        try {
            if (terminateServer) {
                terminateServerAndExit();
            } else {
                exitClientHandler();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminateServerAndExit() throws IOException {
        ConnectionUtils.TerminateServer = true;
        exitClientHandler();
    }

    public void exitClientHandler() throws IOException {
        _clientSocket.close();
        ConnectionUtils.ActiveConnections --;
    }
}
