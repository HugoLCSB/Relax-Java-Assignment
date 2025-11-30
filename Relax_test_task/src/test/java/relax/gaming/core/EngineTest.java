package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.utils.SingletonExecutor;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {

    @Test
    public void SeededSpinTest(){
        long seed = Long.parseLong("-6387813778980389556");
        Round round = Engine.doSpin(seed, 10);
        assertEquals(2, round.steps().size());

        assertEquals(5, round.steps().get(0).getClusters().get(0).getSize());
        assertEquals(3.0, round.steps().get(0).getClusters().get(0).getPayout());

        assertEquals(6, round.steps().get(0).getClusters().get(1).getSize());
        assertEquals(1.0, round.steps().get(0).getClusters().get(1).getPayout());

        assertEquals(4.0, round.totalPayout());
    }

    @Test
    public void SeededGambleTest(){
        long seed = Long.parseLong("-2846572335491250255");
        Round round = Engine.doGamble(seed, 10);
        assertNull(round.steps());
        assertEquals(20.0, round.totalPayout());
    }

    @Test
    public void SimTestAsync(){
        int spins = 100000;
        int batchSize = 100;
        CompletableFuture<SimulationResult> future = Engine.doSimulationAsync(SingletonExecutor.SIMULATION_POOL, spins, batchSize);
        assertDoesNotThrow(() ->{
            SimulationResult result = future.get();
            assertEquals(spins, result.spins());
            assertTrue(result.rtp() > 0.5);
        });
    }

    @Test
    public void SimTest(){
        int spins = 100000;
        SimulationResult result = Engine.doSimulation(spins);
        assertEquals(spins, result.spins());
        assertTrue(result.rtp() > 0.5);
    }
}
