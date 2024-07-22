import java.util.*;

public class Board {
    List<Piece> pieces = new ArrayList<>();
    
    public Board() {
        parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }

    public int evaluate() {
        return pieces.stream()
                     .mapToInt(p -> (p.isWhite ? 1 : -1) * p.getValue())
                     .sum();
    }

    public void printBoard() {
        int index = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                System.out.print(pieces.get(index).getType() + " ");
                index++;
            }
            System.out.println();
        }
    }

    public void parseFEN(String fen) {
        pieces.clear();
        String[] rows = fen.split("/");
        int y = 0;
        for (String row : rows) {
            int x = 0;
            for (char ch : row.toCharArray()) {
                if (Character.isDigit(ch)) {
                    for (int c = 0; c < ch - '0'; c++) {
                        Piece piece = createPiece(' ', x, y);
                        if (piece != null) {
                            pieces.add(piece);
                        }
                        x++;
                    }
                } else {
                    Piece piece = createPiece(ch, x, y);
                    if (piece != null) {
                        pieces.add(piece);
                    }
                    x++;
                }
            }
            y++;
        }
    }

    private Piece createPiece(char ch, int x, int y) {
        Piece piece = null;
        switch (Character.toLowerCase(ch)) {
            case 'p': piece = new Pawn(); break;
            case 'n': piece = new Knight(); break;
            case 'b': piece = new Bishop(); break;
            case 'r': piece = new Rook(); break;
            case 'q': piece = new Queen(); break;
            case 'k': piece = new King(); break;
            case ' ': piece = new Empty(); break;
        }
        if (piece != null) {
            piece.isWhite = Character.isUpperCase(ch);
            piece.x = x;
            piece.y = y;
        }
        return piece;
    }
}
