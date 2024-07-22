public class Piece {
    private int location;
    private String fen; //e is empty

    public Piece(int location, String fen) {
        this.location = location;
        this.fen = fen;
    }

    public int getlocation() {
        return location;
    }

    public void setlocation(int location) {
        this.location = location;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
}