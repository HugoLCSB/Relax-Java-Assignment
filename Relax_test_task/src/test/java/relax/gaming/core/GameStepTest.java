package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.SymbolType;
import relax.gaming.utils.Utils;

import static org.junit.jupiter.api.Assertions.*;

public class GameStepTest {
    @Test
    public void TestSingleClusterDetection(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.H1;
        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;
        grid[0][4] = SymbolType.H1;

        GameStep step = new GameStep(grid, 10);
        step.compute();
        assertEquals(1, step.getClusters().size());
        assertEquals(5, step.getClusters().get(0).getSize());
        assertEquals(5.0, step.getStepPayout());
    }

    @Test
    public void TestDestroySingleCluster(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.H1;
        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;
        grid[0][4] = SymbolType.H1;

        GameStep step = new GameStep(grid, 10);
        step.compute();
        assertNull(step.getGridAfterDestroy()[0][0]);
        assertNull(step.getGridAfterDestroy()[0][1]);
        assertNull(step.getGridAfterDestroy()[0][2]);
        assertNull(step.getGridAfterDestroy()[0][3]);
        assertNull(step.getGridAfterDestroy()[0][4]);
    }

    @Test
    public void TestGravitySingleCluster(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.L8;

        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;
        grid[0][4] = SymbolType.H1;
        grid[0][5] = SymbolType.H1;

        GameStep step = new GameStep(grid, 10);
        step.compute();
        assertEquals(SymbolType.L8, step.getGridAfterGravity()[0][7]);
    }

    @Test
    public void TestFullClusterDetection(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = SymbolType.H1;
            }
        }

        GameStep step = new GameStep(grid, 10);
        step.compute();
        assertEquals(1, step.getClusters().size());
        assertEquals(64, step.getClusters().get(0).getSize());
    }

    @Test
    public void TestDestroyIgnoredNeighbors(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.H1;
        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;
        grid[0][4] = SymbolType.H1;

        grid[0][5] = SymbolType.BL;
        grid[0][6] = SymbolType.BL;

        GameStep step = new GameStep(grid, 10);
        step.compute();

        assertNull(step.getGridAfterDestroy()[0][5]);
        assertNotNull(step.getGridAfterDestroy()[0][6]);
    }

    @Test
    public void TestWildCards(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.H1;
        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;

        grid[0][4] = SymbolType.WR;

        grid[1][4] = SymbolType.H2;
        grid[2][4] = SymbolType.H2;
        grid[3][4] = SymbolType.H2;
        grid[4][4] = SymbolType.H2;

        GameStep step = new GameStep(grid, 10);
        step.compute();

        assertEquals(2, step.getClusters().size());
        for(Cluster cluster : step.getClusters()){
            assertEquals(5, cluster.getSize());
        }
    }

    @Test
    public void TestSingleClusterGrid(){
        SymbolType[][] grid = new SymbolType[Engine.REEL_AMOUNT][Engine.ROW_AMOUNT];
        grid[0][0] = SymbolType.H1;
        grid[0][1] = SymbolType.H1;
        grid[0][2] = SymbolType.H1;
        grid[0][3] = SymbolType.H1;
        grid[0][4] = SymbolType.H1;

        GameStep step = new GameStep(grid, 10);
        step.compute();
        assertEquals(Utils.formatGrid(grid), Utils.formatGrid(step.getGrid()));
    }
}
