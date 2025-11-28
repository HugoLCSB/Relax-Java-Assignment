package relax.gaming;

import java.util.List;

public class GameStep {
    private int step;
    private SymbolType[][] grid;
    private List<Cluster> clusters;
    private int winAmount;

    public GameStep(int step, SymbolType[][] grid, List<Cluster> clusters, int winAmount) {
        this.step = step;
        this.grid = grid;
        this.clusters = clusters;
        this.winAmount = winAmount;
    }

    public String toJson(){
        return "";
    }
}
