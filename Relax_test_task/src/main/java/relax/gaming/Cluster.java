package relax.gaming;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private SymbolType type;
    private List<Coord> coords;
    private int size;

    public Cluster(SymbolType type) {
        this.type = type;
        this.coords = new ArrayList<>(){};
        this.size = 0;
    }

    public SymbolType getType() {
        return this.type;
    }

    public List<Coord> getCoords() {
        return this.coords;
    }

    public int getSize() {
        return this.size;
    }

    public boolean add(SymbolType type, Coord coord) {
        if(this.coords.contains(coord)){
            return false;
        }
        if(SymbolType.isIgnored(type)) {
            this.coords.add(coord);
            return false;
        }

        if(SymbolType.isWildCard(type) || this.type.equals(type)) {
            this.size++;
            this.coords.add(coord);
            return true;
        }
        return false;
    }
}
