package relax.gaming.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private final SymbolConfig symbolConfig;
    private final PayoutConfig payoutConfig;

    public ConfigManager(String symbolFile, String payoutFile, String bucketsFile) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.symbolConfig = loadSymbolConfig(mapper, symbolFile);
            this.payoutConfig = loadPayoutConfig(mapper, payoutFile, bucketsFile);
        } catch (IOException e) {
            LOGGER.error("Error loading configuration files", e);
            throw new IOException(e);
        }
    }

    public SymbolConfig getSymbolConfig() {
        return symbolConfig;
    }

    public PayoutConfig getPayoutConfig() {
        return payoutConfig;
    }

    private SymbolConfig loadSymbolConfig(ObjectMapper mapper, String symbolFile) throws IOException {
        List<Symbol> symbols = Arrays.asList(
                mapper.readValue(new File(symbolFile), Symbol[].class)
        );
        return new SymbolConfig(symbols);
    }

    private PayoutConfig loadPayoutConfig(ObjectMapper mapper, String payoutFile, String bucketsFile) throws IOException {
        Map<String, Map<String, Double>> payouts =
                mapper.readValue(new File(payoutFile), new TypeReference<>() {
                });

        Bucket[] buckets = mapper.readValue(new File(bucketsFile), Bucket[].class);
        return new PayoutConfig(payouts, buckets);
    }
}
