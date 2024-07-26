import backstage.move.*;
public class MinimaxResult {
    public double score;
    public Move bestMove;

    public MinimaxResult(double score, Move bestMove) {
        this.score = score;
        this.bestMove = bestMove;
    }
}