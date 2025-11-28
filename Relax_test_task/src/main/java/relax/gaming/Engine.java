package relax.gaming;

import java.util.ArrayList;
import java.util.List;
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

    public Engine(){
        init();
    }

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


    public void doSpin(long seed, double bet){
        double money = 0;
        if(seed == 0){
            seed = new Random().nextLong();
        }
        System.out.println("Spin with seed: " + seed + "\n");
        Random rnd = new Random(seed);
        SymbolType[][] grid = new SymbolType[REEL_AMOUNT][ROW_AMOUNT];

        //initial spin
        populateGrid(grid, this.spinWeights, this.spinWeightTotal, rnd);

        printGrid(grid);

        List<Cluster> clusters = findClusters(grid);
        money += printClusterMoney(clusters, bet);
        while(!clusters.isEmpty()){
            doAvalanche(grid, clusters);

            //add to response step with payout

            populateGrid(grid, this.avalancheWeights, this.avalancheWeightTotal, rnd);
            printGrid(grid);
            clusters = findClusters(grid);
            money += printClusterMoney(clusters, bet);
        }

        System.out.println("Total payout for the round is: " + money + " €");
        //give out json response with all the steps
    }

    private List<Cluster> findClusters(SymbolType[][] grid){
        List<Cluster> found = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid.length];

        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                if(!visited[i][j]){
                    //ignored and wildcards don't form clusters
                    if(SymbolType.isIgnored(grid[i][j]) || SymbolType.isWildCard(grid[i][j])){
                        visited[i][j] = true;
                        continue;
                    }

                    //start new cluster
                    Cluster newCluster = new Cluster(grid[i][j]);
                    clusterSearch(i, j, grid, visited, newCluster);
                    if(ClusterBucket.getBucket(newCluster.getSize()) != ClusterBucket.NONE){
                        found.add(newCluster);
                    }
                }
            }
        }

        return found;
    }

    private void clusterSearch(int i, int j, SymbolType[][] grid, boolean[][] visited, Cluster cluster){
        if(i >= 0 && i < grid.length
                && j >= 0 && j < grid.length
                && ((!visited[i][j])
                    //need to include ignored and wildcards in the cluster so they get destroyed along with it
                    || SymbolType.isIgnored(grid[i][j]) || SymbolType.isWildCard(grid[i][j]))){

            if(cluster.add(grid[i][j], new Coord(i,j))) {
                visited[i][j] = true;

                clusterSearch(i-1, j, grid, visited, cluster);
                clusterSearch(i+1, j, grid, visited, cluster);
                clusterSearch(i, j-1, grid, visited, cluster);
                clusterSearch(i, j+1, grid, visited, cluster);
            }
        }
    }

    private void destroyGrid(SymbolType[][] grid, List<Cluster> clusters){
        for(Cluster cluster : clusters){
            for(Coord coord : cluster.getCoords()){
                grid[coord.reel()][coord.row()] = null;
            }
        }

    }

    private void applyGravity(SymbolType[][] grid){
        SymbolType[] buffReel;
        int counter;
        for(int i = 0; i < grid.length; i++){
            buffReel = new SymbolType[grid.length];
            counter = grid.length-1;
            for(int j = grid.length-1; j >= 0; j--){
                SymbolType curr = grid[i][j];
                if(curr != null){
                    buffReel[counter] = curr;
                    counter--;
                    grid[i][j] = null;
                }
            }
            grid[i] = buffReel;
        }
    }

    private void doAvalanche(SymbolType[][] grid, List<Cluster> clusters){
        destroyGrid(grid, clusters);
        printGrid(grid);
        applyGravity(grid);
        printGrid(grid);
    }

    private void printGrid(SymbolType[][] grid){
        for(int j = 0; j < grid.length; j ++){
            for(int i = 0; i < grid.length; i++){
                String symbol = grid[i][j] != null ? grid[i][j].toString() : "  ";
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private double printClusterMoney(List<Cluster> clusters, double bet){
        double total = 0.0;
        for(Cluster cluster : clusters){
            double payout = Payouts.getPayout(cluster.getType(), cluster.getSize(), bet);
            total += payout;
            System.out.println("Cluster " + cluster.getType() + " of size: " + cluster.getSize() + " elegible for payout of: " + payout + "€");
        }
        System.out.println();
        return total;
    }

    public void doSimulation(int n){

    }
}
