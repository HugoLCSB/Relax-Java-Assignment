package relax.gaming.core;

import relax.gaming.config.Symbol;
import relax.gaming.rnd.Rnd;
import relax.gaming.utils.Utils;

import java.util.List;

public class GridGenerator {
    /**
     * Given an array of options and a same size array of cumulative weights
     * picks a random option according to said weights.
     *
     * @param options list of options to pick from
     * @param weights cumulative list of weights for each option
     * @return the randomly chosen option
     * @param <T> the type of the option
     */
    public static<T> T pick(Rnd rnd, List<T> options, List<Integer> weights){
        if(options == null || weights == null || options.size() != weights.size()){
            throw new IllegalArgumentException("Options and Weights list must be non null and of same size");
        }
        //in a cumulative list the last entry is the total
        int total = weights.get(weights.size()-1);
        int rand = rnd.nextInt(0, total);
        for(int i = 0; i < options.size(); i++){
            if(rand <= weights.get(i)){
                return options.get(i);
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
     * @param options list of options to pick from
     * @param weights the weights used for the probabilities of each symbol
     * @return the new fully populated grid
     */
    public static Symbol[][] generateGrid(Rnd rnd, int reels, int rows, List<Symbol> options, List<Integer> weights){
        if(options == null || weights == null || options.size() != weights.size()){
            throw new IllegalArgumentException("Options and Weights list must be non null and of same size");
        }
        Symbol[][] grid = new Symbol[reels][rows];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = pick(rnd, options, weights);
            }
        }
        return grid;
    }

    /**
     * Expects to be given a non-null grid object and populates every cell that's not populated yet.
     *
     * @param rnd the random number generator
     * @param grid the given non-null 2d grid matrix
     * @param options list of options to pick from
     * @param weights the weights used for the probabilities of each symbol
     * @return the new fully populated grid
     */
    public static Symbol[][] populateGrid(Rnd rnd, Symbol[][] grid, List<Symbol> options, List<Integer> weights){
        if(grid == null){return null;}
        if(options == null || weights == null || options.size() != weights.size()){
            throw new IllegalArgumentException("Options and Weights list must be non null and of same size");
        }
        Symbol[][] newGrid = Utils.deepClone(grid);
        if(newGrid != null){
            for(int i = 0; i < newGrid.length; i++){
                for(int j = 0; j < newGrid[0].length; j++){
                    if(newGrid[i][j] == null){
                        newGrid[i][j] = pick(rnd, options, weights);
                    }
                }
            }
        }
        return newGrid;
    }
}
