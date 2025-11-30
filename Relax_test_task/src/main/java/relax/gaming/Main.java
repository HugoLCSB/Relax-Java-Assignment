package relax.gaming;

import com.sun.net.httpserver.HttpServer;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.handlers.HandleSim;
import relax.gaming.handlers.HandleSpin;
import relax.gaming.handlers.HandleSpinAsync;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        System.out.println();

        //syncServer();
        asyncServer();
    }

    public static void syncServer(){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/spin", new HandleSpin());
            server.createContext("/sim", new HandleSim());

            server.setExecutor(null);
            server.start();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncServer() {
        Undertow server = Undertow.builder()
            .addHttpListener(8080, "localhost")
            .setHandler(
                    Handlers.path()
                            .addPrefixPath("/spin", new HandleSpinAsync())
            )
            .build();
        server.start();
    }
}