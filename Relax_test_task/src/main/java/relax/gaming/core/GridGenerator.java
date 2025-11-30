package relax.gaming.core;

import relax.gaming.config.SymbolType;
import relax.gaming.rnd.Rnd;
import relax.gaming.utils.Utils;

public class GridGenerator {
    /**
     * Given an array of options and a same size array of cumulative weights
     * picks a random option according to said weights.
     *
     * @param options array of options to pick from
     * @param weights cumulative array of weights for each option
     * @return the randomly chosen option
     * @param <T> the type of the option
     */
    public static<T> T pick(Rnd rnd, T[] options, int[] weights){
        //TODO: check arrays of same size
        if(options.length == weights.length){
            //in a cumulative list the last entry is the total
            int total = weights[weights.length-1];
            int rand = rnd.nextInt(0, total);
            for(int i = 0; i < options.length; i++){
                if(rand <= weights[i]){
                    return options[i];
                }
            }
        }

        return null;
    }

    /**
     * Generates a grid with randomized values according to the options and weights.
     *
     * @param rnd the random number generator
     * @param reels number of reels
     * @param rows number of rows
     * @param weights the weights used for the probabilities of each symbol
     * @return the new fully populated grid
     */
    public static SymbolType[][] generateGrid(Rnd rnd, int reels, int rows, int[] weights){
        SymbolType[][] grid = new SymbolType[reels][rows];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = pick(rnd, SymbolType.values(), weights);
            }
        }
        return grid;
    }

    /**
     * Expects to be given a non-null grid object and populates every cell that's not populated yet.
     *
     * @param rnd the random number generator
     * @param grid the given non-null 2d grid matrix
     * @param weights the weights used for the probabilities of each symbol
     * @return the new fully populated grid
     */
    public static SymbolType[][] populateGrid(Rnd rnd, SymbolType[][] grid, int[] weights){
        if(grid == null){return null;}
        SymbolType[][] newGrid = Utils.deepClone(grid);
        if(newGrid != null){
            for(int i = 0; i < newGrid.length; i++){
                for(int j = 0; j < newGrid[0].length; j++){
                    if(newGrid[i][j] == null){
                        newGrid[i][j] = pick(rnd, SymbolType.values(), weights);
                    }
                }
            }
        }
        return newGrid;
    }
}
