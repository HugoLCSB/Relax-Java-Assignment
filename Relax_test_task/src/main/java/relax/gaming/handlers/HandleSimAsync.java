package relax.gaming.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import relax.gaming.core.Engine;
import relax.gaming.utils.Json;

import java.util.Deque;
import java.util.Map;

public class HandleSimAsync implements HttpHandler{
    private static final Logger LOGGER = LogManager.getLogger(HandleSimAsync.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) {

        exchange.dispatch(() -> {

            try {
                Map<String, Deque<String>> params = exchange.getQueryParameters();
                int spins = Integer.parseInt(params.get("spins").getFirst());
                int batchSize = Integer.parseInt(params.get("batchSize").getFirst());

                LOGGER.info("Received simulation request");

                Engine.doSimulationAsync(spins, batchSize)
                        .thenAccept(result -> {
                            try {
                                String json = Json.toJson(result);
                                exchange.getResponseHeaders().put(
                                        Headers.CONTENT_TYPE, "application/json"
                                );
                                exchange.getResponseSender().send(json);
                                LOGGER.info("Simulation completed.");
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
