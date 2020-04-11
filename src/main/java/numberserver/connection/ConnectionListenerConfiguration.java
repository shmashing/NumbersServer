package numberserver.connection;

public class ConnectionListenerConfiguration {
    public int PortToListen;
    public int MaxConnections;

    public ConnectionListenerConfiguration(int portToListen, int maxConnections) {
        PortToListen = portToListen;
        MaxConnections = maxConnections;
    }
}
