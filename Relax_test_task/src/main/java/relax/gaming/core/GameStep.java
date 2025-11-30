package relax.gaming.core;

import org.apache.logging.log4j.LogManager;
import relax.gaming.utils.Utils;
import relax.gaming.config.ClusterBucket;
import relax.gaming.config.SymbolType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class GameStep {
    private static final Logger LOGGER = LogManager.getLogger(GameStep.class);
    private final SymbolType[][] grid;
    private final double bet;
    private final List<Cluster> clusters;
    private SymbolType[][] gridAfterDestroy;
    private SymbolType[][] gridAfterGravity;
    private double winAmount;

    public GameStep(SymbolType[][] grid, double bet) {
        this.grid = Utils.deepClone(grid);
        this.bet = bet;
        this.clusters = new ArrayList<>();
        this.winAmount = 0;
    }

    public SymbolType[][] getGrid(){
        return this.grid;
    }

    public List<Cluster> getClusters(){
        return this.clusters;
    }

    public SymbolType[][] getGridAfterDestroy(){
        return this.gridAfterDestroy;
    }

    public SymbolType[][] getGridAfterGravity(){
        return this.gridAfterGravity;
    }

    public double getStepPayout(){
        return this.winAmount;
    }

    public boolean hasClusters(){
        return !this.clusters.isEmpty();
    }

    public void compute(){
        LOGGER.debug("Starting Grid: {}", Utils.formatGrid(this.grid));
        findClusters();
        if(hasClusters()){
            destroyGrid();
            LOGGER.debug("Grid after destroy: {}", Utils.formatGrid(this.gridAfterDestroy));
            applyGravity();
            LOGGER.debug("Grid after gravity: {}",Utils.formatGrid(this.gridAfterGravity));
        }
    }

    private void findClusters(){
        boolean[][] visited = new boolean[this.grid.length][this.grid.length];

        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid.length; j++){
                if(visited[i][j]){continue;}

                //ignored and wildcards don't form clusters
                if(SymbolType.isIgnored(this.grid[i][j]) || SymbolType.isWildCard(this.grid[i][j])){
                    visited[i][j] = true;
                    continue;
                }

                Cluster newCluster = new Cluster(this.grid[i][j]);
                clusterSearch(i, j, visited, newCluster);
                processNewCluster(newCluster);
            }
        }
    }

    private void clusterSearch(int i, int j, boolean[][] visited, Cluster cluster){
        if(!(i >= 0 && i < this.grid.length
                && j >= 0 && j < this.grid.length)) {return;}

        if(SymbolType.isIgnored(this.grid[i][j])){
            cluster.toDestroy().add(new Coord(i, j));
            return;
        }

        if(!visited[i][j] || SymbolType.isWildCard(this.grid[i][j])){
            if(cluster.add(this.grid[i][j], new Coord(i, j))) {
                visited[i][j] = true;
                cluster.toDestroy().add(new Coord(i, j));

                clusterSearch(i-1, j, visited, cluster);
                clusterSearch(i+1, j, visited, cluster);
                clusterSearch(i, j-1, visited, cluster);
                clusterSearch(i, j+1, visited, cluster);
            }
        }
    }

    private void processNewCluster(Cluster newCluster){
        if(ClusterBucket.getBucket(newCluster.getSize()) != ClusterBucket.NONE){
            this.winAmount += newCluster.calculateWin(this.bet);
            this.clusters.add(newCluster);
            LOGGER.debug("Found Cluster of {}, with size {}, payout {}€",
                    newCluster.getType(), newCluster.getSize(), newCluster.getPayout());
        }
    }

    private void destroyGrid(){
        this.gridAfterDestroy = Utils.deepClone(this.grid);

        Set<Coord> toDestroy = new HashSet<>();
        for(Cluster cluster : this.clusters){
            toDestroy.addAll(cluster.toDestroy());
        }

        for(Coord coord : toDestroy){
            this.gridAfterDestroy[coord.reel()][coord.row()] = null;
        }
    }

    private void applyGravity(){
        this.gridAfterGravity = Utils.deepClone(this.gridAfterDestroy);
        SymbolType[] buffReel;
        int counter;
        for(int i = 0; i < this.gridAfterGravity.length; i++){
            buffReel = new SymbolType[this.gridAfterGravity.length];
            counter = this.gridAfterGravity.length-1;
            for(int j = this.gridAfterGravity.length-1; j >= 0; j--){
                SymbolType curr = this.gridAfterGravity[i][j];
                if(curr != null){
                    buffReel[counter] = curr;
                    counter--;
                    this.gridAfterGravity[i][j] = null;
                }
            }
            this.gridAfterGravity[i] = buffReel;
        }
    }
}
