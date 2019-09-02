import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    private Grid grid;
    private int cellSize;

    public MyPanel(Grid grid, int cellSize){
        this.grid = grid;
        this.cellSize = cellSize;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        for (int row = 0; row < grid.height; row++){
            for (int col = 0; col < grid.width; col++) {

                Image image = Images.getImageFor(grid.cells[row][col].getClass().getName());

                g.drawImage(image, row*cellSize, col*cellSize, cellSize, cellSize, null);
            }
        }
    }
}
