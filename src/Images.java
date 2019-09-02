import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public abstract class Images {

//    public static final char PACMAN = 'C';
//    public static final char GHOST = 'O';
//    public static final char OBSTACLE = '#';
//    public static final char FRUIT = 'F';
//    public static final char EMPTY = '-';

    private static HashMap<String, String> imageFor = new HashMap<>(){{
       put( PacMan.class.getName(), "imgs/pacman2.png");
       put( Block.class.getName(), "imgs/brick.jpeg");
       put( Fruit.class.getName(), "imgs/fruit3.png");
       put( Ghost.class.getName(), "imgs/ghost4.png");
       put( Blank.class.getName(), "imgs/square3.JPG");
       put( PathCell.class.getName(), "imgs/pathCell.png");
    }};

    public static Image getImageFor(String symbol){
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
