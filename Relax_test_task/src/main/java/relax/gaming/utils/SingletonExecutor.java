package relax.gaming.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SingletonExecutor {
    public static final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
}
