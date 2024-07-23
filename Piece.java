import java.awt.Graphics2D;
import java.awt.Image;

public class Piece {
    private int location;
    private String fen;
    private boolean moved = false;
    //private BufferedImage sheet = null;
    private Image sprite;
    //private int sheetScale;

    public Piece(int location, String fen) {
        this.location = location;
        this.fen = fen;
    }

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getFen() {
        return this.fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void moved() {
        moved = true;
    }

    @Override
    public String toString() {
        return this.fen;
    }

    public void paint(Graphics2D g2d) {
        int row = this.location / 8;
        int col = this.location % 8;
        if (this.sprite != null) {
            g2d.drawImage(sprite, col * 85, row * 85, null);
        }
    }
}
