package relax.gaming.utils;

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
     * Formats grid into a string for easy use inside the logger.
     * @param grid the given grid
     * @return a formatted string containing the given grid
     */
    public static<T> String formatGrid(T[][] grid) {
        StringBuilder sb = new StringBuilder("\n");

        sb.append("\n");
        for(int j = 0; j < grid[0].length; j ++){
            for(int i = 0; i < grid.length; i++){
                T s = grid[i][j];
                sb.append(s == null ? "   " : String.format("%2s ", s));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
