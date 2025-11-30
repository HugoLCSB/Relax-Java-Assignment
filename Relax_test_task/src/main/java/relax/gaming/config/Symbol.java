package relax.gaming.config;

import com.fasterxml.jackson.annotation.JsonValue;

public class Symbol {
    private String name;
    private int spinWeight;
    private int avalancheWeight;
    private boolean isWildCard;
    private boolean isBlocker;
    public Symbol(){}

    public Symbol(String name, int spinWeight, int avalancheWeight, boolean isWildCard, boolean isBlocker) {
        this.name = name;
        this.spinWeight = spinWeight;
        this.avalancheWeight = avalancheWeight;
        this.isWildCard = isWildCard;
        this.isBlocker = isBlocker;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getSpinWeight() {
        return this.spinWeight;
    }
    public void setSpinWeight(int spinWeight) {
        this.spinWeight = spinWeight;
    }

    public int getAvalancheWeight() {
        return this.avalancheWeight;
    }
    public void setAvalancheWeight(int avalancheWeight) {
        this.avalancheWeight = avalancheWeight;
    }

    public boolean isWildCard() {
        return this.isWildCard;
    }
    public void setIsWildCard(boolean isWildCard) {
        this.isWildCard = isWildCard;
    }

    public boolean isBlocker() {
        return this.isBlocker;
    }

    public void setIsBlocker(boolean isBlocker) {
        this.isBlocker = isBlocker;
    }

    @JsonValue
    @Override
    public String toString(){
        return this.name;
    }
}
