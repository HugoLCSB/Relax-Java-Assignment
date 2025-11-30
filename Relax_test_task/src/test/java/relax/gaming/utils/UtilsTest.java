package relax.gaming.utils;

import org.junit.jupiter.api.Test;
import relax.gaming.config.SymbolType;
import relax.gaming.core.GridGenerator;
import relax.gaming.rnd.Rnd;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @Test
    public void DeepCloneTest(){
        Rnd rnd = new Rnd(0);
        SymbolType[][] grid = GridGenerator.generateGrid(rnd, 8,8, SymbolType.getSpinWeights());
        SymbolType[][] clone = Utils.deepClone(grid);

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                assertEquals(grid[i][j], clone[i][j]);
            }
        }

        grid[0][0] = null;
        assertNotNull(clone[0][0]);
    }
}