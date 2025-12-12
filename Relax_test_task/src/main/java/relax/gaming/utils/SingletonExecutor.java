package relax.gaming.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is a Singleton that provides Thread pools.
 */
public final class SingletonExecutor {
    public static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService REGULAR_REQUEST_POOL = Executors.newFixedThreadPool(NUM_CORES / 2);
    public static final ExecutorService SIMULATION_POOL = Executors.newFixedThreadPool(NUM_CORES / 2);

    /**
     * The shutdown method
     */
    public static void shutdown() {
        REGULAR_REQUEST_POOL.shutdown();
        SIMULATION_POOL.shutdown();
    }
}
