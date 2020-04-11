package numberserver.caching;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheUtils {
    private static ConcurrentHashMap<String, Boolean> _inputCache;
    private static AtomicInteger _duplicates;
    private static int _lastUniqueCount;

    public static void initializeCaches() {
        _inputCache = new ConcurrentHashMap<>();
        _duplicates = new AtomicInteger(0);
        _lastUniqueCount = 0;
    }

    public static CacheSnapshot getCacheSnapshot() {
        var snapshot = new CacheSnapshot();

        var totalUnique = getInputCount();
        snapshot.TotalUniqueNumbers = totalUnique;
        snapshot.NewUniqueNumbers = totalUnique - _lastUniqueCount;
        snapshot.Duplicates = getDuplicates();

        resetMetrics(totalUnique);

        return snapshot;
    }
    public static void putInput(String input) {
        _inputCache.put(input, false);
    }

    public static boolean inputCacheContainsKey(String key) {
        return _inputCache.containsKey(key);
    }

    public static int getInputCount() {
        return _inputCache.size();
    }

    public static List<String> getUnloggedInputs() {
        var inputs = new ArrayList<String>();
        synchronized (_inputCache)
        {
            _inputCache.forEach((input, isLogged) -> {
                if (!isLogged) {
                    inputs.add(input);
                }
            });

            inputs.forEach(input -> _inputCache.replace(input, true));
        }

        return inputs;
    }

    public static void resetMetrics(int uniqueCount) {
        _duplicates = new AtomicInteger(0);
        _lastUniqueCount = uniqueCount;
    }

    public static void incrementDuplicates() {
        _duplicates.incrementAndGet();
    }

    public static int getDuplicates() {
        return _duplicates.get();
    }
}
