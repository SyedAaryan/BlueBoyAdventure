package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile {

    GamePanel gp;

    public IT_DryTree(GamePanel gp, int col, int row) {

        super(gp, col, row);
        this.gp = gp;
        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        life = 3;
        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        destructible = true;

    }

    // Returns true if the current weapon is an axe
    public boolean isCorrectItem(Entity entity) {
        return entity.currentWeapon.type == type_axe;
    }

    public void playSE() {
        gp.playSE(11);
    }

    // We do this so that the "trunk" shows up in the same location where the tree was
    public InteractiveTile getDestroyedForm() {

        return new IT_Trunk(gp, worldX / gp.tileSize, worldY / gp.tileSize);

    }

}
