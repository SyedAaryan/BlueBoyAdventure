package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class InteractiveTile extends Entity {

    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {

        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity) {
        return false;
    }

    public void playSE() {
    }

    public InteractiveTile getDestroyedForm() {

        return null;

    }

    public void update() {

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    /* We are overwriting the draw method from the entity class, since entities class draw method has transparency effect
    whe the player hits the object, and we don't want that in teh case of interactive tiles*/
    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (
                worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY
        ) {
            g2.drawImage(down1, screenX, screenY, null);
        }

    }

}
