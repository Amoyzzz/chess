import java.util.HashMap;

public class Game {
    private final Piece[] board = new Piece[64];
    private HashMap<String, Integer> places = new HashMap<>();

    public void makeMove(String move) {
        String from = move.substring(0, 2);
        String to = move.substring(3, 5);
        System.out.println(from);
        System.out.println(to);
    }

    @Override
    public String toString() {
        String boardString = "";
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x * 8 + y].getFen().equals("e")) {
                    boardString += "  ";
                } else {
                    boardString += board[x * 8 + y].getFen() + " ";
                }
            }
            boardString += "\n";
        }

        return boardString;
    }
    
    public final void parseFEN(String fen) {
        int l = 0;
        String[] fenArray = fen.split("/");
        for (String fenArray1 : fenArray) {
            char[] row = fenArray1.toCharArray();
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
        places.put("a8", 0);
        places.put("b8", 1);
        places.put("c8", 2);
        places.put("d8", 3);
        places.put("e8", 4);
        places.put("f8", 5);
        places.put("g8", 6);
        places.put("h8", 7);
        places.put("a7", 8);
        places.put("b7", 9);
        places.put("c7", 10);
        places.put("d7", 11);
        places.put("e7", 12);
        places.put("f7", 13);
        places.put("g7", 14);
        places.put("h7", 15);
        places.put("a6", 16);
        places.put("b6", 17);
        places.put("c6", 18);
        places.put("d6", 19);
        places.put("e6", 20);
        places.put("f6", 21);
        places.put("g6", 22);
        places.put("h6", 23);
        places.put("a5", 24);
        places.put("b5", 25);
        places.put("c5", 26);
        places.put("d5", 27);
        places.put("e5", 28);
        places.put("f5", 29);
        places.put("g5", 30);
        places.put("h5", 31);
        places.put("a4", 32);
        places.put("b4", 33);
        places.put("c4", 34);
        places.put("d4", 35);
        places.put("e4", 36);
        places.put("f4", 37);
        places.put("g4", 38);
        places.put("h4", 39);
        places.put("a3", 40);
        places.put("b3", 41);
        places.put("c3", 42);
        places.put("d3", 43);
        places.put("e3", 44);
        places.put("f3", 45);
        places.put("g3", 46);
        places.put("h3", 47);
        places.put("a2", 48);
        places.put("b2", 49);
        places.put("c2", 50);
        places.put("d2", 51);
        places.put("e2", 52);
        places.put("f2", 53);
        places.put("g2", 54);
        places.put("h2", 55);
        places.put("a1", 56);
        places.put("b1", 57);
        places.put("c1", 58);
        places.put("d1", 59);
        places.put("e1", 60);
        places.put("f1", 61);
        places.put("g1", 62);
        places.put("h1", 63);

        //parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        parseFEN("nnnnnnnn/nnnnnnnn/nnnnnnnn/nnnnnnnn/NNNNNNNN/NNNNNNNN/NNNNNNNN/NNNNNNNN");

        GameGUI gui = new GameGUI(board);
    }

    public Game(String fen) {
        places.put("a8", 0);
        places.put("b8", 1);
        places.put("c8", 2);
        places.put("d8", 3);
        places.put("e8", 4);
        places.put("f8", 5);
        places.put("g8", 6);
        places.put("h8", 7);
        places.put("a7", 8);
        places.put("b7", 9);
        places.put("c7", 10);
        places.put("d7", 11);
        places.put("e7", 12);
        places.put("f7", 13);
        places.put("g7", 14);
        places.put("h7", 15);
        places.put("a6", 16);
        places.put("b6", 17);
        places.put("c6", 18);
        places.put("d6", 19);
        places.put("e6", 20);
        places.put("f6", 21);
        places.put("g6", 22);
        places.put("h6", 23);
        places.put("a5", 24);
        places.put("b5", 25);
        places.put("c5", 26);
        places.put("d5", 27);
        places.put("e5", 28);
        places.put("f5", 29);
        places.put("g5", 30);
        places.put("h5", 31);
        places.put("a4", 32);
        places.put("b4", 33);
        places.put("c4", 34);
        places.put("d4", 35);
        places.put("e4", 36);
        places.put("f4", 37);
        places.put("g4", 38);
        places.put("h4", 39);
        places.put("a3", 40);
        places.put("b3", 41);
        places.put("c3", 42);
        places.put("d3", 43);
        places.put("e3", 44);
        places.put("f3", 45);
        places.put("g3", 46);
        places.put("h3", 47);
        places.put("a2", 48);
        places.put("b2", 49);
        places.put("c2", 50);
        places.put("d2", 51);
        places.put("e2", 52);
        places.put("f2", 53);
        places.put("g2", 54);
        places.put("h2", 55);
        places.put("a1", 56);
        places.put("b1", 57);
        places.put("c1", 58);
        places.put("d1", 59);
        places.put("e1", 60);
        places.put("f1", 61);
        places.put("g1", 62);
        places.put("h1", 63);

        parseFEN(fen);
    }
}