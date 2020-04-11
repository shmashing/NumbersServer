package numberserver.connection;

import numberserver.client.ClientHandler;
import numberserver.client.IClientHandlerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConnectionListener implements Runnable {
    private ServerSocket _serverSocket = null;
    private int _maxConnections;
    private List<ClientHandler> _clients;
    private IClientHandlerFactory _clientFactory;

    public ConnectionListener(int port, int maxConnections, IClientHandlerFactory clientFactory) throws IOException {
        _serverSocket = new ServerSocket(port);
        _maxConnections = maxConnections;
        _clientFactory = clientFactory;
        _clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (ConnectionUtils.ActiveConnections >= _maxConnections) {
                    continue;
                }
                try {
                    var connectionSocket = _serverSocket.accept();
                    ConnectionUtils.ActiveConnections++;

                    var inputStream = connectionSocket.getInputStream();
                    var outputStream = connectionSocket.getOutputStream();

                    var inputScanner = new Scanner(inputStream, "UTF-8");
                    var serverOutput = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

                    serverOutput.println("Successfully Connected! Feel Free to Start Entering Nine Digit Numbers!");
                    var clientHandler = _clientFactory.getClient(connectionSocket, inputScanner);

                    var thread = new Thread(clientHandler);
                    thread.start();
                    _clients.add(clientHandler);
                } catch (SocketException e) {
                    // server has exited
                    closeClients();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSocketAndExit() throws IOException {
        _serverSocket.close();
    }

    public void closeClients() throws IOException {
        for (ClientHandler _client : _clients) {
            _client.exitClientHandler();
        }
    }
}
