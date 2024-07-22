public class King extends Piece {
    @Override
    int getValue() { return 1000000; }

    @Override
    char getType() { return isWhite ? 'K' : 'k'; }
}
