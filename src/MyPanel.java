import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MyPanel extends JPanel {

    private Grid grid;
    private int cellSize;

    MyPanel(Grid grid, int cellSize){
        this.grid = grid;
        this.cellSize = cellSize;
    }

    void setCellSize(int cellSize){
        this.cellSize = cellSize;
    }

    void setGrid(Grid newGrid) {grid = newGrid;}

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int row = 0; row < grid.height; row++){
            for (int col = 0; col < grid.width; col++) {

                String imageName = grid.cells[row][col].getClass().getName();
                BufferedImage image = Images.getImageFor(imageName);

                Font myFont = new Font ("Courier New", Font.BOLD, 17);
                g.setFont(myFont);

                g.drawImage(image, row*cellSize, col*cellSize, cellSize, cellSize, null);
            }
        }
    }
}
