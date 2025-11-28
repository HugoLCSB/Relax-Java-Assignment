package relax.gaming;

import java.util.Map;

public class Payouts {
    public static final Map<SymbolType, Map<ClusterBucket, Double>> WinAmount = Map.of(
            SymbolType.H1, Map.of(
                    ClusterBucket.S, 0.5,
                    ClusterBucket.M, 0.6,
                    ClusterBucket.ML, 0.7,
                    ClusterBucket.L, 0.8,
                    ClusterBucket.XL, 1.0
            ),
            SymbolType.H2, Map.of(
                    ClusterBucket.S, 0.4,
                    ClusterBucket.M, 0.5,
                    ClusterBucket.ML, 0.6,
                    ClusterBucket.L, 0.7,
                    ClusterBucket.XL, 0.9
            ),
            SymbolType.H3, Map.of(
                    ClusterBucket.S, 0.4,
                    ClusterBucket.M, 0.5,
                    ClusterBucket.ML, 0.6,
                    ClusterBucket.L, 0.7,
                    ClusterBucket.XL, 0.9
            ),
            SymbolType.H4, Map.of(
                    ClusterBucket.S, 0.3,
                    ClusterBucket.M, 0.4,
                    ClusterBucket.ML, 0.5,
                    ClusterBucket.L, 0.6,
                    ClusterBucket.XL, 0.7
            ),
            SymbolType.L5, Map.of(
                    ClusterBucket.S, 0.1,
                    ClusterBucket.M, 0.2,
                    ClusterBucket.ML, 0.3,
                    ClusterBucket.L, 0.4,
                    ClusterBucket.XL, 0.5
            ),
            SymbolType.L6, Map.of(
                    ClusterBucket.S, 0.1,
                    ClusterBucket.M, 0.2,
                    ClusterBucket.ML, 0.3,
                    ClusterBucket.L, 0.4,
                    ClusterBucket.XL, 0.5
            ),
            SymbolType.L7, Map.of(
                    ClusterBucket.S, 0.1,
                    ClusterBucket.M, 0.2,
                    ClusterBucket.ML, 0.3,
                    ClusterBucket.L, 0.4,
                    ClusterBucket.XL, 0.5
            ),
            SymbolType.L8, Map.of(
                    ClusterBucket.S, 0.1,
                    ClusterBucket.M, 0.2,
                    ClusterBucket.ML, 0.3,
                    ClusterBucket.L, 0.4,
                    ClusterBucket.XL, 0.5
            )
    );

    public static double getPayout(SymbolType type, int clusterSize, double bet){
        Map<ClusterBucket, Double> symbolMultipliers = WinAmount.get(type);
        double multiplier = symbolMultipliers != null
                ? symbolMultipliers.getOrDefault(ClusterBucket.getBucket(clusterSize), 0.0)
                : 0.0;

        return bet * multiplier;
    }
}
