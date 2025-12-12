package relax.gaming.core;

import java.util.List;

public record GameRound(long seed, double bet, List<GameStep> steps, double totalPayout) {
}
