package relax.gaming.rnd;

import java.util.Random;
import java.util.random.RandomGenerator;

public abstract class Rnd {
    protected final RandomGenerator random;
    protected final long seed;

    /**
     * For this implementation the seed is optional in the sense that
     * a 0 value seed will be exchanged for a randomly generated seed.
     *
     * @param seed the given seed
     */
    protected Rnd(long seed) {
        this.seed = seed != 0 ? seed : new Random().nextLong();
        this.random = getRandom();
    }

    /**
     * This method is supposed to be overridden in order to introduce
     * specific RandomGenerator strategies
     *
     * @return the RandomGenerator
     */
    protected abstract RandomGenerator getRandom();

    /**
     * Gets the seed used in this Random.
     *
     * @return the long seed
     */
    public long getSeed() {
        return this.seed;
    }

    /**
     * Next random integer between the given bounds.
     *
     * @param lower lower bound inclusive
     * @param upper upper bound exclusive
     * @return the random int
     */
    public int nextInt(int lower, int upper) {
        return this.random.nextInt(lower, upper);
    }

    /**
     * 50/50 choice.
     *
     * @return the random boolean.
     */
    public boolean nextBool() {
        return this.random.nextBoolean();
    }
}
