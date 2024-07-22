package piece;

import main.GamePanel;
import main.Type;

public class King extends Piece {
    public King(int index, int color) {
        super(index, color);

        type = Type.KING;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-king");
        } else {
            image = getImage("/piece/b-king");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithThinBoard(targetCol, targetRow)) {
            // Movement
            if (Math.abs(targetCol - preCol) +
                Math.abs(targetRow - preRow) == 1 ||
                Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1) {

                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }

            // Castling
            if (!moved) {
                // Regular Castling
                if (targetCol == preCol + 2 && targetRow == preRow && !pieceIsOnStraightLine(targetCol, targetRow)) {
                    for (Piece piece : GamePanel.simPieces) {
                        // Check if any piece can attack the king's path
                        if ((piece.canMove(this.row, this.col) && piece.color != this.color) ||
                            (piece.canMove(this.row, this.col + 1) && piece.color != this.color) ||
                            (piece.canMove(this.row, this.col + 2) && piece.color != this.color)) {
                            return false;
                        }
                        if (piece.col == preCol + 3 && piece.row == preRow && !piece.moved && piece.type == Type.ROOK && piece.color == this.color) {
                            GamePanel.castlingP = piece;
                            return true;
                        }
                    }
                }

                // Queen Side Castling
                if (targetCol == preCol - 2 && targetRow == preRow && !pieceIsOnStraightLine(targetCol, targetRow)) {
                    Piece[] p = new Piece[2];
                    for (Piece piece : GamePanel.simPieces) {
                        // Check if any piece can attack the king's path
                        if ((piece.canMove(this.row, this.col) && piece.color != this.color) ||
                            (piece.canMove(this.row, this.col - 1) && piece.color != this.color) ||
                            (piece.canMove(this.row, this.col - 2) && piece.color != this.color)) {
                            return false;
                        }
                        if (piece.col == preCol - 3 && piece.row == targetRow) {
                            p[0] = piece;
                        }
                        if (piece.col == preCol - 4 && piece.row == targetRow) {
                            p[1] = piece;
                        }
                        if (p[0] == null && p[1] != null && !p[1].moved && p[1].type == Type.ROOK && p[1].color == this.color) {
                            GamePanel.castlingP = p[1];
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
