package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.Symbol;
import relax.gaming.utils.Utils;

import static org.junit.jupiter.api.Assertions.*;

public class GameStepTest {
    private static final int MIN_CLUSTER_SIZE = 5;

    @Test
    public void TestSingleClusterDetection(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        grid[0][0] = testSymbol;
        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;
        grid[0][4] = testSymbol;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();
        assertEquals(1, step.getClusters().size());
        assertEquals(5, step.getClusters().get(0).getSize());
        //assertEquals(5.0, step.getStepPayout());
    }

    @Test
    public void TestDestroySingleCluster(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        grid[0][0] = testSymbol;
        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;
        grid[0][4] = testSymbol;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();
        assertNull(step.getGridAfterDestroy()[0][0]);
        assertNull(step.getGridAfterDestroy()[0][1]);
        assertNull(step.getGridAfterDestroy()[0][2]);
        assertNull(step.getGridAfterDestroy()[0][3]);
        assertNull(step.getGridAfterDestroy()[0][4]);
    }

    @Test
    public void TestGravitySingleCluster(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbolDiff = new Symbol("L8", 0, 0, false, false);
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);

        grid[0][0] = testSymbolDiff;

        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;
        grid[0][4] = testSymbol;
        grid[0][5] = testSymbol;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();
        assertEquals(testSymbolDiff, step.getGridAfterGravity()[0][7]);
    }

    @Test
    public void TestFullClusterDetection(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = testSymbol;
            }
        }

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();
        assertEquals(1, step.getClusters().size());
        assertEquals(64, step.getClusters().get(0).getSize());
    }

    @Test
    public void TestDestroyIgnoredNeighbors(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        Symbol testBlocker = new Symbol("BL", 0, 0, false, true);

        grid[0][0] = testSymbol;
        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;
        grid[0][4] = testSymbol;

        grid[0][5] = testBlocker;
        grid[0][6] = testBlocker;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();

        assertNull(step.getGridAfterDestroy()[0][5]);
        assertNotNull(step.getGridAfterDestroy()[0][6]);
    }

    @Test
    public void TestWildCards(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        Symbol testSymbol2 = new Symbol("H2", 0, 0, false, false);
        Symbol testWR = new Symbol("WR", 0, 0, true, false);

        grid[0][0] = testSymbol;
        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;

        grid[0][4] = testWR;

        grid[1][4] = testSymbol2;
        grid[2][4] = testSymbol2;
        grid[3][4] = testSymbol2;
        grid[4][4] = testSymbol2;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();

        assertEquals(2, step.getClusters().size());
        for(Cluster cluster : step.getClusters()){
            assertEquals(5, cluster.getSize());
        }
    }

    @Test
    public void TestSingleClusterGrid(){
        Symbol[][] grid = new Symbol[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        grid[0][0] = testSymbol;
        grid[0][1] = testSymbol;
        grid[0][2] = testSymbol;
        grid[0][3] = testSymbol;
        grid[0][4] = testSymbol;

        GameStep step = new GameStep(grid, MIN_CLUSTER_SIZE);
        step.compute();
        assertEquals(Utils.formatGrid(grid), Utils.formatGrid(step.getGrid()));
    }
}
