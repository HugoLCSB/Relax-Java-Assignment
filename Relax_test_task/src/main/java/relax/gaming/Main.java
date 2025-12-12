package relax.gaming;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import io.undertow.Handlers;
import io.undertow.Undertow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.config.ConfigManager;
import relax.gaming.core.Engine;
import relax.gaming.handlers.HandleGambleAsync;
import relax.gaming.handlers.HandleSimAsync;
import relax.gaming.handlers.HandleSpinAsync;
import relax.gaming.utils.SingletonExecutor;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    @Parameter(names = "--symbols", description = "Path to config file 1")
    private String SYMBOL_FILE = "symbols.json";

    @Parameter(names = "--payouts", description = "Path to config file 2")
    private String PAYOUT_FILE = "payouts.json";

    @Parameter(names = "--buckets", description = "Path to config file 3")
    private String BUCKET_FILE = "clusterBuckets.json";

    public static void main(String[] args) {
        try {
            Main main = new Main();

            JCommander.newBuilder()
                    .addObject(main)
                    .build()
                    .parse(args);

            LOGGER.info("Starting Server");

            ConfigManager configManager = new ConfigManager(main.SYMBOL_FILE, main.PAYOUT_FILE, main.BUCKET_FILE);
            Engine engine = new Engine(configManager);
            Undertow server = asyncServer(engine);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutdown initiated.");
                server.stop();
                SingletonExecutor.shutdown();
            }));
        } catch (ParameterException e) {
            LOGGER.error("Invalid command", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error running main", e);
        }
    }

    public static Undertow asyncServer(Engine engine) {
        try {
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
            return server;
        } catch (Exception e) {
            LOGGER.error("Error starting the server", e);
            throw e;
        }
    }
}