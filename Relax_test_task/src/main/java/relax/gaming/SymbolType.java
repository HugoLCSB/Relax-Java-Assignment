package relax.gaming;

public enum SymbolType {
    BLOCKER(100, 0),
    H1(100, 100),
    H2(100, 100),
    H3(100, 100),
    H4(100, 100),
    L5(100, 100),
    L6(100, 100),
    L7(100, 100),
    L8(100, 100),
    WR(100, 100);

    public final int spinWeight;
    public final int avalancheWeight;

    SymbolType(int spinWeight, int avalancheWeight) {
        this.spinWeight = spinWeight;
        this.avalancheWeight = avalancheWeight;
    }
}
