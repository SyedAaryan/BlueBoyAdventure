package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;
    int value = 5;

    public OBJ_Potion_Red(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);

        description = "[ " + name + "]\nPotion that HEALS \nyour health by " + value;

    }

    // This method will handle what will happen if we use this item
    public void use(Entity entity) {

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drank the " + name + "!\nYour life has been recovered by " + value + "!";
        entity.life += value;
        if (gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(2);

    }

}
