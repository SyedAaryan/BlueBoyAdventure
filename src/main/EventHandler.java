package main;

public class EventHandler {

    GamePanel gp;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {

        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

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
            if (hit(27, 16, "right")) {
                damagePit(27, 16, gp.dialogueState);
            }

            if (hit(23, 12, "up")) {
                healingPool(23, 12, gp.dialogueState);
            }
        }

    }

    public boolean hit(int col, int row, String reqDirection) {

        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;

            }

        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;

    }

    public void damagePit(int col, int row, int gameState) {

        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You fell into a pit!!";
        gp.player.life -= 1;
        canTouchEvent = false;

    }

    public void healingPool(int col, int row, int gameState) {

        if (gp.keyH.enterPressed) {

            gp.gameState = gameState;
            gp.player.attackCancelled = true;
            gp.playSE(2);
            gp.ui.currentDialogue = "You drank the water \n You life and mana are fully recovered";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;

            // So when we heal, the monster will reset the below method spawns them
            gp.aSetter.setMonster();

        }

    }

}
