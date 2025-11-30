package relax.gaming.core;

import java.util.Map;

public record Round(long seed, double bet, Map<Integer, GameStep> steps, double totalPayout) {
}
