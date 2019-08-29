import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    private State state;
    private int cellSize;

    public MyPanel(State state, int cellSize){
        this.state = state;
        this.cellSize = cellSize;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        for (int row = 0 ; row < state.height; row++){
            for (int col = 0 ; col < state.width; col++) {

                Image image = Images.getImageFor(state.cells[row][col].getClass().getName());

                g.drawImage(image, row*cellSize, col*cellSize, cellSize, cellSize, null);
            }
        }
    }
}
