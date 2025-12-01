package relax.gaming.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayoutConfigTest {
    //TODO: should not depend on the config files for unit tests
    private static final String SYMBOL_FILE = "symbols.json";
    private static final String PAYOUT_FILE = "payouts.json";
    private static final String BUCKET_FILE = "clusterBuckets.json";

    private PayoutConfig payoutConfig;

    @BeforeEach
    void setup() {
        try{
            ConfigManager config = new ConfigManager(SYMBOL_FILE, PAYOUT_FILE, BUCKET_FILE);
            this.payoutConfig = config.getPayoutConfig();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void InvalidSizedClusterTest(){
        double pay = this.payoutConfig.getPayout(null, this.payoutConfig.getMinClusterSize(), 0);
        assertEquals(0.0, pay);
    }


    @Test
    public void ValidSizeClusterTest(){
        String testSymbol = "H1";
        double pay = this.payoutConfig.getPayout(testSymbol, this.payoutConfig.getMinClusterSize(), 10.0);
        assertEquals(5.0, pay);
    }
}