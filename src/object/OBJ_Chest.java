package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp) {

        super(gp);
        this.gp = gp;

        name = "Chest";
        type = type_obstacle;
        image = setup("/objects/chest", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);
        down1 = image;
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    public void setLoot(Entity loot) {
        this.loot = loot;
        setDialogue();
        // We are calling setDialogue here because it has "loot", and if we call it from OBJ_Chest, it will return null
    }

    public void setDialogue() {
        dialogues[0][0] = "You opened a chest and found a" + loot.name + "!\n..But you cannot carry any more";

        dialogues[1][0] = "You opened a chest and found a" + loot.name + "\nYou obtained the" + loot.name + "!";

        dialogues[2][0] = "Its Empty :(";
    }

    public void interact() {

        if (!opened) {
            gp.playSE(3);

            if (!gp.player.canObtainItem(loot)) {
                startDialogue(this, 0);
            } else {
                startDialogue(this, 1);
                down1 = image2;
                opened = true;
            }

        } else {
            startDialogue(this, 2);
        }

    }

}
