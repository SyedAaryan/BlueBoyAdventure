package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OBJ_Heart extends SuperObject {

    private static final Logger logger = Logger.getLogger(OBJ_Heart.class.getName());

    public OBJ_Heart(GamePanel gp) {

        name = "Heart";

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_full.png")));
            image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_half.png")));
            image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_blank.png")));

            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize, gp.tileSize);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading image: " + name, e);
        }

    }

}
