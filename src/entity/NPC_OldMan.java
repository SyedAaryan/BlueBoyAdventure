package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 1;

        getNPCImage();
        setDialogue();

    }

    public void getNPCImage() {

        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");

    }

    public void setDialogue() {

        dialogues[0] = "Hello, Lad";
        dialogues[1] = "So you have come to this island to \nfind the treasure ";
        dialogues[2] = "I used to be a great wizard but now....... \nI am a bit too old for an adventure";
        dialogues[3] = "Well, GOOD LUCK !!!";

    }

    public void setAction() {

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

    public void speak() {

        super.speak();

    }

}
