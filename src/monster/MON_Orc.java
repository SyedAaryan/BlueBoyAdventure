package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_Orc extends Entity {

    GamePanel gp;

    public MON_Orc(GamePanel gp) {

        super(gp);

        this.gp = gp;

        type = type_monster;
        name = "Orc";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 10;
        life = maxLife;
        attack = 8;
        defense = 2;
        exp = 10;
        knockBackPower = 5;

        motion1_duration = 40;
        motion2_duration = 85;

        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 48;
        attackArea.height = 48;

        getImage();
        getAttackImage();

    }

    public void getImage() {

        up1 = setup("/monster/orc_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/orc_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/orc_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/orc_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/orc_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/orc_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/orc_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/orc_right_2", gp.tileSize, gp.tileSize);

    }

    public void getAttackImage() {
        attackUp1 = setup("/monster/orc_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/monster/orc_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/monster/orc_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/monster/orc_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/monster/orc_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/monster/orc_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/monster/orc_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/monster/orc_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }

    // Making changes to the setAction class by adding conditions to make the monster go on aggro state
    public void setAction() {
        if (onPath) {
            // If the monster is chasing and the player is 15 tiles away from the monster, it stops the chase
            checkStopChasingOrNot(gp.player, 15, 100);

            // To follow the player
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        } else {
            // the monster is not chasing right now and of the player is close to the monster (i,e 5 tiles),
            // there is a 50% chance that the monster may go aggro
            checkStartChasingOrNot(gp.player, 5, 100);

            getRandomDirection(120);
        }

        // Check if it attacks
        if (!attacking) {
            checkAttackOrNot(30, gp.tileSize * 4, gp.tileSize);
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

