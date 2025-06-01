package main;

import entity.Entity;

public class EventHandler {

    GamePanel gp;
    EventRect[][][] eventRect;
    Entity eventMaster;
    int tempMap, tempCol, tempRow;

    int previousEventX, previousEventY;// The reason for this is explained in checkEvent Class
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {

        this.gp = gp;
        eventMaster = new Entity(gp);

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;

                if (row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

        setDialogue();

    }

    public void setDialogue() {
        eventMaster.dialogues[0][0] = "You fell into a pit!!";

        eventMaster.dialogues[1][0] = "You drank the water \n You life and mana are fully recovered\n(Progress Has been Saved)";
        eventMaster.dialogues[1][1] = "Dam this is good water";
    }

    public void checkEvent() {

        //CHECK IF THE PLAYER IS MORE THAN 1 TILE AWAY
        // This is done so that a same event cant be repeated if the player doesn't move from the tile
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
            if (hit(0, 27, 16, "right")) {
                damagePit(gp.dialogueState);
            } else if (hit(0, 23, 12, "up")) {
                healingPool(gp.dialogueState);
            } else if (hit(0, 10, 39, "any")) {
                teleport(1, 12, 13);
            } else if (hit(1, 12, 13, "any")) {
                teleport(0, 10, 39);
            } else if (hit(1, 12, 9, "up")) { // For talking with the merchant through the table
                speak(gp.npc[1][0]);
            }
        }

    }

    public boolean hit(int map, int col, int row, String reqDirection) {

        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;

                }

            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;

    }

    public void damagePit(int gameState) {

        gp.gameState = gameState;
        gp.playSE(6);
        eventMaster.startDialogue(eventMaster, 0);
        gp.player.life -= 1;
        canTouchEvent = false;

    }

    public void healingPool(int gameState) {

        if (gp.keyH.enterPressed) {

            gp.gameState = gameState;
            gp.player.attackCancelled = true;
            gp.playSE(2);
            eventMaster.startDialogue(eventMaster, 1);
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;

            // So when we heal, the monster will reset the below method spawns them
            gp.aSetter.setMonster();
            gp.saveLoad.save();

        }

    }

    public void teleport(int map, int col, int row) {

        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;

        canTouchEvent = false;
        gp.playSE(13);

    }

    public void speak(Entity entity) {

        if (gp.keyH.enterPressed) {
            gp.gameState = gp.dialogueState;
            gp.player.attackCancelled = true;
            entity.speak();
        }

    }

}
