package numberserver.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientFactoryTests {
    private IClientHandlerFactory _clientFactory;

    @Before public void Setup() {
        _clientFactory = new ClientHandlerFactory();
    }

    @Test public void GetClientHandler_ReturnsNewClientHandler() {
        var input = "123123";
        var inputStream = new ByteArrayInputStream(input.getBytes());
        var inputScanner = new Scanner(inputStream);
        var client = _clientFactory.getClient(new Socket(), inputScanner);

        Assert.assertNotNull(client);
    }
}
