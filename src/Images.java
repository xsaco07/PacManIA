import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.BatchUpdateException;
import java.util.HashMap;

abstract class Images {

    private static HashMap<String, String> filenameFor = new HashMap<>(){{
       put( PacMan.class.getName(), "resources/imgs/pacman2.png");
       put( Block.class.getName(), "resources/imgs/brick.jpeg");
       put( Fruit.class.getName(), "resources/imgs/fruit3.png");
       put( Ghost.class.getName(), "resources/imgs/ghost3.PNG");
       put( Blank.class.getName(), "resources/imgs/square3.JPG");
       put( PathCell.class.getName(), "resources/imgs/point.jpg");
    }};

    private static HashMap<String, BufferedImage> imageFor = new HashMap<String, BufferedImage>();

    static BufferedImage getImageFor(String symbol){
        BufferedImage image = null;
        try{
            image = imageFor.get(symbol);
            if( image == null){ //Image not cached
                image = ImageIO.read(new File(filenameFor.get(symbol)));
                imageFor.put(symbol, image);
            }
        }
        catch(Exception e){
            System.out.println("Error loading image");
            e.printStackTrace();
        }
        return image;
    }
}
