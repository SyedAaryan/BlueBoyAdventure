package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    public OBJ_Axe(GamePanel gp) {

        super(gp);

        type = type_axe;
        name = "Woodcutters Axe";
        down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;

        description = "[ " + name + "]\nRusty but can cut \nsome trees.";

        //Solid attack area of the weapon
        attackArea.width = 30;
        attackArea.height = 30;

    }

}
