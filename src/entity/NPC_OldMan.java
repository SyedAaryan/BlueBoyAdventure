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
        dialogueSet = -1;

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

        dialogues[0][0] = "Hello, Lad";
        dialogues[0][1] = "So you have come to this island to \nfind the treasure ";
        dialogues[0][2] = "I used to be a great wizard but now....... \nI am a bit too old for an adventure";
        dialogues[0][3] = "Well, GOOD LUCK !!!";

        dialogues[1][0] = "If you are tired, rest at the water";
        dialogues[1][1] = "But monsters also re-appear if you rest,\n Idk how that works";
        dialogues[1][2] = "In any case, done push yourself";

        dialogues[2][0] = "I wonder how to open the door";

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

        facePlayer();
        startDialogue(this, dialogueSet);

        dialogueSet++;

        if (dialogues[dialogueSet][0] == null) {
            dialogueSet--;
        }

    }

}
