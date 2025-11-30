package relax.gaming;

import io.undertow.Handlers;
import io.undertow.Undertow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.config.*;
import relax.gaming.core.Engine;
import relax.gaming.handlers.*;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final String SYMBOL_FILE = "symbols.json";
    private static final String PAYOUT_FILE = "payouts.json";
    private static final String BUCKET_FILE = "clusterBuckets.json";

    public static void main(String[] args) {
        LOGGER.info("Starting Server");

        ConfigManager configManager = new ConfigManager(SYMBOL_FILE, PAYOUT_FILE, BUCKET_FILE);
        Engine engine = new Engine(configManager);
        asyncServer(engine);
    }

    public static void asyncServer(Engine engine) {
        Undertow server = Undertow.builder()
            .addHttpListener(DEFAULT_PORT, DEFAULT_HOST)
            .setHandler(
                    Handlers.path()
                            .addPrefixPath("/spin", new HandleSpinAsync(engine))
                            .addPrefixPath("/sim", new HandleSimAsync(engine))
                            .addPrefixPath("/gamble", new HandleGambleAsync(engine))
            )
            .build();
        server.start();
        LOGGER.info("Server has started on {}:{}", DEFAULT_HOST, DEFAULT_PORT);
    }
}