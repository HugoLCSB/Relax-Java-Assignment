package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.SymbolType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
