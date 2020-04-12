package numberserver.connection;

import numberserver.client.ClientHandler;
import numberserver.client.IClientHandlerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConnectionListener implements Runnable {
    private ServerSocket _serverSocket = null;
    private int _maxConnections;
    private int _port;
    private List<ClientHandler> _clients;
    private IClientHandlerFactory _clientFactory;

    public ConnectionListener(int port, int maxConnections, IClientHandlerFactory clientFactory) {
        _port = port;
        _maxConnections = maxConnections;
        _clientFactory = clientFactory;
        _clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            _serverSocket = getServerSocket();

            while (!Thread.currentThread().isInterrupted()) {
                if (ConnectionUtils.ActiveConnections.get() >= _maxConnections) continue;
                try {
                    var connectionSocket = getSocket();
                    ConnectionUtils.ActiveConnections.incrementAndGet();

                    var inputScanner = getInputScanner(connectionSocket);
                    var clientHandler = _clientFactory.getClient(connectionSocket, inputScanner);

                    startClientThread(clientHandler);
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

    public ServerSocket getServerSocket() throws IOException {
        return new ServerSocket(_port);
    }

    public Socket getSocket() throws IOException {
        return _serverSocket.accept();
    }

    public Scanner getInputScanner(Socket socket) throws IOException {
        var inputStream = socket.getInputStream();
        return new Scanner(inputStream, "UTF-8");
    }

    public void startClientThread(ClientHandler client) {
        var thread = new Thread(client);
        thread.start();
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
