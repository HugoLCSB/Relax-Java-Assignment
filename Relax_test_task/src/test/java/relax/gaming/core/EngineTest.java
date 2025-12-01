package relax.gaming.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import relax.gaming.config.ConfigManager;
import relax.gaming.utils.SingletonExecutor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {
    //TODO: should not depend on the config files for unit tests
    private static final String SYMBOL_FILE = "symbols.json";
    private static final String PAYOUT_FILE = "payouts.json";
    private static final String BUCKET_FILE = "clusterBuckets.json";

    private Engine engine;

    @BeforeEach
    void setup() {
        try {
            ConfigManager config = new ConfigManager(SYMBOL_FILE, PAYOUT_FILE, BUCKET_FILE);
            this.engine = new Engine(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void SeededSpinTest(){
        long seed = Long.parseLong("-6387813778980389556");
        GameRound round = this.engine.doSpin(seed, 10);
        assertEquals(3, round.steps().size());

        assertEquals(8, round.steps().get(0).getClusters().get(0).getSize());
        assertEquals(4.0, round.steps().get(0).getClusters().get(0).getPayout());

        assertEquals(9, round.steps().get(0).getClusters().get(1).getSize());
        assertEquals(6.0, round.steps().get(0).getClusters().get(1).getPayout());

        assertEquals(17.0, round.totalPayout());
    }

    @Test
    public void SeededGambleTest(){
        long seed = Long.parseLong("-2846572335491250255");
        GameRound round = this.engine.doGamble(seed, 10);
        assertNull(round.steps());
        assertEquals(20.0, round.totalPayout());
    }

    @Test
    public void SimTestAsync(){
        int spins = 10000;
        int batchSize = 100;
        CompletableFuture<SimulationResult> future = this.engine.doSimulationAsync(SingletonExecutor.SIMULATION_POOL, spins, batchSize);
        assertDoesNotThrow(() ->{
            SimulationResult result = future.get();
            assertEquals(spins, result.spins());
            assertTrue(result.rtp() > 0.5);
        });
    }
}
