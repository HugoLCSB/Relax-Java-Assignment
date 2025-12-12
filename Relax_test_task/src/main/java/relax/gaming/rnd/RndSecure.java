package relax.gaming.rnd;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

/**
 * This implementation of Rnd uses regular java.security.SecureRandom internally
 */
public class RndSecure extends Rnd {
    /**
     * The Constructor
     *
     * @param seed the given seed
     */
    public RndSecure(long seed) {
        super(seed);
    }

    /**
     * This override introduces java.security.SecureRandom
     *
     * @return the RandomGenerator
     */
    @Override
    protected RandomGenerator getRandom() {
        SecureRandom random = new SecureRandom();
        random.setSeed(super.seed);
        return random;
    }
}
