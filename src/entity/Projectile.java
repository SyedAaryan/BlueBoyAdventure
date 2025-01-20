package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {

        super(gp);

    }

    // This method is called when the player or a monster shoots a projectile
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = maxLife;

    }

    //This is used to updated whatever is happening to the entity
    public void update() {

        // To check if the user is the player
        if (user == gp.player) {

            // To get the monster index
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack);
                alive = false;
            }

        }

        if (user != gp.player) {

            // To check if the entity is colliding with the player
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (!gp.player.invincible && contactPlayer){
                damagePlayer(attack);
                alive = false;
            }

        }

        // To change the position (basically the movement of the projectile)
        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }

        /* We have kept the life of this fireball as 80, and the fireball will disappear when this method will be
         *called 80 times, so the fireball will disappear after 80 frames*/
        life--;
        if (life <= 0) {
            alive = false;
        }

        // TO "animate" the projectile by changing images based on the frames
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }

}
