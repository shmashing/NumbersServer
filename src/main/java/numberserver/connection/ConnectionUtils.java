package numberserver.connection;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionUtils {
    public static AtomicInteger ActiveConnections = new AtomicInteger(0);
    public static boolean TerminateServer = false;
}
