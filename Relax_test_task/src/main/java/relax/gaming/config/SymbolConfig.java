package relax.gaming.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolConfig {
    private final Map<String, Symbol> symbols = new HashMap<>();

    private final List<Symbol> spinOptions = new ArrayList<>();
    private final List<Integer> spinWeights = new ArrayList<>();
    private final List<Symbol> avalancheOptions = new ArrayList<>();
    private final List<Integer> avalancheWeights = new ArrayList<>();

    public SymbolConfig(List<Symbol> incoming){
        if(incoming == null){
            throw new IllegalArgumentException("Symbol list must not be null");
        }
        int spinTotal = 0;
        int avalancheTotal = 0;
        for(Symbol symbol : incoming){
            this.symbols.put(symbol.getName(), symbol);

            if(symbol.getSpinWeight() > 0){
                spinTotal += symbol.getSpinWeight();
                this.spinWeights.add(spinTotal);
                this.spinOptions.add(symbol);
            }

            if(symbol.getAvalancheWeight() > 0){
                avalancheTotal += symbol.getAvalancheWeight();
                this.avalancheWeights.add(avalancheTotal);
                this.avalancheOptions.add(symbol);
            }
        }
    }

    public List<Symbol> spinOptions(){return this.spinOptions;}
    public List<Integer> spinWeights(){return this.spinWeights;}
    public List<Symbol> avalancheOptions(){return this.avalancheOptions;}
    public List<Integer> avalancheWeights(){return this.avalancheWeights;}

    public Symbol get(String name){
        return symbols.get(name);
    }
}
