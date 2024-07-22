public class Queen extends Piece {
    @Override
    int getValue() { return 900; }

    @Override
    char getType() { return isWhite ? 'Q' : 'q'; }
}
