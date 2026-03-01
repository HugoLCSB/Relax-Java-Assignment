package relax.gaming.core;

import relax.gaming.config.Symbol;
import relax.gaming.utils.Utils;

public class Cluster {
    private final Symbol type;
    private long coords;
    private final int gridRowsSize;
    private int clusterSize;
    private double payout;

    public Cluster(Symbol type, int reels, int rows) {
        if (type == null) {
            throw new IllegalArgumentException("Cluster type must not be null");
        }
        this.type = type;
        this.gridRowsSize = rows;
        this.coords = 0L;
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

    /**
     * Some pieces might need to be manually added to cluster for destruction
     * purposes
     * and thus shouldnt be counted for the total cluster size
     * 
     * @param coord
     */
    public void addToDestroy(int reel, int row) {
        int index = (reel * this.gridRowsSize) + row;
        this.coords = Utils.bitwiseAdd(this.coords, index);
    }

    public long toDestroy() {
        return this.coords;
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
    public boolean addIfValid(Symbol type, int reel, int row) {
        int index = (reel * this.gridRowsSize) + row;

        long mask = 1L << index;
        boolean contains = (this.coords & mask) != 0L;

        if (!contains && ((this.type.equals(type) || type.isWildCard()))) {
            this.clusterSize++;
            this.coords |= mask;
            return true;
        }
        return false;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }
}
