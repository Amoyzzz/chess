public class Board {
    private Piece[] board = new Piece[64];

    public Board() {
        int l;
        board[0] = new Rook(0, false);
        board[1] = new Knight(1, false);
        board[2] = new Bishop(2, false);
        board[3] = new Queen(3, false);
        board[4] = new King(4, false);
        board[5] = new Bishop(5, false);
        board[6] = new Knight(6, false);
        board[7] = new Rook(7, false);

        for (l = 8; l < 16; l++) {
            board[l] = new Pawn(l, false);
        }

        for (l = 16; l < 48; l++) {
            board[l] = new Pawn(l, false);
        }


        for (l = 8; l < 16; l++) {
            board[l] = new Piece(l, true);
        }
    }
}
