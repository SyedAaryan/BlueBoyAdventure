package object;

import javax.imageio.ImageIO;
import java.util.Objects;

public class OBJ_Key extends SuperObject {

    public OBJ_Key() {

        name = "Key";

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/key.png")));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
