import javax.swing.*;
import java.awt.*;

public class Main {

    private static JFrame frame;

    public static void main(String[] args){
        int rows = 10, cols = 10;
        int cellSize = 60;

        Grid grid = new Grid(rows, cols, new PacMan(0, 0));

        initializeGUI(grid, rows, cols, cellSize);
        System.out.println(grid.fruitsNodes.size());
        Game game = new Game(grid, frame);
        game.play();
    }

    private static void initializeGUI(Grid grid, int rows, int cols, int cellSize) {
        grid.put(new Fruit(5, 2));
        grid.put(new Fruit(0, 5));

        for (int i = 0; i < 7; i++) {
            grid.put(new Block(3, i));
            grid.put(new Block(5, 9 - i));
            grid.put(new Block(7, 1));
            grid.put(new Block(9, 9 - i));
        }

        grid.put(new Ghost(2, 7));
        grid.put(new Ghost(9, 2));
        grid.put(new Ghost(7, 6));

        frame = new JFrame("PacManIA");

        frame.setBounds(200,100,cols*cellSize,rows*cellSize+25);

        frame.setBackground(Color.BLACK);

        frame.add(new MyPanel(grid, cellSize));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

}
