package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fireball extends Projectile {

    public static final String objName = "Fireball";
    GamePanel gp;

    public OBJ_Fireball(GamePanel gp) {

        super(gp);
        this.gp = gp;

        name = objName;
        speed = 8;
        maxLife = 80;
        life = maxLife;
        attack = 1;
        knockBackPower = 5;
        useCost = 1;
        alive = false;

        getImage();

    }

    // To get the image of the fireball
    public void getImage() {

        up1 = setup("/projectile/fireball_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/fireball_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/fireball_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/fireball_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/fireball_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/fireball_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);

    }

    // To check if the player has enough mana
    public boolean hasResource(Entity user) {

        return user.mana >= useCost;

    }

    // To reduce the players mana
    public void subtractResource(Entity user) {
        user.mana -= useCost;
    }

    public Color getParticleColor() {
        return new Color(240, 50, 0);
    }

    public int getParticleSize() {
        return 10; // 6 pixels
    }

    public int getParticleSpeed() {
        return 1; // speed
    }

    public int getParticleMaxLife() {
        return 20;
    }

}
