package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    public static final String objName = "Key";
    GamePanel gp;

    public OBJ_Key(GamePanel gp) {

        super(gp);
        this.gp = gp;

        name = objName;
        type = type_consumable;
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        price = 10;
        stackable = true;

        description = "[ " + name + "]\nIt opens a door";

        setDialogue();

    }

    public void setDialogue() {

        dialogues[0][0] = "You used the " + name + " and opened the door";

        dialogues[1][0] = " What are you doing ??!!";

    }

    public boolean use(Entity entity) {
        int objIndex = getDetected(entity, gp.obj, "Door");

        if (objIndex != 999) {
            startDialogue(this, 0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        } else {
            startDialogue(this, 1);
            return false;
        }

    }

}
