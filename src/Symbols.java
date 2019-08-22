import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public abstract class Symbols {
    public static final char PACMAN = 'C';
    public static final char GHOST = 'O';
    public static final char OBSTACLE = '#';
    public static final char FRUIT = 'F';
    public static final char EMPTY = '-';

    private static HashMap<Character, String> imageFor = new HashMap<>(){{
       put( PACMAN, "imgs/pacman.jpeg");
       put( OBSTACLE, "imgs/brick.jpeg");
       put( FRUIT, "imgs/fruit.jpeg");
       put( GHOST, "imgs/ghost.png");
       put( EMPTY, "imgs/white.jpeg");
    }};

    public static Image getImageFor( char symbol){
        Image image = null;
        try{
            image  = ImageIO.read(new File(imageFor.get(symbol)));
        }
        catch(Exception e){
            System.out.println("Error loading image");
            e.printStackTrace();
        }
        return image;
    }
}
