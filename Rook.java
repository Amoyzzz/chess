public class Rook extends Piece {
    @Override
    int getValue() { return 500; }

    @Override
    char getType() { return isWhite ? 'R' : 'r'; }
}
