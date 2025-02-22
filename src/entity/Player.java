package entity;

import debugger.Debugger;
import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;

    // It means not attacking
    public boolean attackCancelled = false;

    // Inventory
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

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

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();

        // Inventory items
        setItems();
    }

    //Default values of the player
    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;

        speed = 4;
        direction = "down";

        //PLAYER STATUS
        level = 1;
        maxLife = 6;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        life = maxLife;
        strength = 1; // The more strength he has, the damage he gives
        dexterity = 1; // The more dexterity he has, the less damage he takes
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Axe(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack = getAttack(); // Total attack value is decided by strength and weapon
        defense = getDefense(); // Total defense value is decided by dexterity and shield

    }

    // When the player dies
    public void setDefaultPosition(){

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";

    }

    public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    // To set the inventory items
    public void setItems() {

        // Inventory.clear will clear all items, and then the default shield and sword will only be there
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack() {
        // With this the players attack area will be updated depending on the weapon
        attackArea = currentWeapon.attackArea;

        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
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

    // This method is used to set the attacking sprite of the player by checking the current weapon
    public void getPlayerAttackImage() {

        // If the current weapon is a sword
        if (currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }

        // If the current weapon is an axe
        if (currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }

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

            //CHECK INTERACTIVE TILES COLLISION
            gp.cChecker.checkEntity(this, gp.iTile);

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

            if (keyH.enterPressed && !attackCancelled) {

                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;

            }

            attackCancelled = false;

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

        // "projectile.alive" is to ensure that the player cant shoot another projectile when the 1st one is still alive
        if (gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.hasResource(this)) {

            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT MANA
            projectile.subtractResource(this);

            // ADD IT TO THE LIST
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;

            gp.playSE(10);
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if (life > maxLife) {
            life = maxLife;
        }

        if (mana > maxMana) {
            mana = maxMana;
        }

        // Checking if the play has no life, if he doesn't, game ends
        if (life <= 0){
            gp.gameState = gp.gameOverState;
            gp.playSE(12);
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
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

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

            //PICKUP ONLY ITEMS
            if (gp.obj[i].type == type_pickupOnly) {
                gp.obj[i].use(this);
                gp.obj[i] = null;
            }

            // INVENTORY ITEMS
            else {
                String text;

                // Checking whether the inventory is full
                if (inventory.size() != maxInventorySize) {

                    inventory.add(gp.obj[i]);
                    gp.playSE(1);
                    text = "Got a " + gp.obj[i].name + "!";

                } else {
                    text = "Cant carry more items";
                }

                gp.ui.addMessage(text);

                // This is imp as without this, the obj won't disappear
                gp.obj[i] = null;

            }

        }
    }

    public void interactNPC(int i) {

        if (keyH.enterPressed) {
            if (i != 999) {
                attackCancelled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }

    }

    public void contactMonster(int i) {

        if (i != 999) {
            //The player will take damage when he is not invincible and the monster is not dying
            if (!invincible && !gp.monster[i].dying) {
                gp.playSE(6);

                // It calculates the players defense and monster attack and gives a "damage" value to the player
                int damage = gp.monster[i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }

                life -= damage;
                invincible = true;
            }

        }

    }

    // This method will damage monster
    public void damageMonster(int i, int attack) {

        if (i != 999) {
            if (!gp.monster[i].invincible) {
                gp.playSE(5);

                // It calculates the monster defense and player attack and gives a "damage" value
                int damage = attack - gp.monster[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;

                // shows how much damage the player has done
                gp.ui.addMessage(damage + " damage!");

                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;

                    // shows if the killed is killed
                    gp.ui.addMessage("Killed the " + gp.monster[i].name);

                    // Playing will get exp if he kills the monster
                    gp.ui.addMessage("Exp + " + gp.monster[i].exp);
                    exp += gp.monster[i].exp;

                    // To check if the player leveled up
                    checkLevelUp();

                }
            }
        }

    }

    public void damageInteractiveTile(int i) {

        if (i != 999 && gp.iTile[i].destructible && gp.iTile[i].isCorrectItem(this) && !gp.iTile[i].invincible) {
            gp.iTile[i].playSE();
            gp.iTile[i].life--;
            gp.iTile[i].invincible = true;

            // Generate particle
            generateParticle(gp.iTile[i], gp.iTile[i]);

            if (gp.iTile[i].life == 0) {
                gp.iTile[i] = gp.iTile[i].getDestroyedForm();
            }

        }

    }

    public void checkLevelUp() {

        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are Level " + level;
        }

    }

    // This method will check for the selected item from the inventory
    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot();

        // This is to check if we are selecting an item and not an empty slot
        if (itemIndex < inventory.size()) {

            // Selected item will be of type "Entity"
            Entity selectedItem = inventory.get(itemIndex);

            //Checking if the item is a sword or an axe and updating the defense
            if (selectedItem.type == type_axe || selectedItem.type == type_sword) {
                currentWeapon = selectedItem;
                attack = getAttack();

                // When the item is changed the below method is called to change the animation
                getPlayerAttackImage();
            }

            // Checking if the item is a shield and updating the defense
            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }

            // Checking if the item is a consumable
            if (selectedItem.type == type_consumable) {

                selectedItem.use(this);
                inventory.remove(itemIndex);

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
                        g2.drawRect(tempScreenX + solidArea.x, tempScreenY + solidArea.y, attackArea.width, attackArea.height);
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
                        g2.drawRect(tempScreenX + solidArea.x, tempScreenY + solidArea.y, attackArea.width, attackArea.height);
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
