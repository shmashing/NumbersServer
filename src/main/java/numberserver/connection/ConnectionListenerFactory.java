package numberserver.connection;

import numberserver.client.IClientHandlerFactory;

import java.io.IOException;

public class ConnectionListenerFactory implements IConnectionListenerFactory {
    private ConnectionListenerConfiguration _configuration;
    private IClientHandlerFactory _clientFactory;

    public ConnectionListenerFactory(IClientHandlerFactory clientFactory, ConnectionListenerConfiguration configuration) {
        _configuration = configuration;
        _clientFactory = clientFactory;
    }

    public ConnectionListener getConnectionListener() throws IOException {
        return new ConnectionListener(_configuration.PortToListen, _configuration.MaxConnections, _clientFactory);
    }
}
