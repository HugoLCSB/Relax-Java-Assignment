package relax.gaming.rnd;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

public class RndSecure extends Rnd {
    /**
     * This implementation of Rnd uses regular java.security.SecureRandom internally
     * @param seed   the given seed
     */
    public RndSecure(long seed) {
        super(seed);
    }

    @Override
    protected RandomGenerator getRandom() {
        SecureRandom random = new SecureRandom();
        random.setSeed(super.seed);
        return random;
    }
}
