package relax.gaming.utils;

import io.undertow.server.HttpServerExchange;

/**
 * This is the Utils class.
 */
public class Utils {
    /**
     * Performs a safe clone of a 2D array.
     *
     * @param toCopy the array to copy from
     * @param <T>    the array type
     * @return the clone
     */
    public static <T> T[][] deepClone(T[][] toCopy) {
        T[][] clone = toCopy.clone();
        for (int i = 0; i < toCopy.length; i++) {
            clone[i] = toCopy[i].clone();
        }
        return clone;
    }

    /**
     * Formats grid into a string for easy use inside the logger.
     *
     * @param grid the given grid
     * @return a formatted string containing the given grid
     */
    public static <T> String formatGrid(T[][] grid) {
        StringBuilder sb = new StringBuilder("\n");

        sb.append("\n");
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                T s = grid[i][j];
                sb.append(s == null ? "   " : String.format("%2s ", s));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Quick method that lets you do a response with a specific http status code in a single line
     *
     * @param exchange http exchange object
     * @param code     the given code
     * @param message  the given message
     */
    public static void sendHttpResponse(HttpServerExchange exchange, int code, String message) {
        exchange.setStatusCode(code);
        exchange.getResponseSender().send(message);
    }
}
