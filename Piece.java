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
        // try {
        //     File file = new File("piece.png");
        //     System.out.println("File exists: " + file.exists());
        //     System.out.println("Absolute path: " + file.getAbsolutePath());
            
        //     // Load the image using ImageIO
        //     this.sheet = ImageIO.read(file);

        //     this.sheetScale = sheet.getWidth() / 6;

        //     if (fen != null && !fen.equals("e")) {
        //         String pieces = "KQBNRPkqbnrp";
        //         int index = pieces.indexOf(fen);
        //         if (index >= 0) {
        //             int row = Character.isUpperCase(fen.charAt(0)) ? 0 : this.sheetScale;
        //             int col = index % 6;
        //             // Crop and scale the sprite
        //             BufferedImage subimage = sheet.getSubimage(col * this.sheetScale, row, this.sheetScale, this.sheetScale);
        //             this.sprite = subimage.getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        //         }
        //     }
        // }
        // catch(IOException e){}
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
