package relax.gaming.core;

import org.junit.jupiter.api.Test;
import relax.gaming.config.SymbolType;

import static org.junit.jupiter.api.Assertions.*;

class ClusterTest {
    @Test
    public void AddingNullTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertDoesNotThrow(() ->{
            cluster.add(null, null);
        });

        assertEquals(0, cluster.getSize());

        boolean output = cluster.add(null, null);
        assertFalse(output);
    }

    @Test
    public void AddingWRTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertTrue(cluster.add(SymbolType.WR, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
    }

    @Test
    public void AddingBLTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertFalse(cluster.add(SymbolType.BL, new Coord(0,0)));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingDifferentTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertFalse(cluster.add(SymbolType.H2, new Coord(0,0)));
        assertEquals(0, cluster.getSize());
    }

    @Test
    public void AddingSameTypeTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        assertTrue(cluster.add(SymbolType.H1, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
    }

    @Test
    public void AddingDupTest(){
        Cluster cluster = new Cluster(SymbolType.H1);
        cluster.add(SymbolType.H1, new Coord(0,0));
        assertFalse(cluster.add(SymbolType.H1, new Coord(0,0)));
        assertEquals(1, cluster.getSize());
    }
}