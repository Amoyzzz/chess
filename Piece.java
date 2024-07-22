public class Piece {
    private int location;
    private boolean white;

    public Piece(int location, boolean white) {
        this.location = location;
        this.white = white;
    }

    public int getlocation() {
        return location;
    }

    public void setlocation(int location) {
        this.location = location;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
}
