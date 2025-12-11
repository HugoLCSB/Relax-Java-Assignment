package relax.gaming.rnd;

import java.util.Random;
import java.util.random.RandomGenerator;

public class RndRegular extends Rnd {
    /**
     * This implementation uses regular java.util.Random internally
     * @param seed   the given seed
     */
    public RndRegular(long seed) {
        super(seed);
    }

    @Override
    protected RandomGenerator getRandom() {
        return new Random(super.seed);
    }
}
