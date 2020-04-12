package numberserver.connectiontests;

import numberserver.client.ClientHandlerFactory;
import numberserver.client.IClientHandlerFactory;
import numberserver.connection.ConnectionListenerConfiguration;
import numberserver.connection.ConnectionListenerFactory;
import numberserver.connection.IConnectionListenerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ConnectionListenerFactoryTests {
    private IClientHandlerFactory _clientFactory;

    private ConnectionListenerConfiguration _configuration;
    private IConnectionListenerFactory _connectionFactory;

    @Before public void Setup() {
        _clientFactory = mock(ClientHandlerFactory.class);

        _configuration = new ConnectionListenerConfiguration(4444, 2);
        _connectionFactory = new ConnectionListenerFactory(_clientFactory, _configuration);
    }

    @Test
    public void GetConnectionListener_ReturnsNonNullListener() {
        var listener = _connectionFactory.getConnectionListener();

        Assert.assertNotNull(listener);
    }
}
