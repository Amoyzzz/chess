public class Knight extends Piece {
    int getValue() { return 320; }

    char getType() { return isWhite ? 'N' : 'n'; }
}
