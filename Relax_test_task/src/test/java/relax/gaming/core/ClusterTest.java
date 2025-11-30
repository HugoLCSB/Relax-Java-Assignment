package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.SymbolType;

import static org.junit.jupiter.api.Assertions.*;

class ClusterTest {
    @Test
    public void AddingNullTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertDoesNotThrow(() ->{
            cluster.addIfValid(null, null, false);
        });

        assertEquals(0, cluster.getSize());

        boolean output = cluster.addIfValid(null, null, false);
        assertFalse(output);
    }

    @Test
    public void AddingWRTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertTrue(cluster.addIfValid(SymbolType.WR, new Coord(0,0), true));
        assertEquals(1, cluster.getSize());
    }

    @Test
    public void AddingBLTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertFalse(cluster.addIfValid(SymbolType.BL, new Coord(0,0), false));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingDifferentTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertFalse(cluster.addIfValid(SymbolType.H2, new Coord(0,0), false));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingSameTypeTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertTrue(cluster.addIfValid(SymbolType.H1, new Coord(0,0), false));
        assertEquals(1, cluster.getSize());
        assertEquals(0, cluster.getCoords().get(0).reel());
        assertEquals(0, cluster.getCoords().get(0).row());
    }

    @Test
    public void AddingDupTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        cluster.addIfValid(SymbolType.H1, new Coord(0,0), false);
        assertFalse(cluster.addIfValid(SymbolType.H1, new Coord(0,0), false));
        assertEquals(1, cluster.getSize());
    }
}