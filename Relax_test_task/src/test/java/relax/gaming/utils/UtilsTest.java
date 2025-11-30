package relax.gaming.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import relax.gaming.config.ConfigManager;
import relax.gaming.config.Symbol;
import relax.gaming.core.GridGenerator;
import relax.gaming.rnd.Rnd;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    private static final String SYMBOL_FILE = "symbols.json";
    private static final String PAYOUT_FILE = "payouts.json";
    private static final String BUCKET_FILE = "clusterBuckets.json";

    private ConfigManager config;

    @BeforeEach
    void setup() {
        this.config = new ConfigManager(SYMBOL_FILE, PAYOUT_FILE, BUCKET_FILE);
    }

    @Test
    public void DeepCloneTest(){
        Rnd rnd = new Rnd(0);
        Symbol[][] grid = GridGenerator.generateGrid(rnd, 8,8,
                config.getSymbolConfig().spinOptions(), config.getSymbolConfig().spinWeights());
        Symbol[][] clone = Utils.deepClone(grid);

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                assertEquals(grid[i][j], clone[i][j]);
            }
        }

        grid[0][0] = null;
        assertNotNull(clone[0][0]);
    }
}