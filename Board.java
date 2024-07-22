public class Board {
    private Piece[] board = new Piece[64];

    public Board() {
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