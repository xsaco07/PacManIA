import javax.swing.*;
import java.awt.*;

public class Main{

    private static JFrame frame;


    public static void test(){
        Foo f = new Foo();

        VoiceManager.register(f);
        VoiceManager.start();

        try{
            Thread.sleep(30 * 1000);
            System.out.println("Main thread finished");
            VoiceManager.stop();
        }catch (Exception e){
            System.out.println( "Main Thread Interrupted" );
        }

    }

    public static void main(String[] args){

        test();
        return;

        int rows = 10, cols = 10;
        int cellSize = 60;
        initializeGUI(grid, rows, cols, cellSize);
        Game game = new Game(grid, frame);
        game.play();
    }

    private static void initializeGUI(Grid grid, int rows, int cols, int cellSize) {
        grid.put(new Fruit(7, 8));
        grid.put(new Fruit(1, 7));
        grid.put(new Fruit(3, 7));
        grid.put(new Fruit(9, 9));
        grid.put(new Fruit(9, 0));

        for (int i = 0; i < 4; i++) {
            grid.put(new Block(1, i));
            grid.put(new Block(4, 9 - i));
            grid.put(new Block(9-i, 4));
            grid.put(new Block(8, 9 - i));
        }

        grid.put(new Ghost(5, 4));
        grid.put(new Ghost(9, 4));
        grid.put(new Ghost(7, 6));
        grid.put(new Ghost(2, 6));
        grid.put(new Ghost(2, 9));

        frame = new JFrame("PacManIA");

        frame.setBounds(200,100,cols*cellSize,rows*cellSize+25);

        frame.setBackground(Color.BLACK);

        MyPanel myPanel = new MyPanel(grid, cellSize);
        myPanel.setBackground(new Color(0, 0, 29));
        frame.add(myPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }





}
