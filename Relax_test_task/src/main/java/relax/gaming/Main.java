package relax.gaming;

import io.undertow.Handlers;
import io.undertow.Undertow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.handlers.*;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        LOGGER.info("Starting Server");
        asyncServer();
    }

    public static void asyncServer() {
        Undertow server = Undertow.builder()
            .addHttpListener(DEFAULT_PORT, DEFAULT_HOST)
            .setHandler(
                    Handlers.path()
                            .addPrefixPath("/spin", new HandleSpinAsync())
                            .addPrefixPath("/sim", new HandleSimAsync())
                            .addPrefixPath("/gamble", new HandleGambleAsync())
            )
            .build();
        server.start();
        LOGGER.info("Server has started on {}:{}", DEFAULT_HOST, DEFAULT_PORT);
    }
}