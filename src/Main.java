import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String args[]){
        int rows = 12, cols = rows;

        State s = new State(cols,rows);
        s.put(0,0, Symbols.PACMAN);

        s.put(3,4, Symbols.FRUIT);
        s.put(3,10, Symbols.FRUIT);
        s.put(9,1, Symbols.FRUIT);

        for (int i = 0; i < 7; i++) {
            s.put(3, i, Symbols.OBSTACLE);
            s.put(5, 11-i, Symbols.OBSTACLE);
            s.put(7, i, Symbols.OBSTACLE);
            s.put(9, 11-i, Symbols.OBSTACLE);
        }


        s.put(5,2, Symbols.GHOST);
        s.put(9,2, Symbols.GHOST);


        JFrame frame = new JFrame("PacManIA");

        frame.setBounds(200,100,600,635);

        frame.add( new MyPanel(s, 50) );

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }


}
