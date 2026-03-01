package relax.gaming.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.config.Symbol;
import relax.gaming.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a GameStep, which is a single step in a GameRound.
 * A GameStep is composed of one round of clusterSearch one round of destroying
 * the found clusters
 * and one round of applying gravity, to make up a single avalanche.
 */
public class GameStep {
    private static final Logger LOGGER = LogManager.getLogger(GameStep.class);
    private final Symbol[][] grid;
    private final List<Cluster> clusters;
    private Symbol[][] gridAfterDestroy;
    private Symbol[][] gridAfterGravity;
    private final int minClusterSize;
    private double stepPayout;
    private boolean shouldPrint;

    public GameStep(Symbol[][] grid, int minClusterSize, boolean shouldPrint) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid must not be null");
        }
        this.grid = Utils.deepClone(grid);
        this.clusters = new ArrayList<>();
        this.minClusterSize = minClusterSize;
        this.stepPayout = 0;
        this.shouldPrint = shouldPrint;
    }

    public Symbol[][] getGrid() {
        return this.grid;
    }

    public List<Cluster> getClusters() {
        return this.clusters;
    }

    public Symbol[][] getGridAfterDestroy() {
        return this.gridAfterDestroy;
    }

    public Symbol[][] getGridAfterGravity() {
        return this.gridAfterGravity;
    }

    public double getStepPayout() {
        return this.stepPayout;
    }

    public void setStepPayout(double stepPayout) {
        this.stepPayout = stepPayout;
    }

    public boolean hasClusters() {
        return !this.clusters.isEmpty();
    }

    /**
     * Executes the GameStep.
     */
    public void compute() {
        try {
            logGridState("Starting Grid: {}", this.grid);
            findClusters();
            if (hasClusters()) {
                destroyGrid();
                logGridState("Grid after destroy: {}", this.gridAfterDestroy);

                applyGravity();
                logGridState("Grid after gravity: {}", this.gridAfterGravity);
            }
        } catch (Exception e) {
            LOGGER.error("Error while game step computation", e);
            throw e;
        }
    }

    private void logGridState(String msg, Symbol[][] grid) {
        if (LOGGER.isDebugEnabled() && this.shouldPrint) {
            LOGGER.debug(msg, Utils.formatGrid(grid));
        }
    }

    /**
     * Finds clusters using a DFS approach where the 4 neighbors
     * (up/down/left/right)
     * are explored before backtracking, if same type found, recursively explore
     * those
     * neighbors and so on, until there are no same type neighbors or the grid is
     * totally visited.
     */
    private void findClusters() {
        boolean[][] visited = new boolean[this.grid.length][this.grid.length];

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                if (visited[i][j]) {
                    continue;
                }

                // blockers and wildcards don't form clusters
                if (grid[i][j] == null
                        || this.grid[i][j].isBlocker()
                        || this.grid[i][j].isWildCard()) {
                    visited[i][j] = true;
                    continue;
                }

                Cluster newCluster = new Cluster(this.grid[i][j], this.grid.length, this.grid[0].length);
                clusterSearch(i, j, visited, newCluster);
                processNewCluster(newCluster);
            }
        }
    }

    /**
     * Recursive search to find same type neighbors.
     *
     * @param i       reel of the current position
     * @param j       row of the current position
     * @param visited list of all visited positions
     * @param cluster the current cluster being explored
     */
    private void clusterSearch(int i, int j, boolean[][] visited, Cluster cluster) {
        if (!withinBounds(i, j) || grid[i][j] == null) {
            return;
        }

        Symbol type = grid[i][j];

        // manually add blockers to the destroy list
        if (type.isBlocker()) {
            cluster.addToDestroy(i, j);
            return;
        }

        if ((!visited[i][j] || type.isWildCard()) && cluster.addIfValid(type, i, j)) {
            visited[i][j] = true;

            clusterSearch(i - 1, j, visited, cluster);
            clusterSearch(i + 1, j, visited, cluster);
            clusterSearch(i, j - 1, visited, cluster);
            clusterSearch(i, j + 1, visited, cluster);
        }
    }

    /**
     * Check if position is within grid bounds
     *
     * @param i reel of the current position
     * @param j row of the current position
     * @return true if within the grid bounds
     */
    private boolean withinBounds(int i, int j) {
        return (i >= 0 && i < this.grid.length
                && j >= 0 && j < this.grid[0].length);
    }

    /**
     * Adds new cluster to the GameStep found list if given
     * cluster is of valid size.
     *
     * @param newCluster the cluster to be added
     */
    private void processNewCluster(Cluster newCluster) {
        if (newCluster.getSize() >= this.minClusterSize) {
            this.clusters.add(newCluster);
            if (shouldPrint)
                LOGGER.debug("Found Cluster of {}, with size {}",
                        newCluster.getType(), newCluster.getSize());
        }
    }

    /**
     * Generates a clone of the grid where the clusters
     * previously found are destroyed
     */
    private void destroyGrid() {
        this.gridAfterDestroy = Utils.deepClone(this.grid);

        long allToDestroy = 0L;
        for (Cluster cluster : this.clusters) {
            // merging the longs is done with a simple bitwise OR
            allToDestroy |= cluster.toDestroy();
        }

        // Loop exactly as many times as there are 1s in the bitboard
        while (allToDestroy != 0L) {

            // Find the position of the the first '1' from the right
            int index = Long.numberOfTrailingZeros(allToDestroy);

            // Convert the flat index back to 2D coords
            // Assuming original math was: index = (reel * TOTAL_ROWS) + row
            int reel = index / this.grid[0].length;
            int row = index % this.grid[0].length;

            this.gridAfterDestroy[reel][row] = null;

            // Clear that lowest set bit so the loop can move to the next one
            allToDestroy &= (allToDestroy - 1L);
        }
    }

    /**
     * Generates a clone of the grid where positions that don't have a bottom
     * neighbor that's not null will be moved down as if gravity was applied.
     */
    private void applyGravity() {
        this.gridAfterGravity = Utils.deepClone(this.gridAfterDestroy);
        Symbol[] buffReel;
        int counter;
        for (int i = 0; i < this.gridAfterGravity.length; i++) {
            buffReel = new Symbol[this.gridAfterGravity.length];
            counter = this.gridAfterGravity.length - 1;
            for (int j = this.gridAfterGravity.length - 1; j >= 0; j--) {
                Symbol curr = this.gridAfterGravity[i][j];
                if (curr != null) {
                    buffReel[counter] = curr;
                    counter--;
                    this.gridAfterGravity[i][j] = null;
                }
            }
            this.gridAfterGravity[i] = buffReel;
        }
    }
}
