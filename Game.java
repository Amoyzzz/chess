
import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private final Piece[] board = new Piece[64];
    private final HashMap<String, Integer> places = new HashMap<>();

    public boolean makeMove(String move) {
        Move.init();
        if (!move.contains("O-O") && !move.contains("resign") && !move.contains("draw") && !move.contains("e.p") && !move.contains("=")) {
        } else {
            System.out.println("not implemented yet");
            return false;
        }
        String from = move.substring(0, 2);
        String to = move.substring(3, 5);
        int startPos = places.get(from);
        int endPos = places.get(to);
        ArrayList<Integer> moves = possibleMoves(startPos);
        if (!moves.contains(endPos)) {
            System.out.println("bro is tweaking");
            return false;
        } else {
            board[endPos] = board[startPos];
            board[startPos] = new Empty(startPos, "e");
            board[endPos].moved();
            board[endPos].setLocation(endPos);
        }
        return true;
    }
    public boolean isStalemateWhite(){
        if(isWhiteKingInCheck() || isBlackKingInCheck()){
            return false;
        }
        for(int i = 0; i < 64; i++){
            if(board[i].getFen().charAt(0) != 'e' && Character.isUpperCase(board[i].getFen().charAt(0))){
                if(!possibleMoves(i).isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
     public boolean isStalemateBlack(){
        if(isWhiteKingInCheck() || isBlackKingInCheck()){
            return false;
        }
        for(int i = 0; i < 64; i++){
            if(board[i].getFen().charAt(0) != 'e' && Character.isLowerCase(board[i].getFen().charAt(0))){
                if(!possibleMoves(i).isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isWhiteKingInCheck(){
        int location = 0;
        for(int i = 0; i < 64; i++){
            if(board[i].getFen().equals("K")){
                location = i;
            }
        }
        for(int i = 0; i < 64; i++){
            if(i == location || board[i].getFen().equals("e")){
            }
            else{
                if(possibleMoves(i).contains(location)){
                    return true;
                }
            }
        }
        return false;
    }
     public boolean isBlackKingInCheck(){
        int location = 0;
        for(int i = 0; i < 64; i++){
            if(board[i].getFen().equals("k")){
                location = i;
            }
        }
        for(int i = 0; i < 64; i++){
            if(i == location || board[i].getFen().equals("e")){
            }
            else{
                if(possibleMoves(i).contains(location)){
                    return true;
                }
            }
        }
        return false;
    }

    //TODO: check for blockers
    public ArrayList<Integer> possibleMoves(int location) { //doesn't factor in check, stalemate, or checkmate so i have to deal with that
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        switch (board[location].getFen().toUpperCase()) {
            case "Q" -> possibleMoves = Move.queenMoves[location];
            case "R" -> possibleMoves = Move.rookMoves[location];
            case "B" -> possibleMoves = Move.bishopMoves[location];
            case "K" -> possibleMoves = Move.kingMoves[location];
            case "N" -> possibleMoves = Move.knightMoves[location];
            case "P" -> possibleMoves = Move.pawnMoves(location, board[location].getFen());
        }
        return possibleMoves;
    }

    @Override
    public String toString() {
        String boardString = "";
        for (int x = 7; x >= 0; x--) {
            boardString += (x + 1) + "   ";
            for (int y = 0; y < 8; y++) {
                if (board[x * 8 + y].getFen().equals("e")) {
                    boardString += "  ";
                } else {
                    boardString += board[x * 8 + y].getFen() + " ";
                }
            }
            boardString += "\n";
        }
        boardString += "\n    a b c d e f g h\n\n";
        return boardString;
    }

    public final void parseFEN(String fen) {
        int l = 0;
        String[] fenArray = fen.split("/");
        for (int i = 7; i >= 0; i--) {
            char[] row = fenArray[i].toCharArray();
            for (int y = 0; y < row.length; y++) {
                if (Character.isDigit(row[y])) {
                    for (int c = 0; c < row[y] - '0'; c++) {
                        board[l] = new Empty(l, "e");
                        l++;
                    }
                } else {
                    switch (row[y]) {
                        case 'p' -> {
                            board[l] = new Pawn(l, "p");
                            l++;
                        }
                        case 'r' -> {
                            board[l] = new Rook(l, "r");
                            l++;
                        }
                        case 'n' -> {
                            board[l] = new Knight(l, "n");
                            l++;
                        }
                        case 'b' -> {
                            board[l] = new Bishop(l, "b");
                            l++;
                        }
                        case 'q' -> {
                            board[l] = new Queen(l, "q");
                            l++;
                        }
                        case 'k' -> {
                            board[l] = new King(l, "k");
                            l++;
                        }
                        case 'P' -> {
                            board[l] = new Pawn(l, "P");
                            l++;
                        }
                        case 'R' -> {
                            board[l] = new Rook(l, "R");
                            l++;
                        }
                        case 'N' -> {
                            board[l] = new Knight(l, "N");
                            l++;
                        }
                        case 'B' -> {
                            board[l] = new Bishop(l, "B");
                            l++;
                        }
                        case 'Q' -> {
                            board[l] = new Queen(l, "Q");
                            l++;
                        }
                        case 'K' -> {
                            board[l] = new King(l, "K");
                            l++;
                        }
                        default -> {
                        }
                    }
                }
            }
        }
    }

    public Game() {
        initialize();
        parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }

    public Game(String fen) {
        initialize();
        parseFEN(fen);
    }

    public final void initialize() {
        places.put("a1", 0);
        places.put("b1", 1);
        places.put("c1", 2);
        places.put("d1", 3);
        places.put("e1", 4);
        places.put("f1", 5);
        places.put("g1", 6);
        places.put("h1", 7);
        places.put("a2", 8);
        places.put("b2", 9);
        places.put("c2", 10);
        places.put("d2", 11);
        places.put("e2", 12);
        places.put("f2", 13);
        places.put("g2", 14);
        places.put("h2", 15);
        places.put("a3", 16);
        places.put("b3", 17);
        places.put("c3", 18);
        places.put("d3", 19);
        places.put("e3", 20);
        places.put("f3", 21);
        places.put("g3", 22);
        places.put("h3", 23);
        places.put("a4", 24);
        places.put("b4", 25);
        places.put("c4", 26);
        places.put("d4", 27);
        places.put("e4", 28);
        places.put("f4", 29);
        places.put("g4", 30);
        places.put("h4", 31);
        places.put("a5", 32);
        places.put("b5", 33);
        places.put("c5", 34);
        places.put("d5", 35);
        places.put("e5", 36);
        places.put("f5", 37);
        places.put("g5", 38);
        places.put("h5", 39);
        places.put("a6", 40);
        places.put("b6", 41);
        places.put("c6", 42);
        places.put("d6", 43);
        places.put("e6", 44);
        places.put("f6", 45);
        places.put("g6", 46);
        places.put("h6", 47);
        places.put("a7", 48);
        places.put("b7", 49);
        places.put("c7", 50);
        places.put("d7", 51);
        places.put("e7", 52);
        places.put("f7", 53);
        places.put("g7", 54);
        places.put("h7", 55);
        places.put("a8", 56);
        places.put("b8", 57);
        places.put("c8", 58);
        places.put("d8", 59);
        places.put("e8", 60);
        places.put("f8", 61);
        places.put("g8", 62);
        places.put("h8", 63);
    }
}
