package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_GreenSlime extends Entity {

    GamePanel gp;

    public MON_GreenSlime(GamePanel gp) {

        super(gp);

        this.gp = gp;

        type = type_monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_Rock(gp);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();

    }

    public void getImage() {

        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);

    }

    // Making changes to the update class by adding conditions to make the monster go on aggro state
    public void update() {
        super.update();

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        // !onPath means its not chasing right now and of the player is close to the monster (i,e 5 tiles),
        // there is a 50% chance that the monster may go aggro
        if (!onPath && tileDistance < 5) {
            int i = new Random().nextInt(100) + 1;
            if (i > 50) {
                onPath = true;
            }
        }

        // If the monster is chasing and the player is 20 tiles away from the monster, it stops the chase
//        if(onPath && tileDistance > 20){
//            onPath = false;
//        }
    }

    public void setAction() {

        if (onPath) {

            // To follow the player
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);

            // The monster is in aggro
            int i = new Random().nextInt();
            if (i > 197 && !projectile.alive && shotAvailableCounter == 30) {

                projectile.set(worldX, worldY, direction, true, this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;

            }

        } else {
            actionLockCounter++;

            // 120 cause 120 frames, i,e 2 seconds
            if (actionLockCounter == 120) {

                Random random = new Random();
                int i = random.nextInt(100) + 1; // Pick any number from 1 to 100

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75) {
                    direction = "right";
                }

                actionLockCounter = 0;

            }
        }

    }

    // This is called when the monster gets damaged by the player
    public void damageReaction() {

        actionLockCounter = 0;
//        direction = gp.player.direction;
        onPath = true;

    }

    // This is called when the monster dies to check if it drops something
    public void checkDrop() {

        // Cast a die
        int i = new Random().nextInt(100) + 1;

        //Setting the monsters drop
        // The dropping will occur randomly
        if (i < 50) {
            dropItem(new OBJ_Coin_Bronze(gp));
        }

        if (i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gp));
        }

        if (i >= 75) {
            dropItem(new OBJ_ManaCrystal(gp));
        }

    }

}
