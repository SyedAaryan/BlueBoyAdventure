package entity;

import debugger.Debugger;
import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;

    // It means not attacking
    public boolean attackCancelled = false;

    public boolean lightUpdated = false;

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
    }

    //Default values of the player
    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;

//        worldX = gp.tileSize * 12;
//        worldY = gp.tileSize * 12;
        gp.currentMap = 0;

        defaultSpeed = 4;
        speed = defaultSpeed;
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
        coin = 500;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = null;
        projectile = new OBJ_Fireball(gp);
        attack = getAttack(); // Total attack value is decided by strength and weapon
        defense = getDefense(); // Total defense value is decided by dexterity and shield

        getImage();
        getAttackImage();
        getGuardImage();

        // Inventory items
        setItems();

    }

    // When the player dies
    public void setDefaultPosition() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";

    }

    public void restoreStatus() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
    }

    // To set the inventory items
    public void setItems() {

        // Inventory.clear will clear all items, and then the default shield and sword will only be there
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Axe(gp));
    }

    public int getAttack() {
        // With this the players attack area will be updated depending on the weapon
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getImage() {

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
    public void getAttackImage() {

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

    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }

    public void getGuardImage() {
        guardUp = setup("/player/boy_guard_up", gp.tileSize, gp.tileSize);
        guardDown = setup("/player/boy_guard_down", gp.tileSize, gp.tileSize);
        guardLeft = setup("/player/boy_guard_left", gp.tileSize, gp.tileSize);
        guardRight = setup("/player/boy_guard_right", gp.tileSize, gp.tileSize);
    }

    public void update() {

        if (knockBack) {

            // CHECK TILE COLLISION
            collisionOn = false;
            if (!debugger.playerGodMode) {
                gp.cChecker.checkTile(this);
            }

            // CHECK OBJECT COLLISION
            gp.cChecker.checkObject(this, true);

            //CHECK NPC COLLISION
            gp.cChecker.checkEntity(this, gp.npc);

            // CHECK MONSTER COLLISION
            gp.cChecker.checkEntity(this, gp.monster);

            //CHECK INTERACTIVE TILES COLLISION
            gp.cChecker.checkEntity(this, gp.iTile);

            // If it hits a solid tile, knockBack is false, this is done to avoid the entity passing through solid tiles
            // during a knockBack
            if (collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else {
                switch (knockBackDirection) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            knockBackCounter++;
            // The more we increase the number, the more the distance increases
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        } else if (attacking) {
            attacking();
        } else if (keyH.spacePressed) {
            guarding = true;
            guardCounter++;
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
            if (!debugger.playerGodMode) {
                gp.cChecker.checkTile(this);
            }

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
            guarding = false;
            guardCounter = 0;

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
            guarding = false;
            guardCounter = 0;
        }

        // "projectile.alive" is to ensure that the player cant shoot another projectile when the 1st one is still alive
        if (gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.hasResource(this)) {

            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT MANA
            projectile.subtractResource(this);

            //CHECK VACANCY
            // We check which slot is empty, and put the projectile on that slot
            for (int i = 0; i < gp.projectile[1].length; i++) {
                if (gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;

            gp.playSE(10);
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                transparent = false;
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
        if (life <= 0) {
            if(debugger.immortal){
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum = -1;
                gp.stopMusic();
                gp.playSE(12);
            }

        }

    }

    public void pickObject(int i) {

        if (i != 999) {

            //PICKUP ONLY ITEMS
            if (gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }

            //OBSTACLE
            else if (gp.obj[gp.currentMap][i].type == type_obstacle) {
                if (keyH.enterPressed) {
                    attackCancelled = true;
                    gp.obj[gp.currentMap][i].interact();
                }
            }

            // INVENTORY ITEMS
            else {
                String text;

                // Checking whether the inventory is full
                if (canObtainItem(gp.obj[gp.currentMap][i])) {
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";

                } else {
                    text = "Cant carry more items";
                }

                gp.ui.addMessage(text);

                // This is imp as without this, the obj won't disappear
                gp.obj[gp.currentMap][i] = null;

            }

        }
    }

    public void interactNPC(int i) {

        if (keyH.enterPressed) {
            if (i != 999) {
                attackCancelled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            }
        }

    }

    public void contactMonster(int i) {

        if (i != 999) {
            //The player will take damage when he is not invincible and the monster is not dying
            if (!invincible && !gp.monster[gp.currentMap][i].dying) {
                gp.playSE(6);

                // It calculates the players defense and monster attack and gives a "damage" value to the player
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if (damage < 1) {
                    damage = 1;
                }

                life -= damage;
                invincible = true;
                transparent = true;
            }

        }

    }

    // This method will damage monster
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {

        if (i != 999) {
            if (!gp.monster[gp.currentMap][i].invincible) {
                gp.playSE(5);

                if (knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }

                if (gp.monster[gp.currentMap][i].offBalance) {
                    attack *= 5;
                }

                // It calculates the monster defense and player attack and gives a "damage" value
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;

                // shows how much damage the player has done
                gp.ui.addMessage(damage + " damage!");

                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;

                    // shows if the killed is killed
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name);

                    // Playing will get exp if he kills the monster
                    gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;

                    // To check if the player leveled up
                    checkLevelUp();

                }
            }
        }

    }

    public void damageInteractiveTile(int i) {

        if (i != 999 && gp.iTile[gp.currentMap][i].destructible && gp.iTile[gp.currentMap][i].isCorrectItem(this) && !gp.iTile[gp.currentMap][i].invincible) {
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            // Generate particle
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if (gp.iTile[gp.currentMap][i].life == 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }

        }

    }

    public void damageProjectile(int i) {
        // When the player hits the projectile, it dies
        if (i != 999) {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
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

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        // This is to check if we are selecting an item and not an empty slot
        if (itemIndex < inventory.size()) {

            // Selected item will be of type "Entity"
            Entity selectedItem = inventory.get(itemIndex);

            //Checking if the item is a sword or an axe and updating the defense
            if (selectedItem.type == type_axe || selectedItem.type == type_sword) {
                currentWeapon = selectedItem;
                attack = getAttack();

                // When the item is changed the below method is called to change the animation
                getAttackImage();
            }

            // Checking if the item is a shield and updating the defense
            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }

            if (selectedItem.type == type_light) {
                if (currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }

            // Checking if the item is a consumable
            if (selectedItem.type == type_consumable) {

                if (selectedItem.use(this)) {
                    if (selectedItem.amount > 1) {
                        selectedItem.amount--;
                    } else {
                        inventory.remove(itemIndex);
                    }
                }

            }

        }

    }

    //This method can be used for various stuff, to see if the player has the particular item, and returning hte index
    //has it
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;

        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    // A function to see if the player can pick the item and keep it in the inventory
    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;

        // CHECK IF STACKABLE
        if (item.stackable) {
            int index = searchItemInInventory(item.name);

            if (index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            } else { // New item, need to check vacancy
                if (inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        } else { // NOT A STACKABLE ITEM
            if (inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
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

                if (guarding) {
                    image = guardUp;
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

                if (guarding) {
                    image = guardDown;
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

                if (guarding) {
                    image = guardLeft;
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

                if (guarding) {
                    image = guardRight;
                }
            }
        }

        // This will make the player transparent when they are invincible (in the sense they just got hit)
        if (transparent) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4F));
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
