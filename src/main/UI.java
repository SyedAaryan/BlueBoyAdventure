package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UI {

    private static final Logger logger = Logger.getLogger(UI.class.getName());

    GamePanel gp;
    Graphics2D g2;
    Font purisaB, maruMonica;

    BufferedImage heart_full, heart_half, heart_blank;

    public boolean messageOn = false;

    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;

    public String currentDialogue = "";

    public int commandNum = 0;

    // For inventory
    public int slotCol = 0;
    public int slotRow = 0;


    public UI(GamePanel gp) {

        this.gp = gp;

        try {

            InputStream iS = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            assert iS != null;
            purisaB = Font.createFont(Font.TRUETYPE_FONT, iS);

            iS = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            assert iS != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, iS);

        } catch (FontFormatException e) {
            logger.log(Level.SEVERE, "Font " + maruMonica + "format not found");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "The given font is not found");
        }

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void addMessage(String text) {

        message.add(text);
        messageCounter.add(0);

    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {

            drawPlayerLife();
            drawMessage();

        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }

        // CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }

    }

    public void drawPlayerLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // RESET
        x = gp.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }


    }

    public void drawMessage() {

        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {

            // To avoid null pointer exception
            if (message.get(i) != null) {

                // For shadow effect
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);

                // For Actual text
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);


                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                // 180 i,e i80 frames, meaning 3 seconds
                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }

            }

        }

    }

    public void drawTitleScreen() {

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Blue Boy Adventure";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;

        // SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // CHARACTER IMAGE
        x = gp.screenWidth / 2 - (gp.tileSize * 2 / 2);
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += (int) (gp.tileSize * 3.5);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }

    }

    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "Paused";

        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);

    }

    public void drawDialogueScreen() {

        // WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {

            g2.drawString(line, x, y);
            y += 40;

        }


    }

    public void drawCharacterScreen() {

        //CREATE A FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;

        // lineHeight is to keep the text away from each other horizontally
        final int lineHeight = 35;

        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;

        g2.drawString("Life", textX, textY);
        textY += lineHeight;

        g2.drawString("Strength", textX, textY);
        textY += lineHeight;

        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;

        g2.drawString("Attack", textX, textY);
        textY += lineHeight;

        g2.drawString("Defense", textX, textY);
        textY += lineHeight;

        g2.drawString("Exp", textX, textY);
        textY += lineHeight;

        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;

        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 20;

        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;

        g2.drawString("Shield", textX, textY);

        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize; // Resetting textY;

        characterScreenValues(String.valueOf(gp.player.level), tailX, textY);
        textY += lineHeight;

        characterScreenValues(gp.player.life + "/" + gp.player.maxLife, tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.strength), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.dexterity), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.attack), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.defense), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.exp), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.nextLevelExp), tailX, textY);
        textY += lineHeight;

        characterScreenValues(String.valueOf(gp.player.coin), tailX, textY);
        textY += lineHeight;

        // TO DISPLAY THE IMAGES OF WEAPON AND SHIELD
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 14, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 14, null);

        /*So basically what happens is first the names get rendered, i,e the level, life etc., once that's done
         * we reset the textY value and move the alignment of the text towards the right and render tha value of
         * level, life etc.*/

    }

    // Method for inventory
    public void drawInventory() {

        //FRAME
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;

        // Calling the subWindow frame
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // DRAW PLAYER ITEMS
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            // 4, 9, 14 because when index it that, it should go to the next row
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += slotSize;
            }

        }

        //CURSOR (by cursor, it is to select the things in the inventory)
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR (by cursor, it is to select the things in the inventory)
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

    }

    // Used to draw a subWindow, parameters are passed as X,Y,WIDTH,HEIGHT
    public void drawSubWindow(int x, int y, int width, int height) {

        // For the window
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        // For the border of the window
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

    // A method which takes the input as a text and returns the "X" value for it to be displayed at the center of the screen
    public int getXForCenteredText(String text) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return gp.screenWidth / 2 - length / 2;

    }

    // A method which returns the right alignment for the character stats screen
    public int getXForAlignToRight(String text, int tailX) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;

    }

    // This will display the values in the character screen
    public void characterScreenValues(String value, int tailX, int textY) {

        int textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);

    }

}
