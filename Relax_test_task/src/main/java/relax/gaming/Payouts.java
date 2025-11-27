package relax.gaming;

import java.util.Map;

public class Payouts {
    public static final Map<SymbolType, Map<ClusterBucket, Integer>> WinAmount = Map.of(
            SymbolType.H1, Map.of(
                    ClusterBucket.S, 5,
                    ClusterBucket.M, 6,
                    ClusterBucket.ML, 7,
                    ClusterBucket.L, 8,
                    ClusterBucket.XL, 10
            ),
            SymbolType.H2, Map.of(
                    ClusterBucket.S, 4,
                    ClusterBucket.M, 5,
                    ClusterBucket.ML, 6,
                    ClusterBucket.L, 7,
                    ClusterBucket.XL, 9
            ),
            SymbolType.H3, Map.of(
                    ClusterBucket.S, 4,
                    ClusterBucket.M, 5,
                    ClusterBucket.ML, 6,
                    ClusterBucket.L, 7,
                    ClusterBucket.XL, 9
            ),
            SymbolType.H4, Map.of(
                    ClusterBucket.S, 3,
                    ClusterBucket.M, 4,
                    ClusterBucket.ML, 5,
                    ClusterBucket.L, 6,
                    ClusterBucket.XL, 7
            ),
            SymbolType.L5, Map.of(
                    ClusterBucket.S, 1,
                    ClusterBucket.M, 2,
                    ClusterBucket.ML, 3,
                    ClusterBucket.L, 4,
                    ClusterBucket.XL, 5
            ),
            SymbolType.L6, Map.of(
                    ClusterBucket.S, 1,
                    ClusterBucket.M, 2,
                    ClusterBucket.ML, 3,
                    ClusterBucket.L, 4,
                    ClusterBucket.XL, 5
            ),
            SymbolType.L7, Map.of(
                    ClusterBucket.S, 1,
                    ClusterBucket.M, 2,
                    ClusterBucket.ML, 3,
                    ClusterBucket.L, 4,
                    ClusterBucket.XL, 5
            ),
            SymbolType.L8, Map.of(
                    ClusterBucket.S, 1,
                    ClusterBucket.M, 2,
                    ClusterBucket.ML, 3,
                    ClusterBucket.L, 4,
                    ClusterBucket.XL, 5
            )
    );

    public static int getPayout(SymbolType type, int clusterSize){
        Map<ClusterBucket, Integer> symbolMultipliers = WinAmount.get(type);
        int multiplier = symbolMultipliers != null ?
                symbolMultipliers.getOrDefault(ClusterBucket.getBucket(clusterSize), 0) : 0;

        return clusterSize * multiplier;
    }
}
