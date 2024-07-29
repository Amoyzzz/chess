import backstage.Board;
import java.util.*;

public class OpeningBook {
    private final Map<String, BookMove[]> movesByPosition;
    private final Random rng;
    private final double weightPow;

    public OpeningBook(String file, double weightPow) {
        rng = new Random();
        this.weightPow = Math.max(0, Math.min(weightPow, 1)); // Ensure weightPow is between 0 and 1

        String[] entries = file.trim().split("pos");
        movesByPosition = new HashMap<>(entries.length - 1);

        for (int i = 1; i < entries.length; i++) {
            String[] entryData = entries[i].trim().split("\n");
            String positionFen = entryData[0].trim();
            String[] allMoveData = Arrays.copyOfRange(entryData, 1, entryData.length);

            BookMove[] bookMoves = new BookMove[allMoveData.length];

            for (int moveIndex = 0; moveIndex < bookMoves.length; moveIndex++) {
                String[] moveData = allMoveData[moveIndex].split(" ");
                bookMoves[moveIndex] = new BookMove(moveData[0], Integer.parseInt(moveData[1]));
            }

            movesByPosition.put(positionFen, bookMoves);
        }
    }

    public boolean hasBookMove(String positionFen) {
        return movesByPosition.containsKey(removeMoveCountersFromFEN(positionFen));
    }

    // WeightPow is a value between 0 and 1.
    // 0 means all moves are picked with equal probability, 1 means moves are weighted by num times played.
    public boolean tryGetBookMove(Board board, String[] moveString) {
        String positionFen = board.getFen();
        if (movesByPosition.containsKey(removeMoveCountersFromFEN(positionFen))) {
            BookMove[] moves = movesByPosition.get(removeMoveCountersFromFEN(positionFen));

            int totalPlayCount = 0;
            for (BookMove move : moves) {
                totalPlayCount += weightedPlayCount(move.numTimesPlayed);
            }

            double[] weights = new double[moves.length];
            double weightSum = 0;
            for (int i = 0; i < moves.length; i++) {
                double weight = weightedPlayCount(moves[i].numTimesPlayed) / (double) totalPlayCount;
                weightSum += weight;
                weights[i] = weight;
            }

            double[] probCumul = new double[moves.length];
            for (int i = 0; i < weights.length; i++) {
                double prob = weights[i] / weightSum;
                probCumul[i] = (i > 0 ? probCumul[i - 1] : 0) + prob;
            }

            double random = rng.nextDouble();
            for (int i = 0; i < moves.length; i++) {
                if (random <= probCumul[i]) {
                    moveString[0] = moves[i].moveString;
                    return true;
                }
            }
        }

        moveString[0] = "Null";
        return false;
    }

    private String removeMoveCountersFromFEN(String fen) {
        String fenA = fen.substring(0, fen.lastIndexOf(' '));
        return fenA.substring(0, fenA.lastIndexOf(' '));
    }

    private int weightedPlayCount(int playCount) {
        return (int) Math.ceil(Math.pow(playCount, weightPow));
    }

    public static class BookMove {
        public final String moveString;
        public final int numTimesPlayed;

        public BookMove(String moveString, int numTimesPlayed) {
            this.moveString = moveString;
            this.numTimesPlayed = numTimesPlayed;
        }
    }
}
