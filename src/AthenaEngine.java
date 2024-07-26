import java.util.*;
import backstage.*;
import backstage.move.*;

public class AthenaEngine {
    private static final double INFINITY = 100000000000.0;
    private static final double MINUS_INFINITY = -100000000000.0;

    public static void main(String[] args) {
        Board board = new Board();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(board);
            System.out.println("Evaluation: " + evaluate(board));
            System.out.println("\n\n");
            board.doMove(in.nextLine());
            System.out.println("\n\n");
            
            if (board.isDraw()) {
                System.out.println("DRAW");
                break;
            }
            if (board.isMated()) {
                System.out.println("CHECKMATE");
                break;
            }
            MinimaxResult result = minimax(board, 6, false, MINUS_INFINITY, INFINITY);
            System.out.println("Chosen move: " + result.bestMove);
            board.doMove(result.bestMove);
        }

        in.close();
    }
    
    public static MinimaxResult minimax(Board board, int depth, boolean maximizingPlayer, double alpha, double beta) {
        if (depth == 0 || board.isMated() || board.isDraw()) {
            return new MinimaxResult(evaluate(board), null);
        }

        if (maximizingPlayer) {
            double maxEval = MINUS_INFINITY;
            Move bestMove = null;
            for (Move move : board.legalMoves()) {
                board.doMove(move);
                MinimaxResult result = minimax(board, depth - 1, false, alpha, beta);
                board.undoMove();

                if (result.score > maxEval) {
                    maxEval = result.score;
                    bestMove = move;
                }
                alpha = Math.max(alpha, result.score);
                if(beta <= alpha){
                    break;
                }
            }
            return new MinimaxResult(maxEval, bestMove);
        } else {
            double minEval = INFINITY;
            Move bestMove = null;
            for (Move move : board.legalMoves()) {
                board.doMove(move);
                MinimaxResult result = minimax(board, depth - 1, true, alpha, beta);
                board.undoMove();

                if (result.score < minEval) {
                    minEval = result.score;
                    bestMove = move;
                }
                beta = Math.min(result.score, beta);
                if(beta <= alpha){
                    break;
                }
            }
            return new MinimaxResult(minEval, bestMove);
        }
    }

    public static double evaluate(Board board) {
        Piece[] pieces = board.boardToArray();
        int whiteScore = 0;
        int blackScore = 0;
        for(Piece p : pieces){
            if(p.getPieceType() == PieceType.NONE){
                continue;
            }
            if(p.getPieceSide() == Side.WHITE){
                if(p.getPieceType() == PieceType.KING){
                    whiteScore += 1000000;
                }
                else if(p.getPieceType() == PieceType.QUEEN){
                    whiteScore += 900;
                } 
                else if(p.getPieceType() == PieceType.ROOK){
                    whiteScore += 500;
                }
                else if(p.getPieceType() == PieceType.BISHOP){
                    whiteScore += 330;
                }
                else if(p.getPieceType() == PieceType.KNIGHT){
                    whiteScore += 300;
                }
                else if(p.getPieceType() == PieceType.PAWN){
                    whiteScore += 100;
                }
            }
            else if(p.getPieceSide() == Side.BLACK){
                if(p.getPieceType() == PieceType.KING){
                    blackScore += 1000000;
                }
                else if(p.getPieceType() == PieceType.QUEEN){
                    blackScore += 900;
                } 
                else if(p.getPieceType() == PieceType.ROOK){
                    blackScore += 500;
                }
                else if(p.getPieceType() == PieceType.BISHOP){
                    blackScore += 330;
                }
                else if(p.getPieceType() == PieceType.KNIGHT){
                    blackScore += 300;
                }
                else if(p.getPieceType() == PieceType.PAWN){
                    blackScore += 100;
                }
            }
        }
        if(board.isMated()){
            if(board.getSideToMove() == Side.WHITE){
                return MINUS_INFINITY;
            }
            else{
                return INFINITY;
            }
        }
        if(board.isDraw()){
            return 0;
        }
        double eval = (blackScore - whiteScore);
        return eval;
    }
}
