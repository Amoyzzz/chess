package piece;

import main.GamePanel;
import main.Type;

public class Rook extends Piece{
    boolean hasMoved = false;
    public Rook(int index, int color) {
        super(index, color);
        type = Type.ROOK;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-rook");
        }else {
            image = getImage("/piece/b-rook");
        }
    }

    public boolean canMove(int targetCol, int targetRow){
        if (isWithThinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            // Rook can move as long as either its col or row is the same
            if (targetCol == preCol || targetRow == preRow){
                if (isValidSquare(targetCol,targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
