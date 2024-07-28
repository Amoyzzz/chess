import backstage.Board;
import backstage.Piece;
import backstage.PieceType;
import backstage.Side;
import backstage.Square;
import backstage.move.Move;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AthenaEngine {
    private static final double INFINITY = 1000000.0;
    static Move bestMove;
    private static final int DEPTH_USED = 6;
    private static long numTranspositions = 0;
    private static final HashMap<Long, Double> transpositions = new HashMap<>();
    
    
    public static void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Board board = new Board();
        board.loadFromFen("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -"); //Input FEN here
        bestMove = null;
        try (Scanner in = new Scanner(System.in)) {
            initTables();
            
            while (true) {
                if (board.isMated()) {
                    playSound("win.wav"); // Play checkmate sound
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(board.toStringFromBlackViewPoint());
                System.out.println("\n\n");
                
                // Check for end-of-game conditions
                if (board.isDraw()) {
                    System.out.println("DRAW");
                    break;
                }
                if (board.isMated()) {
                    System.out.println("CHECKMATE");
                    break;
                }
                
                // Get and execute the best move from the AI (maximizing player)
                double starttime = System.currentTimeMillis();
                search(board, DEPTH_USED);
                System.out.println("Transpositions: " + numTranspositions);
                System.out.println("Legal moves: " + board.legalMoves());
                System.out.println("Chosen move: " + bestMove);
                board.doMove(bestMove);
                double elapsedTime = System.currentTimeMillis() - starttime;
                double elapsedSeconds = elapsedTime / 1000;
                System.out.println(elapsedSeconds);


                if (board.isMated()) {
                    playSound("win.wav"); // Play checkmate sound
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                // Print the board after the AI's move
                System.out.println(board.toStringFromBlackViewPoint());
                System.out.println("\n\n");
                
                // Check for end-of-game conditions
                if (board.isDraw()) {
                    System.out.println("DRAW");
                    break;
                }
                if (board.isMated()) {
                    System.out.println("CHECKMATE");
                    break;
                }
                
                // Get and execute the player's move (minimizing player)
                board.doMove(in.nextLine());
            }
        }
    }

    public static void OrderMoves(List<Move> moves) {
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move m1, Move m2) {
                return Integer.compare(m2.getScore(), m1.getScore()); // For descending order
            }
        });
    }
    
    public static double search(Board board, int depth) {
        double maxEval = -INFINITY;
        int count = 0;
        List<Move> moves = board.legalMoves();
        for (Move move: moves){
            move.setScore(MoveValue(board, move));
        }
        OrderMoves(moves);
        System.out.println(moves);
        for (Move move: moves) {
            if(count == 0){
                bestMove = move;
            }
            count++;
            board.doMove(move);
            double result = -minimax(board, depth - 1, true, -INFINITY, INFINITY, move);
            board.undoMove();
            System.out.println(move + " -> " + result);

            if (result > maxEval) {
                maxEval = result;
                bestMove = move;
            }
        }
        return maxEval;
    }

    public static double minimax(Board board, int depth, boolean maximizingPlayer, double alpha, double beta, Move testMove) {
        

        if (depth == 0 || board.isMated() || board.isDraw()) {
            return eval(board, board.getSideToMove() == Side.WHITE ? 0 : 1, DEPTH_USED - depth, testMove);
        }
        

        if (maximizingPlayer) {
            double maxEval = -INFINITY;
            for (Move move : board.legalMoves()) {
                board.doMove(move);
                double result = minimax(board, depth - 1, false, alpha, beta, testMove);
                board.undoMove();

                if (result > maxEval) {
                    maxEval = result;
                }
                alpha = Math.max(alpha, result);
                
                if(beta < alpha){
                    break;
                }
            }
            return maxEval;
        } else {
            double minEval = INFINITY;
            for (Move move : board.legalMoves()) {
                board.doMove(move);
                double result = minimax(board, depth - 1, true, alpha, beta, testMove);
                board.undoMove();

                if (result < minEval) {
                    minEval = result;
                }
                beta = Math.min(result, beta);
                if(beta < alpha){
                    break;
                }
            }
            return minEval;
        }
    }

    public static double eval(Board board, int sideToMove, int movesPlayed, Move testMove) {
        long zobristKey = board.getZobristKey();
        if (transpositions.containsKey(zobristKey)){
            numTranspositions++;
            return transpositions.get(zobristKey);
        }
        if (board.isMated() && sideToMove == 1) {
            //System.out.println("found a mate");
            return -INFINITY / 10 + movesPlayed;
        } else if(board.isDraw()){
            return 0;
        } else if (board.isMated() && sideToMove == 0) {
            return INFINITY / 10 + movesPlayed;
        }

        int[] mg = {0, 0};
        int[] eg = {0, 0};
        int gamePhase = 0;
        int beginSquare = 0;

        int friendlyKingSquare = 0;
        int opponentKingSquare = 0;
        for (Piece piece : board.boardToArray()) {
            if (piece.getFenSymbol().equals(".")) {
                beginSquare++;
                continue;
            }
            int color = piece.getPieceSide() == Side.WHITE ? 0 : 1;
            int pieceType = getPieceValue(piece.getFenSymbol());
            int sq = beginSquare;
            beginSquare++;
            int mgValue = mgValue(pieceType);
            int egValue = egValue(pieceType);
            mg[color] += mgValue + mgPestoTable[color][pieceType][sq];
            eg[color] += egValue + egPestoTable[color][pieceType][sq];
            gamePhase += gamePhaseInc(pieceType);

            if(piece.getPieceType() == PieceType.KING && color == sideToMove) {
                friendlyKingSquare = beginSquare;
            }
            if(piece.getPieceType() == PieceType.KING && color != sideToMove) {
                opponentKingSquare = beginSquare;
            }
        }
        eg[sideToMove] += forceKingToCornerEndgameEval(friendlyKingSquare, opponentKingSquare, gamePhase);
        
        int mgScore = mg[sideToMove] - mg[1 - sideToMove];
        int egScore = eg[sideToMove] - eg[1 - sideToMove];
        double finalScore = (mgScore * gamePhase + egScore * (24 - gamePhase)) / 24;

        transpositions.put(zobristKey, -(finalScore - movesPlayed));
        
        return -(finalScore - movesPlayed);
    }

    

   private static final int[][][] mgPestoTable = new int[2][6][64];
   private static final int[][][] egPestoTable = new int[2][6][64];
   
   public static void initTables() {
       for (int i = 0; i < 64; i++) {
           mgPestoTable[0][0][i] = mgPawnTable[i];
           mgPestoTable[0][1][i] = mgKnightTable[i];
           mgPestoTable[0][2][i] = mgBishopTable[i];
           mgPestoTable[0][3][i] = mgRookTable[i];
           mgPestoTable[0][4][i] = mgQueenTable[i];
           mgPestoTable[0][5][i] = mgKingTable[i];
           mgPestoTable[1][0][i] = mgPawnTable[mirror(i)];
           mgPestoTable[1][1][i] = mgKnightTable[mirror(i)];
           mgPestoTable[1][2][i] = mgBishopTable[mirror(i)];
           mgPestoTable[1][3][i] = mgRookTable[mirror(i)];
           mgPestoTable[1][4][i] = mgQueenTable[mirror(i)];
           mgPestoTable[1][5][i] = mgKingTable[mirror(i)];

           egPestoTable[0][0][i] = egPawnTable[i];
           egPestoTable[0][1][i] = egKnightTable[i];
           egPestoTable[0][2][i] = egBishopTable[i];
           egPestoTable[0][3][i] = egRookTable[i];
           egPestoTable[0][4][i] = egQueenTable[i];
           egPestoTable[0][5][i] = egKingTable[i];
           egPestoTable[1][0][i] = egPawnTable[mirror(i)];
           egPestoTable[1][1][i] = egKnightTable[mirror(i)];
           egPestoTable[1][2][i] = egBishopTable[mirror(i)];
           egPestoTable[1][3][i] = egRookTable[mirror(i)];
           egPestoTable[1][4][i] = egQueenTable[mirror(i)];
           egPestoTable[1][5][i] = egKingTable[mirror(i)];
       }
   }

   private static int mirror(int square) {
       return (square ^ 0x38);
   }

   public static int getPieceValue(String fen) {
        return switch (fen.toUpperCase()) {
            case "P" -> 0;
            case "N" -> 1;
            case "B" -> 2;
            case "R" -> 3;
            case "Q" -> 4;
            case "K" -> 5;
            default -> -1;
        };
   }
   
    private static int mgValue(int pieceType) {
        // Return the middle game value of the piece type
        return switch (pieceType) {
            case 0 -> 82;
            case 1 -> 337;
            case 2 -> 365;
            case 3 -> 477;
            case 4 -> 1025;
            case 5 -> 0;
            default -> 0;
        }; // PAWN
        // KNIGHT
        // BISHOP
        // ROOK
        // QUEEN
        // KING
    }

    private static int egValue(int pieceType) {
        // Return the end game value of the piece type
        return switch (pieceType) {
            case 0 -> 94;
            case 1 -> 281;
            case 2 -> 297;
            case 3 -> 512;
            case 4 -> 936;
            case 5 -> 0;
            default -> 0;
        }; // PAWN
        // KNIGHT
        // BISHOP
        // ROOK
        // QUEEN
        // KING
    }

    private static int gamePhaseInc(int pieceType) {
        // Return the game phase increment value for the piece type
        return switch (pieceType) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 1;
            case 3 -> 2;
            case 4 -> 4;
            case 5 -> 0;
            default -> 0;
        }; // PAWN
        // KNIGHT
        // BISHOP
        // ROOK
        // QUEEN
        // KING
    }
    
    private static int getPieceValueReal(Piece piece) {
        return switch (piece.getFenSymbol().toUpperCase()) {
            case "P" -> 94;
            case "N" -> 281;
            case "B" -> 297;
            case "R" -> 512;
            case "Q" -> 936;
            case "K" -> 100000;
            case "." -> 0;
            default -> 0;
        };
    }
    private static int MoveValue(Board board, Move move) {

        int moveScoreGuess = 0;

        int movePieceType = getPieceValueReal(board.getPiece(move.getFrom()));
        int capturePieceType = getPieceValueReal(board.getPiece(move.getTo()));


        // Prioritize capturing opponent's most valuable pieces with our least valuable pieces
        if (capturePieceType > movePieceType) {
            // Capturing a more valuable piece
            moveScoreGuess = 10 * capturePieceType - movePieceType;
        } else if (capturePieceType < movePieceType) {
            // Capturing a less valuable piece
            moveScoreGuess = 5;
        } else if (capturePieceType == movePieceType){
            // Capturing an equally valuable piece
            moveScoreGuess = 7;
        }

        // Promoting a pawn is likely to be good
        if (move.getPromotion() != Piece.NONE) {
            moveScoreGuess += 100; // Assuming a high value for promotion
        }

            // Penalize moving pieces to a square attacked by a pawn
        if (isAttackedByPawn(board, move.getTo(), board.getSideToMove())) {
            moveScoreGuess -= 20; // Arbitrary penalty value
        }

        return moveScoreGuess;
    }

    private static boolean isAttackedByPawn(Board board, Square toSquare, Side sideToMove) {
    // Get the opponent's side
    Side opponentSide = sideToMove == Side.WHITE ? Side.BLACK : Side.WHITE;

    // Convert rank and file to integers
    int toRow = toSquare.getRank().ordinal();
    int toCol = toSquare.getFile().ordinal();

    int[][] pawnAttackDeltas = opponentSide == Side.WHITE 
        ? new int[][] { {1, -1}, {1, 1} } // White pawns attack diagonally upwards
        : new int[][] { {-1, -1}, {-1, 1} }; // Black pawns attack diagonally downwards

    // Check if any opponent's pawn is attacking the destination square
    for (int[] delta : pawnAttackDeltas) {
        int attackRow = toRow + delta[0];
        int attackCol = toCol + delta[1];

        if (isValidSquare(attackRow, attackCol)) {
            String attackSquareStr = "" + (char) ('a' + attackCol) + (8 - attackRow); // Correct rank conversion
            Square attackSquare = Square.fromValue(attackSquareStr.toUpperCase());
            Piece piece = board.getPiece(attackSquare);

            if (piece.getPieceType() == PieceType.PAWN && piece.getPieceSide() == opponentSide) {
                return true;
            }
        }
    }

    return false;
}

    private static boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }


    private static int forceKingToCornerEndgameEval(int kingSquare, int oppponentKingSquare, int gamePhase) {
        int evaluation = 0;

        int opponentKingRank = (int)(oppponentKingSquare/8);
        int opponentKingFile = (oppponentKingSquare%8); 
    
        int opponentKingDstToCenterFile = Math.max(3 - opponentKingFile, opponentKingFile - 4);
        int opponentKingDstToCenterRank = Math.max(3 - opponentKingRank, opponentKingRank - 4);
        int opponentKingDstFromCenter = opponentKingDstToCenterFile + opponentKingDstToCenterRank;
        evaluation += opponentKingDstFromCenter;
        

        int friendlyKingRank = (int)(kingSquare/8); //we need a position to rank function
        int friendlyKingFile = (kingSquare%8); //we need a position to file function

        
        int KingsFileDst = Math.abs(friendlyKingFile - opponentKingFile);
        int KingsRankDst = Math.abs(friendlyKingRank - opponentKingRank);
        int totalDst = KingsRankDst + KingsFileDst;


        evaluation += 14 - totalDst;
        if(gamePhase == 0){
            return evaluation * 10 * 10 * 1;
        }
        return (int)(evaluation * 10 * (1/gamePhase + 2));
    }


    private static final int[] mgPawnTable = {
        0,   0,   0,   0,   0,   0,   0,   0,
       98, 134,  61,  95,  68, 126,  34, -11,
       -6,   7,  26,  31,  65,  56,  25, -20,
       -14, 13,   6,  21,  23,  12,  17, -23,
       -2,  -2,  -5,  12,  17,   6,  10, -2,
       -2, -4,  -4,  -10,  3,   3,  33, -2,
       -35, -1, -20, -23, -15,  24,  38, -22,
        0,   0,   0,   0,   0,   0,   0,   0
   };

   private static final int[] egPawnTable = {
        0,   0,   0,   0,   0,   0,   0,   0,
       178, 173, 158, 134, 147, 132, 165, 187,
       94, 100,  85,  67,  56,  53,  82,  84,
       32,  24,  13,   5,  -2,   4,  17,  17,
       13,   9,  -3,  -7,  -7,  -8,   3,  -1,
        4,   7,  -6,   1,   0, -5, -1,  -8,
       13,   8,   8,  10,  13,   0,   2,  -7,
        0,   0,   0,   0,   0,   0,   0,   0
   };

   private static final int[] mgKnightTable = {
       -167, -89, -34, -49,  61, -97, -15, -107,
       -73,  -41,  72,  36,  23,  62,   7, -17,
       -47,  60,  37,  65,  84, 129,  73,  44,
       -9,  17,  19,  53,  37,  69,  18,  22,
       -13,  4,  16,  13,  28,  19,  21,  -8,
       -23, -9,  12,  10,  19,  17,  25, -16,
       -29, -53, -12, -3,  -1,  18, -14, -19,
       -105, -21, -58, -33, -17, -28, -19, -23
   };

   private static final int[] egKnightTable = {
       -58, -38, -13, -28, -31, -27, -63, -99,
       -25,  -8, -25,  -2, -9, -25, -24, -52,
       -24,  -20,  10,   9, -1,  -9, -19, -41,
       -17,   3,  22,  22, 22,  11,   8,  -18,
       -18,  -6,  16,  25, 16,  17,   4,  -18,
       -23,  -3,  -1,  15, 10,  -3,  -20, -22,
       -42,  -20, -10,  -5,  -2,  -20, -23, -44,
       -29, -51, -23, -15, -22, -18, -50, -64
   };

   private static final int[] mgBishopTable = {
       -29,  4, -82, -37, -25, -42,  7,  -8,
       -26, 16, -18, -13,  30,  59,  18, -47,
       -16, 37,  43,  40,  35,  50,  37,  -2,
       -4,   5,  19,  50,  37,  37,   7,  -2,
       -6,  13,  13,  26,  34,  12,  10,   4,
       0,  15,  15,  15,  14,  27,  18,  10,
       4,  15,  16,   0,   7,  21,  33,   1,
       -33, -3, -14, -21, -13, -12, -39, -21
   };

   private static final int[] egBishopTable = {
       -14, -21, -11,  -8, -7,  -9, -17, -24,
       -8,  -4,   7,  -12, -3,  -13, -4,  -14,
       2,  -8,   0,  -1,  -2,   6,   0,  4,
       -3,  9,   12,   9,  14,   10,  3,   2,
       -6,  3,  13,  19,   7,   10,  3,  -9,
       -12,  -3,   8,  10,  13,   3,   5, -3,
       -15,  -3,  -4,   2,  -2,  -6,  -2,  -10,
       -16,  -17,  -11,  -4,  -14,  -12, -15, -10
   };

   private static final int[] mgRookTable = {
       32,  42,  32,  51,  63,  9,  31,  43,
       27,  32,  58,  62,  80,  67,  26,  44,
       -5,  19,  26,  36,  17,  45,  61,   16,
       -24,  -11,   7,  26,  24,  35,   2,  -12,
       -36,  -26,  -12,   -1,  9, -7,   6, -23,
       -45,  -25,  -16,  -17,  3,  0,  -5, -33,
       -44,  -16,  -20,  -9,  -1,  11,  -6,  -71,
       -19,  -13,   1,  17,  16,  7, -37, -26
   };

   private static final int[] egRookTable = {
       13,  10,  18,  15,  12,  12,   8,   5,
       11,  13,  13,  11,  -3,   3,   8,   3,
       7,   7,   7,   5,   4,  -3,  -5,  -3,
       4,   3,  13,   1,   2,   1,  -1,   2,
       3,   5,   8,   4,  -5,  -6,  -8, -11,
       -4,   0,  -5,  -1,  -7,  -12, -8,  -16,
       -6,  -6,   0,   2,  -9,  -9, -11,  -3,
       -9,  2,   3,  -1,  -5,  -13,  4,  -20
   };

   private static final int[] mgQueenTable = {
       -28,   0,  29,  12,  59,  44,  43,  45,
       -24, -39,  -5,   1,  -16,  57,  28,  54,
       -13,  -17,   7,   8,  29,  56,  47,  57,
       -27,  -27,  -16,  -16,  -1,  17,  -2,   1,
       -9,  -26,  -9,  -10,  -2,  -4,   3,  -3,
       -14,   2,  -11,  -2,  -5,   2,  14,   5,
       -35,  -8,  11,   2,   8,  15, -3,   1,
       -1,  -18,  -9,  10,  -15, -25,  -31, -50
   };

   private static final int[] egQueenTable = {
       -9,  22,  22,  27,  27,  19,  10,  20,
       -17,  20,  32,  41,  58,  25,  30,   0,
       -20,   6,   9,  49,  47,  35,  19,   9,
       3,  22,  24,  45,  57,  40,  57,  36,
       -18,  28,  19,  47,  31,  34,  39,  23,
       -16, -27,  15,   6,   9,  17,  10,   5,
       -22,  -23, -30, -16, -16,  -23, -36, -32,
       -33,  -28, -22, -43,  -5,  -32, -20, -41
   };

   private static final int[] mgKingTable = {
       -65,  23,  16, -15, -56, -34,   2,  13,
       29,  -1,  -20,  -7,  -8, -4, -38, -29,
       -9,  24,   2,  -16, -20,   6,  22, -22,
       -17,  -20,  -12,  -27,  -30,  -25,  -14, -36,
       -49,  -1,  -27,  -39,  -46,  -44,  -33,  -51,
       -14,  -14,  -22,  -46,  -44,  -30,  -15,  -27,
       1,   7,  -8,  -64,  -43,  -16,   9,   8,
       -15,  36,  12, -54,   8,  -28,  24,  14
   };

   private static final int[] egKingTable = {
       -74, -35, -18, -18, -11,  15,   4, -17,
       -12,  17,  14,  17,  17,  38,  23,  11,
       10,  17,  23,  15,  20,  45,  44,  13,
       -8,  22,  24,  27,  26,  33,  26,   3,
       -18,  -4,  21,  24,  27,  23,   9,  -11,
       -19,  -3,  11,  21,  23,  16,   7,  -9,
       -27,  -11,   4,  13,  14,   4,  -5, -17,
       -53,  -34, -21,  -11, -28, -14, -24, -43
   };
}