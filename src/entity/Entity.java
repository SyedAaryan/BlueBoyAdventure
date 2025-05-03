package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Entity {

    private static final Logger logger = Logger.getLogger(Entity.class.getName());

    GamePanel gp;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage image, image2, image3;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;

    String[] dialogues = new String[20];

    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public int dialogueIndex = 0;
    public boolean invincible = false;
    public boolean collisionOn = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;

    // COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    int knockBackCounter = 0;

    // CHARACTER ATTRIBUTES
    public String name;
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
    public Entity currentLight;

    // ITEM ATTRIBUTES
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost; //For stuff like fireball, etc
    public int price;
    public int knockBackPower = 0;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    // Inventory
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    //TYPES
    public int type; // 0 is player, 1 is npc, 2 is monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;

    public Entity(GamePanel gp) {

        this.gp = gp;

    }

    public int getLeftX() {
        return worldX + solidArea.x;
    }

    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }

    public int getTopY() {
        return worldY + solidArea.y;
    }

    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }

    public int getCol() {
        return (worldX + solidArea.x) / gp.tileSize;
    }

    public int getRow() {
        return (worldY + solidArea.y) / gp.tileSize;
    }

    public void setAction() {
    }

    public void damageReaction() {

    }

    public void speak() {

        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";

        }

    }

    public void interact() {
    }

    public boolean use(Entity entity) {
        return false;
    }

    public void checkDrop() {
    }

    public void dropItem(Entity droppedItem) {

        for (int i = 0; i < gp.obj[1].length; i++) {
            //null cause the monster is dead, and it is removed from the array
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
                // The break is important, if we don't add it the above lines will be executed in all empty slots
            }
        }

    }

    public Color getParticleColor() {
        return null;
    }

    public int getParticleSize() {
        return 0;
    }

    public int getParticleSpeed() {
        return 0;
    }

    public int getParticleMaxLife() {
        return 0;
    }

    public void generateParticle(Entity generator, Entity target) {

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        // Creating the particle
        // We are making 4 particles to give the "effect", in 4 different directions
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);//it goes top left
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);// it goes top right
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);// it goes down left
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);// it goes down right

        //Adding the particle in the particleList
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);

    }

    public void checkCollision() {

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        // when the monster contacts the player, it can damage if player is not invincible
        if (type == type_monster && contactPlayer) {
            damagePlayer(attack);
        }

    }

    public void update() {

        if (knockBack) {
            checkCollision();

            // If it hits a solid tile, knockBack is false, this is done to avoid the entity passing through solid tiles
            // during a knockBack
            if (collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else {
                switch (gp.player.direction) {
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

        } else {
            setAction();
            checkCollision();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
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
        }

        spriteCounter++;
        if (spriteCounter > 24) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

    }

    // To damage the player
    public void damagePlayer(int attack) {
        if (!gp.player.invincible) {
            // WE CAN GIVE DAMAGE
            gp.playSE(6);

            // It calculates the players defense and monster attack and gives a "damage" value to the player
            int damage = attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }

            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (
                worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY
        ) {

            switch (direction) {
                case "up" -> {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                case "down" -> {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                case "left" -> {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                case "right" -> {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
            }

            //MONSTER HP BAR
            if (type == 2 && hpBarOn) {

                /*This divides the monsters health bar with its max life, i,e one tile
                 * here is 48 pixels, if the monster HP is 4, then 1 scale will be 12 as
                 * 12 * 4 = 48*/
                double oneScale = (double) gp.tileSize / maxLife;

                // With the above value, we can find out the health bar value
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }

            }


            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4F);
            }

            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            changeAlpha(g2, 1F);
        }

    }

    // The method that is called when a monster is killed
    private void dyingAnimation(Graphics2D g2) {

        dyingCounter++;

        int i = 5;

        // So many if else's, this is basically to "Animate" the dying by reducing the opacity upto a certain frames
        if (dyingCounter <= i) {
            changeAlpha(g2, 0F);
        }
        if (dyingCounter > i && dyingCounter <= i * 2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 8) {
            alive = false;
        }

    }

    // Alpha is the transparency of the object
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading image: " + imagePath, e);
        }

        return image;
    }

    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        // It means it has found the path
        if (gp.pFinder.search()) {

            //NEXT worldX AND worldY
            int nextX = gp.pFinder.pathList.getFirst().col * gp.tileSize;
            int nextY = gp.pFinder.pathList.getFirst().row * gp.tileSize;

            //Entities solid area
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            // The below code may be confusing, check RyiSnow pathfinding vid 27:30

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                //Left or right
                if (enLeftX > nextX) {
                    direction = "left";
                }
                if (enLeftX < nextX) {
                    direction = "right";
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                // up or left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                // up or right
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                // down or left
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                // down or right
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            //If the goal is reached, stop the search
//            int nextCol = gp.pFinder.pathList.getFirst().col;
//            int nextRow = gp.pFinder.pathList.getFirst().row;
//            if (nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
//            }

            //The above code is commented when the  NPC wants to follow the player, if it's not commented,
            //then the NPC won't follow after it reaches the player for the 1st time
        }
    }

    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = 999;

        // Check the surrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getRightX();

        // Watch ryiSnow game dev no 42 if you didnt understand this, timestamp "10:10"
        switch (user.direction) {
            case "up":
                nextWorldY = user.getTopY() - user.speed;
                break;

            case "down":
                nextWorldY = user.getBottomY() + user.speed;
                break;

            case "left":
                nextWorldX = user.getLeftX() - user.speed;
                break;

            case "right":
                nextWorldX = user.getRightX() + user.speed;
                break;
        }
        int col = nextWorldX / gp.tileSize;
        int row = nextWorldY / gp.tileSize;

        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {
                if (target[gp.currentMap][i].getCol() == col && target[gp.currentMap][i].getRow() == row &&
                        target[gp.currentMap][i].name.equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

}
