package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 2;

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getNPCImage();
        setDialogue();

    }

    public void getNPCImage() {

        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);

    }

    public void setDialogue() {

        dialogues[0] = "Hello, Lad";
        dialogues[1] = "So you have come to this island to \nfind the treasure ";
        dialogues[2] = "I used to be a great wizard but now....... \nI am a bit too old for an adventure";
        dialogues[3] = "Well, GOOD LUCK !!!";

    }

    public void setAction() {

        if (onPath) {

//            int goalCol = 12;
//            int goalRow = 9;

            // To follow the player
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);

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

    public void speak() {

        super.speak();
        onPath = true;

    }

}
