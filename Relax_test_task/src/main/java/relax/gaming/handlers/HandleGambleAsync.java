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
import relax.gaming.utils.Utils;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HandleGambleAsync implements HttpHandler {
    private static final Logger LOGGER = LogManager.getLogger(HandleGambleAsync.class);
    private final Engine engine;

    public HandleGambleAsync(Engine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Engine can't be null");
        }
        this.engine = engine;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.dispatch(() -> {
            try {
                Map<String, Deque<String>> params = exchange.getQueryParameters();
                String seedParam = params.get("seed") != null ? params.get("seed").getFirst() : null;
                String betParam = params.get("bet") != null ? params.get("bet").getFirst() : null;

                if (betParam == null) {
                    LOGGER.warn("Missing required parameter bet");
                    Utils.sendHttpResponse(exchange, 400, "Missing required parameter bet");
                    return;
                }

                long seed = seedParam != null ? Long.parseLong(seedParam) : 0;
                double bet = Double.parseDouble(betParam);

                LOGGER.info("Received gamble request");
                CompletableFuture.supplyAsync(() -> this.engine.doGamble(seed, bet), SingletonExecutor.REGULAR_REQUEST_POOL)
                        .thenAccept(result -> {
                            try {
                                String json = Json.toJson(result);
                                exchange.getResponseHeaders().put(
                                        Headers.CONTENT_TYPE, "application/json"
                                );
                                exchange.getResponseSender().send(json);
                                LOGGER.info("Gamble completed.");
                            } finally {
                                ThreadContext.clearMap();
                            }
                        })
                        .exceptionally(e -> {
                            LOGGER.error("Error doing gamble execution", e);
                            Utils.sendHttpResponse(exchange, 500, "Internal error");
                            return null;
                        });
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid parameter values", e);
                Utils.sendHttpResponse(exchange, 400, "Invalid parameter values");
            } catch (Exception e) {
                LOGGER.error("Spin request failed", e);
                Utils.sendHttpResponse(exchange, 500, "Bad request");
            }
        });
    }
}
