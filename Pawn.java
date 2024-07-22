public class Pawn extends Piece {
    @Override
    int getValue() { return 100; }

    @Override
    char getType() { return isWhite ? 'P' : 'p'; }
}
