import backstage.move.*;
public class MinimaxResult {
    public int score;
    public Move bestMove;

    public MinimaxResult(int score, Move bestMove) {
        this.score = score;
        this.bestMove = bestMove;
    }
}