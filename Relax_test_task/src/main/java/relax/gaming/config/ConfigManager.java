package relax.gaming.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final SymbolConfig symbolConfig;
    private final PayoutConfig payoutConfig;

    public ConfigManager(String symbolFile, String payoutFile, String bucketsFile){
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Symbol> symbols = Arrays.asList(
                    mapper.readValue(new File(symbolFile), Symbol[].class)
            );
            this.symbolConfig = new SymbolConfig(symbols);

            Map<String, Map<String, Double>> payouts = mapper.readValue(
                    new File(payoutFile),
                    new TypeReference<Map<String, Map<String, Double>>>(){}
            );

            List<Bucket> buckets = Arrays.asList(
                    mapper.readValue(new File(bucketsFile), Bucket[].class)
            );
            this.payoutConfig = new PayoutConfig(payouts, buckets);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SymbolConfig getSymbolConfig() {
        return symbolConfig;
    }

    public PayoutConfig getPayoutConfig() {
        return payoutConfig;
    }
}
