package relax.gaming.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PayoutConfig {
    private static final Logger LOGGER = LogManager.getLogger(PayoutConfig.class);
    private final Map<String, Map<String, Double>> payoutTable = new HashMap<>();
    private final Bucket[] buckets;
    private final int minClusterSize;

    public PayoutConfig(Payout[] payouts, Bucket[] buckets) {
        if (payouts == null || buckets == null) {
            throw new IllegalArgumentException("Payout table and buckets list cannot be null");
        }

        for (Payout payout : payouts) {
            Map<String, Double> inner = this.payoutTable.computeIfAbsent(payout.symbol(), k -> new HashMap<>());
            inner.put(payout.bucket(), payout.amount());
        }
        
        this.buckets = buckets;
        this.minClusterSize = findMinClusterSize();
    }

    public int getMinClusterSize() {
        return this.minClusterSize;
    }

    public double getPayout(String type, int clusterSize, double bet) {
        if (type == null || bet <= 0) {
            LOGGER.warn("Invalid parameters: type={}, bet{}, no payout calculated", type, bet);
            return 0;
        }

        String bucket = getBucket(clusterSize);
        if (bucket == null) {
            LOGGER.warn("No bucket found for parameters: type={}, clusterSize={}, bet{}, no payout calculated",
                    type, clusterSize, bet);
            return 0;
        }

        Map<String, Double> payoutMap = this.payoutTable.get(type);
        if (payoutMap == null) {
            LOGGER.warn("No payout map found for parameters: type={}, clusterSize={}, bet{}, no payout calculated",
                    type, clusterSize, bet);
            return 0;
        }

        Double multiplier = payoutMap.get(bucket);
        if (multiplier == null) {
            LOGGER.warn("No payout map found for parameters: type={}, clusterSize={}, bet{}, no payout calculated",
                    type, clusterSize, bet);
            return 0;
        }

        return bet * multiplier;
    }

    private String getBucket(int size) {
        for (Bucket b : this.buckets) {
            if (b.start() <= size && size <= b.end()) {
                return b.name();
            }
        }
        return null;
    }

    private int findMinClusterSize() {
        int min = Integer.MAX_VALUE;
        for (Bucket b : this.buckets) {
            if (b.start() < min) {
                min = b.start();
            }
        }
        return min;
    }
}
