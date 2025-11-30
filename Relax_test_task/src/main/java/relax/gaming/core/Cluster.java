package relax.gaming.core;

import relax.gaming.config.Payouts;
import relax.gaming.config.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private final SymbolType type;
    private final List<Coord> coords;
    private final List<Coord> toDestroy;
    private double payout;

    public Cluster(SymbolType type) {
        this.type = type;
        this.coords = new ArrayList<>(){};
        this.toDestroy = new ArrayList<>(){};
        this.payout = 0;
    }

    public SymbolType getType() {
        return this.type;
    }

    public int getSize(){
        return this.coords.size();
    }

    public List<Coord> getCoords() {
        return this.coords;
    }

    public double getPayout(){
        return this.payout;
    }

    public List<Coord> toDestroy(){
        return this.toDestroy;
    }

    public boolean add(SymbolType type, Coord coord) {
        if(!this.coords.contains(coord) && ((this.type.equals(type) || SymbolType.isWildCard(type)))) {
            this.coords.add(coord);
            return true;
        }
        return false;
    }

    public double calculateWin(double bet){
        return this.payout = Payouts.getPayout(this.type, this.getSize(), bet);
    }
}
