package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {

    public static final String objName = "Bronze Coin";
    GamePanel gp;

    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = objName;
        value = 1;
        down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);

    }

    // This method will handle what will happen if we use this item
    public boolean use(Entity entity) {

        gp.playSE(1);
        gp.ui.addMessage("Coin + " + value);
        gp.player.coin += value;
        return true;
    }

}
