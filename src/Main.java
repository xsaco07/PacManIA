import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        int rows = 10, cols = 10;
        int cellSize = 60;
        State s = new State(cols,rows);

        s.put(new PacMan(1, 0));

        s.put(new Fruit(2, 4));
        s.put(new Fruit(3, 8));
        s.put(new Fruit(9, 1));

        for (int i = 0; i < 7; i++) {
            s.put(new Block(3, i));
            s.put(new Block(5, 9 - i));
            s.put(new Block(7, 1));
            s.put(new Block(9, 9 - i));
        }


        s.put(new Ghost(5, 2));
        s.put(new Ghost(9, 2));


        JFrame frame = new JFrame("PacManIA");

        frame.setBounds(200,100,rows*cellSize,rows*cellSize+25);

        frame.setBackground(Color.BLACK);

        frame.add( new MyPanel(s, cellSize) );

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }


}
