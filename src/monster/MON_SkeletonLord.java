package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_SkeletonLord extends Entity {

    GamePanel gp;
    public static final String monName = "Skeleton Lord";

    public MON_SkeletonLord(GamePanel gp) {

        super(gp);

        this.gp = gp;

        type = type_monster;
        name = monName;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 50;
        life = maxLife;
        attack = 10;
        defense = 2;
        exp = 50;
        knockBackPower = 5;

        motion1_duration = 25;
        motion2_duration = 50;

        int size = gp.tileSize * 5;
        solidArea.x = 48;
        solidArea.y = 48;
        solidArea.width = size - 48 * 2;
        solidArea.height = size - 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 170;
        attackArea.height = 170;

        getImage();
        getAttackImage();

    }

    public void getImage() {

        // To make the monster look bigger, we are increasing its size using "i".
        int i = 5;

        if (!inRange) {
            up1 = setup("/monster/skeletonlord_up_1", gp.tileSize * i, gp.tileSize * i);
            up2 = setup("/monster/skeletonlord_up_2", gp.tileSize * i, gp.tileSize * i);
            down1 = setup("/monster/skeletonlord_down_1", gp.tileSize * i, gp.tileSize * i);
            down2 = setup("/monster/skeletonlord_down_2", gp.tileSize * i, gp.tileSize * i);
            left1 = setup("/monster/skeletonlord_left_1", gp.tileSize * i, gp.tileSize * i);
            left2 = setup("/monster/skeletonlord_left_2", gp.tileSize * i, gp.tileSize * i);
            right1 = setup("/monster/skeletonlord_right_1", gp.tileSize * i, gp.tileSize * i);
            right2 = setup("/monster/skeletonlord_right_2", gp.tileSize * i, gp.tileSize * i);
        } else {
            up1 = setup("/monster/skeletonlord_phase2_up_1", gp.tileSize * i, gp.tileSize * i);
            up2 = setup("/monster/skeletonlord_phase2_up_2", gp.tileSize * i, gp.tileSize * i);
            down1 = setup("/monster/skeletonlord_phase2_down_1", gp.tileSize * i, gp.tileSize * i);
            down2 = setup("/monster/skeletonlord_phase2_down_2", gp.tileSize * i, gp.tileSize * i);
            left1 = setup("/monster/skeletonlord_phase2_left_1", gp.tileSize * i, gp.tileSize * i);
            left2 = setup("/monster/skeletonlord_phase2_left_2", gp.tileSize * i, gp.tileSize * i);
            right1 = setup("/monster/skeletonlord_phase2_right_1", gp.tileSize * i, gp.tileSize * i);
            right2 = setup("/monster/skeletonlord_phase2_right_2", gp.tileSize * i, gp.tileSize * i);
        }

    }

    public void getAttackImage() {

        // To make the monster look bigger, we are increasing its size using "i".
        int i = 5;

        if (!inRange) {
            attackUp1 = setup("/monster/skeletonlord_attack_up_1", gp.tileSize * i, gp.tileSize * i * 2);
            attackUp2 = setup("/monster/skeletonlord_attack_up_2", gp.tileSize * i, gp.tileSize * i * 2);
            attackDown1 = setup("/monster/skeletonlord_attack_down_1", gp.tileSize * i, gp.tileSize * i * 2);
            attackDown2 = setup("/monster/skeletonlord_attack_down_2", gp.tileSize * i, gp.tileSize * i * 2);
            attackLeft1 = setup("/monster/skeletonlord_attack_left_1", gp.tileSize * i * 2, gp.tileSize * i);
            attackLeft2 = setup("/monster/skeletonlord_attack_left_2", gp.tileSize * i * 2, gp.tileSize * i);
            attackRight1 = setup("/monster/skeletonlord_attack_right_1", gp.tileSize * i * 2, gp.tileSize * i);
            attackRight2 = setup("/monster/skeletonlord_attack_right_2", gp.tileSize * i * 2, gp.tileSize * i);
        } else {
            attackUp1 = setup("/monster/skeletonlord_phase2_attack_up_1", gp.tileSize * i, gp.tileSize * i * 2);
            attackUp2 = setup("/monster/skeletonlord_phase2_attack_up_2", gp.tileSize * i, gp.tileSize * i * 2);
            attackDown1 = setup("/monster/skeletonlord_phase2_attack_down_1", gp.tileSize * i, gp.tileSize * i * 2);
            attackDown2 = setup("/monster/skeletonlord_phase2_attack_down_2", gp.tileSize * i, gp.tileSize * i * 2);
            attackLeft1 = setup("/monster/skeletonlord_phase2_attack_left_1", gp.tileSize * i * 2, gp.tileSize * i);
            attackLeft2 = setup("/monster/skeletonlord_phase2_attack_left_2", gp.tileSize * i * 2, gp.tileSize * i);
            attackRight1 = setup("/monster/skeletonlord_phase2_attack_right_1", gp.tileSize * i * 2, gp.tileSize * i);
            attackRight2 = setup("/monster/skeletonlord_phase2_attack_right_2", gp.tileSize * i * 2, gp.tileSize * i);
        }

    }

    // Making changes to the setAction class by adding conditions to make the monster go on aggro state
    public void setAction() {

        if (!inRange && life < maxLife / 2) {
            inRange = true;
            getImage();
            getAttackImage();
            defaultSpeed++;
            speed = defaultSpeed;
            attack *= 2;
        }

        if (getTileDistance(gp.player) < 10) {
            moveTowardsPlayer(60);
        } else {
            getRandomDirection(120);
        }

        // Check if it attacks
        if (!attacking) {
            checkAttackOrNot(60, gp.tileSize * 7, gp.tileSize * 5);
        }
    }

    // This is called when the monster gets damaged by the player
    public void damageReaction() {
        actionLockCounter = 0;
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
