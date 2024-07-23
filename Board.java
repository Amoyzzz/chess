import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Board extends JPanel {
    
    public int tileSize = 85;
    int cols = 8;
    int rows = 8;
    private final Piece[] board;
    public Board(Piece[] board){
        this.board = board;
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        this.setBackground(Color.GREEN);
    }
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                g2d.setColor((c+r) % 2 == 0 ? new Color(235, 210, 183) : new Color(161, 111, 90));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }
        for (Piece piece : board){
            System.out.print(piece);
            if(!piece.getFen().equals("e")){
                piece.paint(g2d);
            }
        }
    }
}