package numberserver.connection;

import java.io.IOException;

public interface IConnectionListenerFactory {
    ConnectionListener getConnectionListener() throws IOException;
}
