package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);

        description = "[ " + name + "]\nPotion that HEALS \nyour health by " + value;
        price = 100;

    }

    // This method will handle what will happen if we use this item
    public boolean use(Entity entity) {

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drank the " + name + "!\nYour life has been recovered by " + value + "!";
        entity.life += value;
        gp.playSE(2);
        return true;

    }

}
