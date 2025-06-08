package monster;

import debugger.Debugger;
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
        defaultSpeed = 1;
        speed = defaultSpeed;
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

    // Making changes to the setAction class by adding conditions to make the monster go on aggro state
    public void setAction() {
        if (onPath) {
            // If the monster is chasing and the player is 15 tiles away from the monster, it stops the chase
            checkStopChasingOrNot(gp.player, 15, 100);

            // To follow the player
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

            // The monster is in aggro
            checkShootOrNot(200, 30);
        } else {
            // the monster is not chasing right now and of the player is close to the monster (i,e 5 tiles),
            // there is a 50% chance that the monster may go aggro
            checkStartChasingOrNot(gp.player, 5, 100);

            getRandomDirection(120);
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
