import java.util.List;

import backstage.Board;
import backstage.Piece;
import backstage.PieceType;
import backstage.Side;
import backstage.move.Move;

public class AthenaEngine {
    private final static Board board = new Board();
    public static void testEval(){
        String fen = "5k2/1Q6/8/8/8/4K1Q1/8/8 w - - 0 1";
        //Board board = new Board();
        board.loadFromFen(fen);
        //initializeTables();
        System.out.println(board);

        System.out.println(evaluate());
    }
    public static void main(String[] args) {
        testEval();
        // //Board board = new Board();
        // //board.loadFromFen("4k3/8/8/8/8/4KQQ1/8/8 w - - 0 1"); //Input FEN here
        // Scanner in = new Scanner(System.in);
        // //initializeTables();
    
        // while (true) {
        //     // Print the board from the black viewpoint and the evaluation
        //     System.out.println(board.toStringFromBlackViewPoint());    
        //     // Check for end-of-game conditions
        //     if (board.isDraw()) {
        //         System.out.println("DRAW");
        //         break;
        //     }
        //     if (board.isMated()) {
        //         System.out.println("CHECKMATE");
        //         break;
        //     }
    
        //     // Get and execute the best move from the AI (maximizing player)
        //     int eval = search(4, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //     System.out.println(eval);
        //     //automatically plays move
        //     // Print the board after the AI's move
        //     System.out.println(board.toStringFromBlackViewPoint());
        //     System.out.println("\n\n");
    
        //     // Check for end-of-game conditions
        //     if (board.isDraw()) {
        //         System.out.println("DRAW");
        //         break;
        //     }
        //     if (board.isMated()) {
        //         System.out.println("CHECKMATE");
        //         break;
        //     }
    
        //     // Get and execute the player's move (minimizing player)
        //     board.doMove(in.nextLine());
        // }
    
        // in.close();
    }
    
    
    // public static MinimaxResult minimax(Board board, int depth, boolean maximizingPlayer, double alpha, double beta) {
    //     if (depth == 0 || board.isMated() || board.isDraw()) {
    //         return new MinimaxResult(evaluate(), null);
    //     }

    //     if (maximizingPlayer) {
    //         double maxEval = Double.NEGATIVE_INFINITY; // Ensure correct constant for minus infinity
    //         Move bestMove = null;
    //         for (Move move : board.legalMoves()) {
    //             board.doMove(move);
    //             MinimaxResult result = minimax(board, depth - 1, false, alpha, beta);
    //             board.undoMove();

    //             if (result.score > maxEval) {
    //                 maxEval = result.score;
    //                 bestMove = move;
    //             }
    //             alpha = Math.max(alpha, result.score);
    //             if(beta <= alpha){
    //                 break;
    //             }
    //         }
    //         return new MinimaxResult(maxEval, bestMove);
    //     } else {
    //         double minEval = Double.POSITIVE_INFINITY; // Ensure correct constant for positive infinity
    //         Move bestMove = null;
    //         for (Move move : board.legalMoves()) {
    //             board.doMove(move);
    //             MinimaxResult result = minimax(board, depth - 1, true, alpha, beta);
    //             board.undoMove();

    //             if (result.score < minEval) {
    //                 minEval = result.score;
    //                 bestMove = move;
    //             }
    //             beta = Math.min(result.score, beta);
    //             if(beta <= alpha){
    //                 break;
    //             }
    //         }
    //         return new MinimaxResult(minEval, bestMove);
    //     }
    // }
    public static int search(int depth, int alpha, int beta){
        if(depth == 0){
            return evaluate();
        }
        List<Move> moves = board.legalMoves();

        if(moves.isEmpty()){
            if(board.isKingAttacked()){
                return Integer.MIN_VALUE;
            }
            return 0;
        }

        for(Move move : moves){
            board.doMove(move);
            int evaluation = -search(depth - 1, -beta, -alpha);
            board.undoMove();
            if(evaluation >= beta){ //Beta cutoff  
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }

        return alpha;
    }

     // Constants
    private static final int PAWN = 0;
    private static final int KNIGHT = 1;
    private static final int BISHOP = 2;
    private static final int ROOK = 3;
    private static final int QUEEN = 4;
    private static final int KING = 5;

    private static final int WHITE = 0;
    private static final int BLACK = 1;

    private static final int[] gamephaseInc = {0, 0, 1, 1, 1, 1, 2, 2, 4, 4, 0, 0};

    private static final int[][] mgTable = new int[12][64];
    private static final int[][] egTable = new int[12][64];

    // Piece-square tables
    private static final int[] mgPawnTable = {
        0, 0, 0, 0, 0, 0, 0, 0,
        98, 134, 61, 95, 68, 126, 34, -11,
        -6, 7, 26, 31, 65, 56, 25, -20,
        -14, 13, 6, 21, 23, 12, 17, -23,
        -27, -2, -5, 12, 17, 6, 10, -25,
        -26, -4, -4, -10, 3, 3, 33, -12,
        -35, -1, -20, -23, -15, 24, 38, -22,
        0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] egPawnTable = {
        0, 0, 0, 0, 0, 0, 0, 0,
        178, 173, 158, 134, 147, 132, 165, 187,
        94, 100, 85, 67, 56, 53, 82, 84,
        32, 24, 13, 5, -2, 4, 17, 17,
        13, 9, -3, -7, -7, -8, 3, -1,
        4, 7, -6, 1, 0, -5, -1, -8,
        13, 8, 8, 10, 13, 0, 2, -7,
        0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] mgKnightTable = {
        -167, -89, -34, -49, 61, -97, -15, -107,
        -73, -41, 72, 36, 23, 62, 7, -17,
        -47, 60, 37, 65, 84, 129, 73, 44,
        -9, 17, 19, 53, 37, 69, 18, 22,
        -13, 4, 16, 13, 28, 19, 21, -8,
        -23, -9, 12, 10, 19, 17, 25, -16,
        -29, -53, -12, -3, -1, 18, -14, -19,
        -105, -21, -58, -33, -17, -28, -19, -23
    };

    private static final int[] egKnightTable = {
        -58, -38, -13, -28, -31, -27, -63, -99,
        -25, -8, -25, -2, -9, -25, -24, -52,
        -24, -20, 10, 9, -1, -9, -19, -41,
        -17, 3, 22, 22, 22, 11, 8, -18,
        -18, -6, 16, 25, 16, 17, 4, -18,
        -23, -3, -1, 15, 10, -3, -20, -22,
        -42, -20, -10, -5, -2, -20, -23, -44,
        -29, -51, -23, -15, -22, -18, -50, -64
    };

    private static final int[] mgBishopTable = {
        -29, 4, -82, -37, -25, -42, 7, -8,
        -26, 16, -18, -13, 30, 59, 18, -47,
        -16, 37, 43, 40, 35, 50, 37, -2,
        -4, 5, 19, 50, 37, 37, 7, -2,
        -6, 13, 13, 26, 34, 12, 10, 4,
        0, 15, 15, 15, 14, 27, 18, 10,
        4, 15, 16, 0, 7, 21, 33, 1,
        -33, -3, -14, -21, -13, -12, -39, -21
    };

    private static final int[] egBishopTable = {
        -14, -21, -11, -8, -7, -9, -17, -24,
        -8, -4, 7, -12, -3, -13, -4, -14,
        2, -8, 0, -1, -2, 6, 0, 4,
        -3, 9, 12, 9, 14, 10, 3, 2,
        -6, 3, 13, 19, 7, 10, -3, -9,
        -12, -3, 8, 10, 13, 3, -7, -15,
        -14, -18, -7, -1, 4, -9, -15, -27,
        -23, -9, -23, -5, -9, -16, -5, -17
    };

    private static final int[] mgRookTable = {
        32, 42, 32, 51, 63, 9, 31, 43,
        27, 32, 58, 62, 80, 67, 26, 44,
        -5, 19, 26, 36, 17, 45, 61, 16,
        -24, -11, 7, 26, 24, 35, -8, -20,
        -36, -26, -12, -1, 9, -7, 6, -23,
        -45, -25, -16, -17, 3, 0, -5, -33,
        -44, -16, -20, -9, -1, 11, -6, -71,
        -19, -13, 1, 17, 16, 7, -37, -26
    };

    private static final int[] egRookTable = {
        13, 10, 18, 15, 12, 12, 8, 5,
        11, 13, 13, 11, -3, 3, 8, 3,
        7, 7, 7, 5, 4, -3, -5, -3,
        4, 3, 13, 1, 2, 1, -1, 2,
        3, 5, 8, 4, -5, -6, -8, -11,
        -4, 0, -5, -1, -7, -12, -8, -16,
        -6, -6, 0, 2, -9, -9, -11, -3,
        -9, 2, 3, -1, -5, -13, 4, -20
    };

    private static final int[] mgQueenTable = {
        -28, 0, 29, 12, 59, 44, 43, 45,
        -24, -39, -5, 1, -16, 57, 28, 54,
        -13, -17, 7, 8, 29, 56, 47, 57,
        -27, -27, -16, -16, -1, 17, -2, 1,
        -9, -26, -9, -10, -2, -4, 3, -3,
        -14, 2, -11, -2, -5, 2, 14, 5,
        -35, -8, 11, 2, 8, 15, -3, 1,
        -1, -18, -9, 10, -15, -25, -31, -50
    };

    private static final int[] egQueenTable = {
        -9, 22, 22, 27, 27, 19, 10, 20,
        -17, 20, 32, 41, 58, 25, 30, 0,
        -20, 6, 9, 49, 47, 35, 19, 9,
        3, 22, 24, 45, 57, 40, 57, 36,
        -18, 28, 19, 47, 31, 34, 39, 23,
        -16, -27, 15, 6, 9, 17, 10, 5,
        -22, -23, -30, -16, -16, -23, -36, -32,
        -33, -28, -22, -43, -5, -32, -20, -41
    };

    private static final int[] mgKingTable = {
        -65, 23, 16, -15, -56, -34, 2, 13,
        29, -1, -20, -7, -8, -4, -38, -29,
        -9, 24, 2, -16, -20, 6, 22, -22,
        -17, -20, -12, -27, -30, -25, -14, -36,
        -49, -1, -27, -39, -46, -44, -33, -51,
        -14, -14, -22, -46, -44, -30, -15, -27,
        1, 7, -8, -64, -43, -16, 9, 8,
        -15, 36, 12, -54, 8, -28, 24, 14
    };

    private static final int[] egKingTable = {
        -74, -35, -18, -18, -11, 15, 4, -17,
        -12, 17, 14, 17, 17, 38, 23, 11,
        10, 17, 23, 15, 20, 45, 44, 13,
        -8, 22, 24, 27, 26, 33, 26, 3,
        -18, -4, 21, 24, 27, 23, 9, -11,
        -19, -3, 11, 21, 23, 16, 7, -9,
        -27, -11, 4, 13, 14, 4, -5, -17,
        -53, -34, -21, -11, -28, -14, -24, -43
    };

    static {
        initializeTables();
    }

    private static void initializeTables() {
        initializePieceTable(PAWN, mgPawnTable, egPawnTable);
        initializePieceTable(KNIGHT, mgKnightTable, egKnightTable);
        initializePieceTable(BISHOP, mgBishopTable, egBishopTable);
        initializePieceTable(ROOK, mgRookTable, egRookTable);
        initializePieceTable(QUEEN, mgQueenTable, egQueenTable);
        initializePieceTable(KING, mgKingTable, egKingTable);
    }

    private static void initializePieceTable(int piece, int[] mgTableData, int[] egTableData) {
        for (int sq = 0; sq < 64; sq++) {
            int flipSq = (piece == PAWN) ? sq : flipSquare(sq);
            mgTable[piece][sq] = mgTableData[flipSq];
            egTable[piece][sq] = egTableData[flipSq];
            mgTable[piece + 6][sq] = -mgTableData[flipSq];
            egTable[piece + 6][sq] = -egTableData[flipSq];
        }
    }

    private static int flipSquare(int sq) {
        return ((sq / 8) * 8) + (7 - (sq % 8));
    }

    private static int pieceTypeToIndex(PieceType type) {
        switch (type) {
            case PAWN -> {
                return 0;
            }
            case KNIGHT -> {
                return 1;
            }
            case BISHOP -> {
                return 2;
            }
            case ROOK -> {
                return 3;
            }
            case QUEEN -> {
                return 4;
            }
            case KING -> {
                return 5;
            }
            default -> throw new IllegalArgumentException("Unknown piece type");
        }
    }

    public static int evaluate() {
        if (board.isMated()) {
            System.out.println("CHECKMATE LOL!!! ");
            // Checkmate condition
            if (board.getSideToMove() == Side.WHITE) {
                return Integer.MAX_VALUE; // White is checkmated
            } else {
                return Integer.MIN_VALUE; // Black is checkmated
            }
        }

        if(board.isDraw()){
            return 0;
        }
        int[] mg = new int[2];
        int[] eg = new int[2];
        int gamePhase = 0;

        // Initialize mg and eg for each color
        mg[WHITE] = 0;
        mg[BLACK] = 0;
        eg[WHITE] = 0;
        eg[BLACK] = 0;

        // Get the pieces from the board
        Piece[] pieces = board.boardToArray();

        // Evaluate each piece
        for (int sq = 0; sq < 64; ++sq) {
            Piece piece = pieces[sq];
            if (piece != null) {
                PieceType type = piece.getPieceType();
                Side side = piece.getPieceSide();
                
                if (type != null && side != null) {
                    int color = (side == Side.WHITE) ? 0 : 1;
                    int pieceIndex = pieceTypeToIndex(type);
                    mg[color] += mgTable[pieceIndex][sq];
                    eg[color] += egTable[pieceIndex][sq];
                    gamePhase += gamephaseInc[pieceIndex];
                }
            }
        }

        // Compute the tapered evaluation
        int mgScore = mg[WHITE] - mg[BLACK];
        int egScore = eg[WHITE] - eg[BLACK];
        int mgPhase = Math.min(gamePhase, 24);
        int egPhase = 24 - mgPhase;
        return (mgScore * mgPhase + egScore * egPhase) / 24;
    }

    private static int forceKingToCornerEndgameEval(Board board, int kingSquare, int oppponentKingSquare, int gamePhase) {
        Piece[] pieces = board.boardToArray();
        int evaluation = 0;

        int opponentKingRank = (int)(kingSquare/8);
        int opponentKingFile = (kingSquare%8); 
    
        int opponentKingDstToCenterFile = Math.max(3 - opponentKingFile, opponentKingFile - 4);
        int opponentKingDstToCenterRank = Math.max(3 - opponentKingRank, opponentKingRank - 4);
        int opponentKingDstFromCenter = opponentKingDstToCenterFile + opponentKingDstToCenterRank;
        evaluation += opponentKingDstFromCenter;
        

        int friendlyKingRank = (int)(oppponentKingSquare/8); //we need a position to rank function
        int friendlyKingFile = (oppponentKingSquare%8); //we need a position to file function

        
        int KingsFileDst = Math.abs(friendlyKingFile - opponentKingFile);
        int KingsRankDst = Math.abs(friendlyKingRank - opponentKingRank);
        int totalDst = KingsRankDst + KingsFileDst;


        evaluation += 14 - totalDst;
        
        return (int)(evaluation * 10 * (1/gamePhase));

    }
}
