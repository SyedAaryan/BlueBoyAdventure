package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OBJ_Boots extends SuperObject {

    private static final Logger logger = Logger.getLogger(OBJ_Boots.class.getName());

    public OBJ_Boots(GamePanel gp) {

        name = "Boots";

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/boots.png")));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading image: " + name, e);
        }

    }

}
