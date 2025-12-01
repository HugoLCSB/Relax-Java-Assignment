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

public class HandleSimAsync implements HttpHandler{
    private static final Logger LOGGER = LogManager.getLogger(HandleSimAsync.class);
    private final Engine engine;

    public HandleSimAsync(Engine engine) {
        if(engine == null){
            throw new IllegalArgumentException("Engine can't be null");
        }
        this.engine = engine;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.dispatch(() -> {
            try {
                Map<String, Deque<String>> params = exchange.getQueryParameters();
                String spinsParam = params.get("spins") != null ? params.get("spins").getFirst() : null;
                String batchSizeParam = params.get("batchSize") != null ? params.get("batchSize").getFirst() : null;

                if(spinsParam == null){
                    LOGGER.warn("Missing required parameter spins");
                    Utils.sendHttpResponse(exchange, 400, "Missing required parameter spins");
                    return;
                }

                int spins = Integer.parseInt(spinsParam);
                int batchSize = batchSizeParam != null ? Integer.parseInt(batchSizeParam) : 1;

                LOGGER.info("Received simulation request");
                this.engine.doSimulationAsync(SingletonExecutor.SIMULATION_POOL, spins, batchSize)
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
                        })
                        .exceptionally(e -> {
                            LOGGER.error("Error doing simulation execution", e);
                            Utils.sendHttpResponse(exchange, 500, "Internal error");
                            return null;
                        });
            }catch(NumberFormatException e){
                LOGGER.error("Invalid parameter values", e);
                Utils.sendHttpResponse(exchange, 400, "Invalid parameter values");
            } catch (Exception e) {
                LOGGER.error("Spin request failed", e);
                Utils.sendHttpResponse(exchange, 400, "Bad request");
            }
        });
    }
}
