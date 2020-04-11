package numberserver.client;

import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerFactory implements IClientHandlerFactory {

    public ClientHandler getClient(Socket socket, Scanner inputScanner) {
        return new ClientHandler(socket, inputScanner);
    }
}
