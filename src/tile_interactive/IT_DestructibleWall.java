package tile_interactive;

import entity.Entity;
import main.GamePanel;
import java.awt.*;

public class IT_DestructibleWall extends InteractiveTile {

    GamePanel gp;

    public IT_DestructibleWall(GamePanel gp, int col, int row) {

        super(gp, col, row);
        this.gp = gp;
        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        life = 3;
        down1 = setup("/tiles_interactive/destructiblewall", gp.tileSize, gp.tileSize);
        destructible = true;

    }

    // Returns true if the current weapon is an axe
    public boolean isCorrectItem(Entity entity) {
        return entity.currentWeapon.type == type_pickaxe;
    }

    public void playSE() {
        gp.playSE(11);
    }

    // We do this so that the "trunk" shows up in the same location where the tree was
    public InteractiveTile getDestroyedForm() {
        return null;
    }

    public Color getParticleColor() {
        return new Color(65, 65, 65);
    }

    public int getParticleSize() {
        return 6; // 6 pixels
    }

    public int getParticleSpeed() {
        return 1; // speed
    }

    public int getParticleMaxLife() {
        return 20;
    }

}
