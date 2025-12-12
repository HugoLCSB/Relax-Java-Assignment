package relax.gaming.rnd;

import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * This implementation uses regular java.util.Random internally
 */
public class RndRegular extends Rnd {
    /**
     * The Constructor
     *
     * @param seed the given seed
     */
    public RndRegular(long seed) {
        super(seed);
    }

    /**
     * This override introduces java.util.Random
     *
     * @return the RandomGenerator
     */
    @Override
    protected RandomGenerator getRandom() {
        return new Random(super.seed);
    }
}
