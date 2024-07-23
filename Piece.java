import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Piece {
    private int location;
    private String fen; // e is empty
    private BufferedImage sheet = null;
    private Image sprite;
    // private Board bd;
    protected int sheetScale;

    public Piece(int location, String fen) {
        this.location = location;
        this.fen = fen;
        loadSheet(); // Load the sheet when the object is created
        if (sheet != null) {
            sheetScale = sheet.getWidth() / 6;
        }
        // Initialize sprite based on fen and sheet
        initSprite();
        JFrame frame = new JFrame("Sheet Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (sheet != null) {
                    g.drawImage(sheet, 0, 0, this);
                }
            }
        });
        frame.setVisible(true);
    }

    private void loadSheet() {
        try {
            sheet = ImageIO.read(new File("piece.png"));
        } catch (IOException e) {}
    }

     private void initSprite() {
        if (sheet != null && fen != null && !fen.equals("e")) {
            // Map FEN characters to the appropriate sprite index
            String pieces = "KQRBNPkqrbnp";
            int index = pieces.indexOf(fen);
            if (index >= 0) {
                int row = Character.isUpperCase(fen.charAt(0)) ? 0 : sheetScale; // 0 for white, sheetScale for black
                int col = index % 6;
                sprite = sheet.getSubimage(col * sheetScale, row, sheetScale, sheetScale)
                              .getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);
            }
        }
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
    @Override
    public String toString(){
        return this.fen;
    }
    public void paint(Graphics2D g2d) {
        int row = location / 8;
        int col = location % 8;
        if (sprite != null) {
            g2d.drawImage(sprite, col * 85, row * 85, null);
        }
    }
}
