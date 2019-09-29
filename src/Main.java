import javax.swing.*;
import java.awt.*;

public class Main{

    private static JFrame frame;

    // This is put in a class attributes because couldn't reach it through the JFrame
    private static MyPanel myPanel;

    public static void main(String[] args){

        int rows = 12, cols = 12;
        int cellSize = 60;

        Grid grid = new Grid(rows, cols);

        initializeGUI(grid, cellSize);
        System.out.println(grid.fruitsNodes.size());
        Game game = new Game(grid, frame);
        game.play();
    }

    /**
     * Initializes the JFrame
     * @param grid
     * @param cellSize
     */
    private static void initializeGUI(Grid grid, int cellSize) {

        frame = new JFrame("PacManIA");

        frame.setBackground(Color.BLACK);

        myPanel = new MyPanel(grid, cellSize);

        frame.add(myPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resetUI(grid, cellSize); // call resetUI here to avoid duplicating code

        frame.setVisible(true);

        System.out.println("GUI Inited");
    }


    /**
     * Adjusts the window to show the new grid and cellSize
     * - Resets the MyPanel's grid and cellsize attributes
     * - Resizes the frame to match the new dimensions
     * @param grid The new grid to show
     * @param cellSize The new cellsize to render the window
     */
    private static void resetUI(Grid grid, int cellSize){
        // Update de JPanel's attributes
        myPanel.setGrid(grid);
        myPanel.setCellSize(cellSize);


        // Don't know why it's necessary to add this to the frame's height to be displayed properly
        int EXTRA_HEIGHT = 30;

        // Resize the frame
        frame.setBounds(200,100, grid.width*cellSize,grid.height*cellSize+EXTRA_HEIGHT);

        System.out.println("UI reset");
    }



}
