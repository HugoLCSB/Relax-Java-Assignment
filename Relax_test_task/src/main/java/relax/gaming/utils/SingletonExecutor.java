package relax.gaming.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SingletonExecutor {
    public static final ExecutorService REGULAR_REQUEST_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ExecutorService SIMULATION_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
}
