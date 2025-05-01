package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    GamePanel gp;

    public OBJ_Key(GamePanel gp) {

        super(gp);
        this.gp = gp;

        name = "Key";
        type = type_consumable;
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        price = 10;

        description = "[ " + name + "]\nIt opens a door";

    }

    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        int objIndex = getDetected(entity, gp.obj, "Door");

        if (objIndex != 999) {
            gp.ui.currentDialogue = "You used the " + name + " and opened the door";
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
        } else {
            gp.ui.currentDialogue = " What are you doing ??!!";
        }

    }

}
