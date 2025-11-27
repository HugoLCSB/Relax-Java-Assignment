package relax.gaming;

import java.util.Random;

/**
 * This class is the Game Engine,a heart of the back-end service responsible for the game rules and mechanics.
 */
public class Engine {
    public static final int REEL_AMOUNT = 8;
    public static final int ROW_AMOUNT = 8;
    public static final int BOARD_SIZE = REEL_AMOUNT * ROW_AMOUNT;
    private SymbolType[] symbolValues;
    private int[] spinWeights;
    private int spinWeightTotal;
    private int[] avalancheWeights;
    private int avalancheWeightTotal;

    public void init(){
        this.symbolValues = SymbolType.values();
        getCummulativeWeights();
    }

    private void getCummulativeWeights(){
        SymbolType[] types = SymbolType.values();
        this.spinWeights = new int[types.length];
        this.avalancheWeights = new int[types.length];
        int spinSum = 0;
        int avalancheSum = 0;
        for(int i = 0; i < types.length; i++){
            spinSum += types[i].spinWeight;
            this.spinWeights[i] = spinSum;

            avalancheSum += types[i].avalancheWeight;
            this.avalancheWeights[i] = avalancheSum;
        }
        this.spinWeightTotal = spinSum;
        this.avalancheWeightTotal = avalancheSum;
    }

    private SymbolType pick(int[] weights, int total, Random rnd){
        if(this.symbolValues.length == weights.length){
            int rand = rnd.nextInt(0, total);
            for(int i = 0; i < this.symbolValues.length; i++){
                if(rand <= weights[i]){
                    return this.symbolValues[i];
                }
            }
        }

        return null;
    }


    /**
     * Expects to be given a non-null grid object and populates every cell that's not populated yet.
     *
     * @param grid the given non-null 2d grid matrix
     * @param weights the weights used for the probabilities of each symbol
     * @param total the total of the weights
     * @param rnd the random number generator
     */
    private void populateGrid(SymbolType[][] grid, int[] weights, int total, Random rnd){
        if(grid != null){
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(grid[i][j] == null){
                        grid[i][j] = pick(weights, total, rnd);
                    }
                }
            }
        }
    }


    public void doSpin(long seed){
        Random rnd = new Random((seed != 0) ? seed : new Random().nextLong());
        SymbolType[][] grid = new SymbolType[REEL_AMOUNT][ROW_AMOUNT];

        //initial spin
        populateGrid(grid, this.spinWeights, this.spinWeightTotal, rnd);

        while(foundClusters()){
            //avalanche
            populateGrid(grid, this.avalancheWeights, this.avalancheWeightTotal, rnd);
        }
    }

    private boolean foundClusters(){


        return false;
    }

    public void doAvalanche(){

    }

    public void doSimulation(int n){

    }


}
