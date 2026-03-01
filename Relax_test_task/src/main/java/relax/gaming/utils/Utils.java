package relax.gaming.utils;

import java.util.ArrayList;
import java.util.List;

import io.undertow.server.HttpServerExchange;
import relax.gaming.core.Coord;

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
     * Quick method that lets you do a response with a specific http status code in
     * a single line
     *
     * @param exchange http exchange object
     * @param code     the given code
     * @param message  the given message
     */
    public static void sendHttpResponse(HttpServerExchange exchange, int code, String message) {
        exchange.setStatusCode(code);
        exchange.getResponseSender().send(message);
    }

    /**
     * Transforms a 2d grid in a coord list where
     * every non null position is added as a coord to the list
     * 
     * @param <T>  the grid type
     * @param grid the given 2d grid
     * @return the coord list
     */
    public static <T> List<Coord> gridToList(T[][] grid) {
        List<Coord> res = new ArrayList<>();
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j] != null) {
                    res.add(new Coord(i, j));
                }
            }
        }

        return res;
    }
}
