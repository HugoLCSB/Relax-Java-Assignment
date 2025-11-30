package relax.gaming.core;

import org.apache.logging.log4j.ThreadContext;
import relax.gaming.config.*;
import relax.gaming.rnd.Rnd;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is the Game Engine,a heart of the back-end service responsible for the game rules and mechanics.
 */
public class Engine {
    private static final Logger LOGGER = LogManager.getLogger(Engine.class);
    public static final int REEL_AMOUNT = 8;
    public static final int ROW_AMOUNT = 8;
    private final SymbolConfig symbolConfig;
    private final PayoutConfig payoutConfig;

    /**
     * The Constructor
     * @param configManager the configuration manager
     */
    public Engine(ConfigManager configManager){
        this.symbolConfig = configManager.getSymbolConfig();
        this.payoutConfig = configManager.getPayoutConfig();
    }

    /**
     * Represents one round of the game, with a randomly generated grid
     * that can have many steps and has a total money payout.
     *
     * @param seed the given seed
     * @param bet the given money bet
     * @return the Round information
     */
    public GameRound doSpin(long seed, double bet){
        Rnd rnd = new Rnd(seed);
        ThreadContext.put("seed", String.valueOf(rnd.getSeed()));

        int stepIndex = 0;
        Map<Integer, GameStep> result = new HashMap<>();

        GameStep currStep;
        Symbol[][] currGrid =  GridGenerator.generateGrid(rnd,
                REEL_AMOUNT, ROW_AMOUNT,
                this.symbolConfig.spinOptions(), this.symbolConfig.spinWeights());
        do{
            LOGGER.debug("Starting gameStep: {}", stepIndex);
            //start gameStep
            currStep = new GameStep(currGrid, payoutConfig.getMinClusterSize());
            currStep.compute();
            result.put(stepIndex, currStep);
            LOGGER.debug("Step payout is {}", currStep.getStepPayout());

            //set up the next gameStep
            if(currStep.hasClusters()){
                stepIndex++;
                currGrid = GridGenerator.populateGrid(rnd,
                        currStep.getGridAfterGravity(),
                        this.symbolConfig.avalancheOptions(), this.symbolConfig.avalancheWeights());
            }
        }while(currStep.hasClusters());

        double totalPayout = calculatePayout(result, bet);
        LOGGER.debug("Total round payout is {}", totalPayout);
        return new GameRound(seed, bet, result, totalPayout);
    }

    /**
     * Calculates the payout for a given round
     * @param steps the steps of the round to calculate from
     * @param bet the bet for this round
     * @return the total payout for this round
     */
    private double calculatePayout(Map<Integer, GameStep> steps, double bet){
        double total = 0;
        for(GameStep step : steps.values()){
            double stepPayout = 0;
            for(Cluster cluster : step.getClusters()){
                double cPayout =
                        this.payoutConfig.getPayout(cluster.getType().getName(), cluster.getSize(), bet);
                cluster.setPayout(cPayout);
                stepPayout += cPayout;
            }
            step.setStepPayout(stepPayout);
            total += stepPayout;
        }
        return total;
    }

    /**
     * Does a bet of double or nothing, 50/50 chance, of either doubling
     * the money or getting nothing.
     * @param seed the given seed
     * @param bet the bet amount
     * @return Round response object.
     */
    public GameRound doGamble(long seed, double bet){
        Rnd rnd = new Rnd(seed);
        double result = rnd.nextBool() ? bet*2 : 0;
        return new GameRound(rnd.getSeed(), bet, null, result);
    }

    /**
     * Does a simulation of the game in order to calculate
     * the (RTP) return to player
     * @param exec the executor service to use for this simulation execution
     * @param n number of simulations to perform
     * @param batchSize the batchSize
     * @return Completable future that once completed returns a SimulationResult
     */
    public CompletableFuture<SimulationResult> doSimulationAsync(ExecutorService exec, int n, int batchSize){
        List<CompletableFuture<Double>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < n/batchSize; i++) {
            CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
                double batchTotal = 0;
                for (int j = 0; j < batchSize; j++) {
                    GameRound round = doSpin(0, 1); // 1 unit bet
                    batchTotal += round.totalPayout();
                }
                return batchTotal;
            }, exec);
            futures.add(future);
        }

        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .mapToDouble(CompletableFuture::join)
                        .sum())
                .thenApply(result -> {
                    long seconds = (System.currentTimeMillis() - start)/1000;
                    return new SimulationResult(n, result/n, seconds);
                });
    }
}
