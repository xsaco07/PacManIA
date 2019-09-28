import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

abstract class Images {

    private static HashMap<String, String> imageFor = new HashMap<>(){{
       put( PacMan.class.getName(), "resources/imgs/pacman2.png");
       put( Block.class.getName(), "resources/imgs/brick.jpeg");
       put( Fruit.class.getName(), "resources/imgs/fruit3.png");
       put( Ghost.class.getName(), "resources/imgs/ghost4.png");
       put( Blank.class.getName(), "resources/imgs/square3.JPG");
       put( PathCell.class.getName(), "resources/imgs/pathCell.png");
    }};

    static BufferedImage getImageFor(String symbol){
        BufferedImage image = null;
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
