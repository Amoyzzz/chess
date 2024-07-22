public class Bishop extends Piece {
    @Override
    int getValue() { return 330; }

    @Override
    char getType() { return isWhite ? 'B' : 'b'; }
}
