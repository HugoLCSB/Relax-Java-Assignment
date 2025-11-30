package relax.gaming.core;

import org.apache.logging.log4j.ThreadContext;
import relax.gaming.config.SymbolType;
import relax.gaming.rnd.Rnd;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relax.gaming.utils.SingletonExecutor;

/**
 * This class is the Game Engine,a heart of the back-end service responsible for the game rules and mechanics.
 */
public class Engine {
    private static final Logger LOGGER = LogManager.getLogger(Engine.class);
    public static final int REEL_AMOUNT = 8;
    public static final int ROW_AMOUNT = 8;
    public static final int BOARD_SIZE = REEL_AMOUNT * ROW_AMOUNT;

    /**
     * Represents one round of the game, with a randomly generated grid
     * that can have many steps and has a total money payout.
     *
     * @param seed the given seed
     * @param bet the given money bet
     * @return the Round information
     */
    public static Round doSpin(long seed, double bet){
        Rnd rnd = new Rnd(seed);
        ThreadContext.put("seed", String.valueOf(rnd.getSeed()));
        double totalPayout = 0;

        int stepIndex = 0;
        Map<Integer, GameStep> result = new HashMap<>();

        GameStep currStep;
        SymbolType[][] currGrid =  GridGenerator.generateGrid(rnd,
                REEL_AMOUNT, ROW_AMOUNT, SymbolType.getSpinWeights());
        do{
            LOGGER.debug("Starting gameStep: {}", stepIndex);
            //start gameStep
            currStep = new GameStep(currGrid, bet);
            currStep.compute();
            result.put(stepIndex, currStep);
            totalPayout += currStep.getStepPayout();
            LOGGER.debug("Step payout is {}", currStep.getStepPayout());

            //set up the next gameStep
            if(currStep.hasClusters()){
                stepIndex++;
                currGrid = GridGenerator.populateGrid(rnd,
                        currStep.getGridAfterGravity(), SymbolType.getAvalancheWeights());
            }
        }while(currStep.hasClusters());

        LOGGER.debug("Total round payout is {}", totalPayout);
        return new Round(seed, bet, result, totalPayout);
    }

    /**
     * Does a bet of double or nothing, 50/50 chance, of either doubling
     * the money or getting nothing.
     * @param seed the given seed
     * @param bet the bet amount
     * @return Round response object.
     */
    public static Round doGamble(long seed, double bet){
        Rnd rnd = new Rnd(seed);
        double result = rnd.nextBool() ? bet*2 : 0;
        return new Round(rnd.getSeed(), bet, null, result);
    }

    /**
     * Does a simulation of the game in order to calculate
     * the (RTP) return to player
     * @param n number of simulations to perform
     * @return the calculated RTP
     */
    public static String doSimulation(int n){
        double sum = 0;
        long start = System.currentTimeMillis();
        for(int i = 0; i < n; i++){
            Round round = doSpin(0,1);
            sum += round.totalPayout();
        }
        long delay = System.currentTimeMillis() - start;
        return String.format("Calculated RTP= %s in %s seconds", sum/n, delay/1000);
    }

    /**
     * Does a simulation of the game in order to calculate
     * the (RTP) return to player
     * @param n number of simulations to perform
     * @return the calculated RTP
     */
    public static CompletableFuture<SimulationResult> doSimulationAsync(int n, int batchSize){
        List<CompletableFuture<Double>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < n/batchSize; i++) {
            CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
                double batchTotal = 0;
                for (int j = 0; j < batchSize; j++) {
                    Round round = doSpin(0, 1); // 1 unit bet
                    batchTotal += round.totalPayout();
                }
                return batchTotal;
            }, SingletonExecutor.POOL);
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
