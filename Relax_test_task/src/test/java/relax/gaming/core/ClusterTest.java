package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.Symbol;

import static org.junit.jupiter.api.Assertions.*;

class ClusterTest {
    @Test
    public void AddingWRTest(){
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        Symbol testWR = new Symbol("WR", 0, 0, true, false);

        Cluster cluster = new Cluster(testSymbol);
        assertTrue(cluster.addIfValid(testWR, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
    }

    @Test
    public void AddingBLTest(){
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        Symbol testBL = new Symbol("BL", 0, 0, false, true);

        Cluster cluster = new Cluster(testSymbol);
        assertFalse(cluster.addIfValid(testBL, new Coord(0,0)));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingDifferentTest(){
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);
        Symbol testSymbol2 = new Symbol("H2", 0, 0, false, false);

        Cluster cluster = new Cluster(testSymbol);
        assertFalse(cluster.addIfValid(testSymbol2, new Coord(0,0)));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingSameTypeTest(){
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);

        Cluster cluster = new Cluster(testSymbol);
        assertTrue(cluster.addIfValid(testSymbol, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
        assertEquals(0, cluster.getCoords().get(0).reel());
        assertEquals(0, cluster.getCoords().get(0).row());
    }

    @Test
    public void AddingDupTest(){
        Symbol testSymbol = new Symbol("H1", 0, 0, false, false);

        Cluster cluster = new Cluster(testSymbol);
        cluster.addIfValid(testSymbol, new Coord(0,0));
        assertFalse(cluster.addIfValid(testSymbol, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
    }
}