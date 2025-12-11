package relax.gaming.rnd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RndImplTest {
    @Test
    public void NoSeedTest(){
        long noSeed = 0;
        Rnd rnd = new RndRegular(0);
        assertNotEquals(noSeed, rnd.getSeed());
    }

    @Test
    public void SeededNextIntTest(){
        long seed = Long.parseLong("-2846572335491250255");
        Rnd rnd = new RndRegular(seed);
        assertEquals(0, rnd.nextInt(0, 2));
    }

    @Test
    public void SeededNextBoolTest(){
        long seed = Long.parseLong("-2846572335491250255");
        Rnd rnd = new RndRegular(seed);
        assertTrue(rnd.nextBool());
    }
}