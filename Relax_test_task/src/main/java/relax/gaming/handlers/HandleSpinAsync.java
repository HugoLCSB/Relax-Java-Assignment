package relax.gaming.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import relax.gaming.core.Engine;
import relax.gaming.utils.Json;
import relax.gaming.utils.SingletonExecutor;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HandleSpinAsync implements HttpHandler {
    private static final Logger LOGGER = LogManager.getLogger(HandleSpinAsync.class);
    private final Engine engine;

    public HandleSpinAsync(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {

        exchange.dispatch(() -> {

            try {
                Map<String, Deque<String>> params = exchange.getQueryParameters();
                long seed = params.get("seed") != null ? Long.parseLong(params.get("seed").getFirst()) : 0;
                double bet = Double.parseDouble(params.get("bet").getFirst());

                LOGGER.info("Received spin request");

                CompletableFuture.supplyAsync(() -> this.engine.doSpin(seed, bet), SingletonExecutor.REGULAR_REQUEST_POOL)
                        .thenAccept(result -> {
                            try {
                                String json = Json.toJson(result);
                                exchange.getResponseHeaders().put(
                                        Headers.CONTENT_TYPE, "application/json"
                                );
                                exchange.getResponseSender().send(json);
                                LOGGER.info("Spin completed.");
                            } finally {
                                ThreadContext.clearMap();
                            }
                        });
            } catch (Exception e) {
                LOGGER.error("Spin request failed", e);
                exchange.setStatusCode(400);
                exchange.getResponseSender().send("Bad request");
            }
        });
    }
}
