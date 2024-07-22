package piece;

import main.GamePanel;
import main.Type;

public class Knight extends Piece{
    public Knight(int index, int color) {
        super(index, color);

        type = Type.KNIGHT;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-knight");
        }else {
            image = getImage("/piece/b-knight");
        }
    }
    public boolean canMove(int targetCol, int targetRow){

        if (isWithThinBoard(targetCol, targetRow)){
            //Knight can move if its movement ratio of col and row 1:2 or 2:1
            if (Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2){
                if (isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
