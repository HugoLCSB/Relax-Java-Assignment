package relax.gaming.rnd;

import java.util.Random;

public class Rnd {
    private final Random random;
    private final long seed;

    /**
     * For this implementation the seed is optional in the sense that
     * a 0 value seed will be exchanged for a randomly generated seed.
     *
     * @param seed the given seed
     */
    public Rnd(long seed){
        if(seed == 0){
            seed = new Random().nextLong();
        }
        this.seed = seed;
        this.random = new Random(seed);
    }

    /**
     * Gets the seed used in this Random.
     * @return the long seed
     */
    public long getSeed(){
        return this.seed;
    }

    /**
     * Next random integer between the given bounds.
     * @param lower lower bound inclusive
     * @param upper upper bound exclusive
     * @return the random int
     */
    public int nextInt(int lower, int upper){
        return this.random.nextInt(lower, upper);
    }

    /**
     * 50/50 choice.
     *
     * @return the random boolean.
     */
    public boolean nextBool(){
        return this.random.nextBoolean();
    }
}
