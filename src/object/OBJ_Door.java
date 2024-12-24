package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OBJ_Door extends SuperObject {

    private static final Logger logger = Logger.getLogger(OBJ_Door.class.getName());

    public OBJ_Door(GamePanel gp) {

        name = "Door";

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/door.png")));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading image: " + name, e);
        }

        collision = true;

    }

}
