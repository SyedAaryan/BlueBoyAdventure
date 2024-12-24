package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OBJ_Chest extends SuperObject {

    private static final Logger logger = Logger.getLogger(OBJ_Chest.class.getName());

    public OBJ_Chest(GamePanel gp) {

        name = "Chest";

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading image: " + name, e);
        }

    }

}
