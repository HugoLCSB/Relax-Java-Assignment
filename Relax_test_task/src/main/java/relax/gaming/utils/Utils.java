package relax.gaming.utils;

import relax.gaming.config.SymbolType;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    /**
     * Performs a safe clone of a 2D array.
     * @param toCopy the array to copy from
     * @return the clone
     * @param <T> the array type
     */
    public static<T> T[][] deepClone(T[][] toCopy){
        T[][] clone = toCopy.clone();
        for(int i = 0; i < toCopy.length; i++){
            clone[i] = toCopy[i].clone();
        }
        return clone;
    }

    /**
     * Prints a 2d grid with spaces between the cells and spaces
     * in place of null entries.
     * @param grid the given grid
     * @param <T> the type of the grid elements
     */
    public static<T> void printGrid(T[][] grid){
        for(int j = 0; j < grid.length; j ++){
            for(int i = 0; i < grid.length; i++){
                String symbol = grid[i][j] != null ? grid[i][j].toString() : "  ";
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String formatGrid(SymbolType[][] grid) {
        StringBuilder sb = new StringBuilder("\n");

        sb.append("\n");
        for(int j = 0; j < grid[0].length; j ++){
            for(int i = 0; i < grid.length; i++){
                SymbolType s = grid[i][j];
                sb.append(s == null ? "   " : String.format("%2s ", s));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Splitting query params into a neat map.
     * @param query the query params in a string
     * @return the map with the parameters found.
     */
    public static Map<String, String> queryStringToMap(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;

        for (String param : query.split("&")) {
            String[] parts = param.split("=", 2);
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }
}
