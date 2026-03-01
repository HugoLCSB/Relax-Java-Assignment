package relax.gaming.core;

import relax.gaming.config.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private final Symbol type;
    private final Symbol[] coords;
    private final int gridRowsSize;
    private int clusterSize;
    private final List<Coord> toDestroy;
    private double payout;

    public Cluster(Symbol type, int reels, int rows) {
        if (type == null) {
            throw new IllegalArgumentException("Cluster type must not be null");
        }
        this.type = type;
        this.gridRowsSize = rows;
        this.coords = new Symbol[reels * rows];
        this.toDestroy = new ArrayList<>();
        this.payout = 0;
    }

    public Symbol getType() {
        return this.type;
    }

    public int getSize() {
        return this.clusterSize;
    }

    public double getPayout() {
        return this.payout;
    }

    public void addToDestroy(Coord coord) {
        this.toDestroy.add(coord);
    }

    public List<Coord> toDestroy() {
        return this.toDestroy;
    }

    /**
     * Adds the given element to the list of Coordinates of this cluster and also to
     * the
     * list to be destroyed if cluster doesn't already contain the given entry and
     * if its
     * of same type as the cluster or wildcard.
     *
     * @param type  type of the given element to add
     * @param coord coordinates of the element to add
     * @return true if added
     */
    public boolean addIfValid(Symbol type, Coord coord) {
        int index = (coord.reel() * this.gridRowsSize) + coord.row();
        if ((this.coords[index] == null) && ((this.type.equals(type) || type.isWildCard()))) {
            this.clusterSize++;
            this.coords[index] = type;
            this.toDestroy.add(coord);
            return true;
        }
        return false;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }
}
