package entity;

import main.GamePanel;
import object.*;

public class NPC_Merchant extends Entity {

    public NPC_Merchant(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 1;

        getNPCImage();
        setDialogue();
        setItems();

    }

    public void getNPCImage() {

        up1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);

    }

    public void setDialogue() {

        dialogues[0][0] = "He he boii, so you found me.\nI have some good stuff.\nYou wanna trade??!!";

        dialogues[1][0] = "Come Again, Hehe";

        dialogues[2][0] = "You need more coin to buy that brdr";

        dialogues[3][0] = "Bruh you cannot carry any more ";

        dialogues[4][0] = "You cannot sell equipped items";

    }

    public void setItems() {

        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Shield_Wood(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_Axe(gp));

    }

    public void speak() {

        gp.gameState = gp.tradeState;
        gp.ui.npc = this;

    }

}
