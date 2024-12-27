package entity;

import debugger.Debugger;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;

    Debugger debugger = new Debugger();

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;

//        worldX = gp.tileSize * 10;
//        worldY = gp.tileSize * 13;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);

    }

    public void getPlayerAttackImage() {

        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);

    }

    public void update() {

        if (attacking) {
            attacking();
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }


            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // CHECK EVENT
            gp.eHandler.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }

            }

            gp.keyH.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
            standCounter++;

            if (standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void attacking() {

        /*So what happens is that we show the 1st attacking image for 5 frames, then the second attacking
         * image is shown for 25 frames, after that its back to image 1, so basically, 1 attack animation
         * lasts for half a second */

        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            /* When we are attacking, we need the solid area of the weapon and not the player, so we are
             * storing the players solid area and his world x and y and temporarily focusing on the solid
             * area of the weapon*/
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust the players worldX/Y for the attack area
            switch (direction) {
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.width;
                case "right" -> worldX += attackArea.width;
            }

            // Attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with the updated worldX, worldY, and solidArea
            /*So what happens here is that, we are checking if the attack area (this was explained above)
             * collides with the monster, this is done using checkEntity(), if it is in fact a monster,
             * we damage it using damageMonster()*/
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            standCounter = 0;
            attacking = false;
        }

    }

    public void pickObject(int i) {
        if (i != 999) {

        }
    }

    public void interactNPC(int i) {

        if (keyH.enterPressed) {
            if (i != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            } else {
                    gp.playSE(7);
                attacking = true;
            }
        }

    }

    public void contactMonster(int i) {

        if (i != 999) {
            if (!invincible) {
                gp.playSE(6);
                life -= 1;
                invincible = true;
            }

        }

    }

    public void damageMonster(int i) {

        if (i != 999) {
            if (!gp.monster[i].invincible) {
                gp.playSE(5);
                gp.monster[i].life -= 1;
                gp.monster[i].invincible = true;

                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                }
            }
        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // THIS IS WHERE WE DRAW IF THE PLAYER IS ATTACKING OR IN NORMAL STATE
        switch (direction) {
            case "up" -> {

                if (!attacking) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }

                if (attacking) {
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }

                    // TO CHECK THE ATTACK AREA
                    if (debugger.playerAttackDebugSwitch) {
                        g2.setColor(Color.red);
                        g2.drawRect(tempScreenX + solidArea.x, tempScreenY + solidArea.y , attackArea.width, attackArea.height);
                    }
                }

            }
            case "down" -> {

                if (!attacking) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }

                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }

                    // TO CHECK THE ATTACK AREA
                    if (debugger.playerAttackDebugSwitch) {
                        g2.setColor(Color.red);
                        g2.drawRect(tempScreenX + solidArea.x, tempScreenY + gp.tileSize, attackArea.width, attackArea.height);
                    }
                }

            }
            case "left" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }

                if (attacking) {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }

                    // TO CHECK THE ATTACK AREA
                    if (debugger.playerAttackDebugSwitch) {
                        g2.setColor(Color.red);
                        g2.drawRect(tempScreenX + solidArea.x , tempScreenY + solidArea.y, attackArea.width, attackArea.height);
                    }
                }
            }
            case "right" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }

                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    }
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }

                    // TO CHECK THE ATTACK AREA
                    if (debugger.playerAttackDebugSwitch) {
                        g2.setColor(Color.red);
                        g2.drawRect(tempScreenX + gp.tileSize, tempScreenY + solidArea.y, attackArea.width, attackArea.height);
                    }
                }
            }
        }

        // This will make the player transparent when they are invincible (in the sense they just got hit)
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Resetting the transparency back to normal
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

        // TO CHECK THE SOLID AREA OF THE PLAYER
        if (debugger.solidAreaOfPlayerDebugSwitch) {
            g2.setColor(Color.red);
            g2.drawRect(screenX + solidArea.y, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

        // TO CHECK THE INVINCIBILITY OF THE PLAYER
        if (debugger.playerInvincibilityChecker) {
            g2.setFont(new Font("Arial", Font.PLAIN, 26));
            g2.setColor(Color.white);
            g2.drawString("Invincible : " + invincibleCounter, 10, 400);
        }

    }
}
