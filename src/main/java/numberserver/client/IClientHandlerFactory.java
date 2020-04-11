package numberserver.client;

import java.net.Socket;
import java.util.Scanner;

public interface IClientHandlerFactory {
    ClientHandler getClient(Socket socket, Scanner inputScanner);
}
