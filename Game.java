public class Game {
    private Piece[] board = new Piece[64];

    public Game() {
        int l;
        board[0] = new Rook(0, "r");
        board[1] = new Knight(1, "n");
        board[2] = new Bishop(2, "b");
        board[3] = new Queen(3, "q");
        board[4] = new King(4, "k");
        board[5] = new Bishop(5, "b");
        board[6] = new Knight(6, "n");
        board[7] = new Rook(7, "r");

        for (l = 8; l < 16; l++) {
            board[l] = new Pawn(l, "p");
        }

        for (l = 16; l < 48; l++) {
            board[l] = new Empty(l, "e");
        }

        for (l = 48; l < 56; l++) {
            board[l] = new Pawn(l, "P");
        }

        board[56] = new Rook(56, "R");
        board[57] = new Knight(57, "N");
        board[58] = new Bishop(58, "B");
        board[59] = new Queen(59, "Q");
        board[60] = new King(60, "K");
        board[61] = new Bishop(61, "B");
        board[62] = new Knight(62, "N");
        board[63] = new Rook(63, "R");
    }

    public void parseFEN(String fen) {
        int l = 0;
        String[] fenArray = fen.split("/");
        for (int x = 0; x < fenArray.length; x++) {
            char[] row = fenArray[x].toCharArray();
            for (int y = 0; y < row.length; y++) {
                if (Character.isDigit(row[y])) {
                    for (int c = 0; c < row[y] - '0'; c++) {
                        board[l] = new Empty(l, "e");
                        l++;
                    }
                } else {
                    if (row[y] == 'p') {
                        board[l] = new Pawn(l, "p");
                        l++;
                    } else if (row[y] == 'r') {
                        board[l] = new Rook(l, "r");
                        l++;
                    } else if (row[y] == 'n') {
                        board[l] = new Knight(l, "n");
                        l++;
                    } else if (row[y] == 'b') {
                        board[l] = new Bishop(l, "b");
                        l++;
                    } else if (row[y] == 'q') {
                        board[l] = new Queen(l, "q");
                        l++;
                    } else if (row[y] == 'k') {
                        board[l] = new King(l, "k");
                        l++;
                    } else if (row[y] == 'P') {
                        board[l] = new Pawn(l, "P");
                        l++;
                    } else if (row[y] == 'R') {
                        board[l] = new Rook(l, "R");
                        l++;
                    } else if (row[y] == 'N') {
                        board[l] = new Knight(l, "N");
                        l++;
                    } else if (row[y] == 'B') {
                        board[l] = new Bishop(l, "B");
                        l++;
                    } else if (row[y] == 'Q') {
                        board[l] = new Queen(l, "Q");
                        l++;
                    } else if (row[y] == 'K') {
                        board[l] = new King(l, "K");
                        l++;
                    }
                }
            }
        }
    }

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
}