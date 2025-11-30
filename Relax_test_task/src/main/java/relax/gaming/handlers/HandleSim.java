package relax.gaming.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.core.Engine;
import relax.gaming.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HandleSim implements HttpHandler {
    private static final Logger LOGGER = LogManager.getLogger(HandleSim.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.debug("Got new request /sim");
        if (!exchange.getRequestMethod().equals("POST")){
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String query = exchange.getRequestURI().getQuery();

        Map<String, String> params = Utils.queryStringToMap(query);
        int spins = Integer.parseInt(params.getOrDefault("spins", "1000000"));


        String rtpMsg = Engine.doSimulation(spins);

        try{
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rtpMsg);
            byte[] response = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }
}
