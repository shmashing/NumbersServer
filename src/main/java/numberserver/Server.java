package numberserver;

import numberserver.caching.CacheUtils;
import numberserver.connection.ConnectionUtils;
import numberserver.connection.IConnectionListenerFactory;
import numberserver.logging.ILoggingService;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private int _reportTimerDelay = 10000;
    private int _reportTimerPeriod = 10000;
    private int _loggingTimerDelay = 1000;
    private int _loggingTimerPeriod = 1000;

    private ILoggingService _loggingService;
    private IConnectionListenerFactory _connectionFactory;

    public Server(ILoggingService loggingService, IConnectionListenerFactory connectionFactory) {
        _loggingService = loggingService;
        _connectionFactory = connectionFactory;
    }

    public void startServer() throws IOException, InterruptedException {
        CacheUtils.initializeCaches();
        _loggingService.createLogFile();

        var reportingTimer = new Timer();
        var loggingTimer = new Timer();

        reportingTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                reportOnInput();
            }
        }, _reportTimerDelay, _reportTimerPeriod);

        loggingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    _loggingService.logNewInputs();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, _loggingTimerDelay, _loggingTimerPeriod);

        var connectionListener = _connectionFactory.getConnectionListener();
        var thread = new Thread(connectionListener);
        thread.start();

        while (!ConnectionUtils.TerminateServer) {
            Thread.sleep(100);
        }

        reportingTimer.cancel();
        loggingTimer.cancel();

        thread.interrupt();
        connectionListener.closeSocketAndExit();

        _loggingService.logNewInputs();
    }

    private void reportOnInput() {
        var cacheSnapshot = CacheUtils.getCacheSnapshot();

        System.out.println("Received " + cacheSnapshot.NewUniqueNumbers
                + " unique numbers, " + cacheSnapshot.Duplicates
                + " duplicates. Unique total: " + cacheSnapshot.TotalUniqueNumbers);
    }
}
