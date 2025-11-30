package relax.gaming.config;

import java.util.List;
import java.util.Map;

public class PayoutConfig {
    private final Map<String, Map<String, Double>> payoutTable;
    private final List<Bucket> buckets;
    private final int minClusterSize;

    public PayoutConfig(Map<String, Map<String, Double>> payoutTable, List<Bucket> buckets){
        this.payoutTable = payoutTable;
        this.buckets = buckets;
        this.minClusterSize = findMinClusterSize();
    }

    public int getMinClusterSize(){
        return this.minClusterSize;
    }

    public double getPayout(String type, int clusterSize, double bet) {
        String bucket = getBucket(clusterSize);
        if (bucket == null) return 0;

        Map<String, Double> payoutMap = payoutTable.get(type);
        if (payoutMap == null) return 0;

        Double multiplier = payoutMap.get(bucket);
        if (multiplier == null) return 0;

        return bet * multiplier;
    }

    private String getBucket(int size){
        for(Bucket b : this.buckets){
            if(b.start() <= size && size <= b.end()){
                return b.name();
            }
        }
        return null;
    }

    private int findMinClusterSize(){
        int min = Integer.MAX_VALUE;
        for(Bucket b : this.buckets){
            if(b.start() < min){
                min = b.start();
            }
        }
        return min;
    }
}
