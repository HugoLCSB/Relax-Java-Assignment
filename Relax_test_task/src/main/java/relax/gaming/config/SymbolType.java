package relax.gaming.config;

import java.util.Arrays;

public enum SymbolType {
    BL(100, 0),
    H1(100, 100),
    H2(100, 100),
    H3(100, 100),
    H4(100, 100),
    L5(100, 100),
    L6(100, 100),
    L7(100, 100),
    L8(100, 100),
    WR(100, 100);

    private final int spinWeight;
    private final int avalancheWeight;

    SymbolType(int spinWeight, int avalancheWeight) {
        this.spinWeight = spinWeight;
        this.avalancheWeight = avalancheWeight;
    }

    public SymbolType[] getValues(){
        return SymbolType.values();
    }
    public int getSpinWeight(){return spinWeight;}
    public int getAvalancheWeight(){return avalancheWeight;}
    public static boolean isIgnored(SymbolType type){
        return type != null && (type.equals(SymbolType.BL));
    }
    public static boolean isWildCard(SymbolType type){
        return type != null && (type.equals(SymbolType.WR));
    }

    /**
     * Gets Spin Weights in a cumulative array
     * @return the weights array
     */
    public static int[] getSpinWeights(){
        int[] spinWeightsArray = Arrays.stream(SymbolType.values())
                .mapToInt(SymbolType::getSpinWeight) // Convert to primitive int
                .toArray();
        return getCumulativeInternal(spinWeightsArray);
    }

    /**
     * Gets avalanche Weights in a cumulative array
     * @return the weights array
     */
    public static int[] getAvalancheWeights(){
        int[] avalancheWeightsArray = Arrays.stream(SymbolType.values())
                .mapToInt(SymbolType::getAvalancheWeight) // Convert to primitive int
                .toArray();
        return getCumulativeInternal(avalancheWeightsArray);
    }

    private static int[] getCumulativeInternal(int[] specificWeights){
        SymbolType[] types = SymbolType.values();
        int[] cumulative = new int[types.length];
        int sum = 0;
        for(int i = 0; i < specificWeights.length; i++){
            sum += specificWeights[i];
            cumulative[i] = sum;
        }
        return cumulative;
    }
}
