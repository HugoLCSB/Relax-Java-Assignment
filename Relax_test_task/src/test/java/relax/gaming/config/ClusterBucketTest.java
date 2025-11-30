package relax.gaming.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClusterBucketTest {

    @Test
    public void TestBucketSelectionMinBound(){
        assertEquals(ClusterBucket.NONE, ClusterBucket.getBucket(0));
    }

    @Test
    public void TestBucketSelectionViableBound(){
        assertEquals(ClusterBucket.S, ClusterBucket.getBucket(5));
    }

    @Test
    public void TestBucketSelectionMaxBound(){
        assertEquals(ClusterBucket.XL, ClusterBucket.getBucket(64));
    }

    @Test
    public void TestBucketSelectionSeparation(){
        assertEquals(ClusterBucket.M, ClusterBucket.getBucket(9));
    }
}