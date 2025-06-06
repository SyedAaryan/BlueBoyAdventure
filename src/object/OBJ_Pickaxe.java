package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Pickaxe extends Entity {

    public static final String objName = "Pickaxe";

    public OBJ_Pickaxe(GamePanel gp) {

        super(gp);

        type = type_pickaxe;
        name = objName;
        down1 = setup("/objects/pickaxe", gp.tileSize, gp.tileSize);
        attackValue = 2;

        description = "[ " + name + "]\nYou will dig with it.";
        price = 75;

        //Solid attack area of the weapon
        attackArea.width = 30;
        attackArea.height = 30;

        knockBackPower = 5;

        motion1_duration = 10;
        motion2_duration = 20;

    }

}
