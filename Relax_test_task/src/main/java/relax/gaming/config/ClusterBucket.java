package relax.gaming.config;

import relax.gaming.core.Engine;

public enum ClusterBucket {
    NONE(0,0),
    S(5, 8),
    M(9, 12),
    ML(13,16),
    L(17,20),
    XL(21, Engine.BOARD_SIZE);

    public final int starting;
    public final int ending;

    ClusterBucket(int starting, int ending) {
        this.starting = starting;
        this.ending = ending;
    }

    public static ClusterBucket getBucket(int clusterSize){
        for(ClusterBucket b : ClusterBucket.values()){
            if(b.starting < clusterSize && clusterSize < b.ending){
                return b;
            }
        }
        return ClusterBucket.NONE;
    }
}
