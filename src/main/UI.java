package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

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

    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;

    public boolean messageOn = false;

    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;

    public String currentDialogue = "";

    public int commandNum = 0;

    // For inventory
    public int slotCol = 0;
    public int slotRow = 0;

    int subState = 0;


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
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;

    }

    // This will add the message in the arrayList
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

        // OPTION STATE
        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }

    }

    // Draws the player life as well as mana
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

        // DRAW MAX MANA
        x = (gp.tileSize / 2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // DRAW CURRENT MANA
        x = (gp.tileSize / 2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }

    }

    // This method will the message that was added by the addMessage() method
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
        final int frameX = gp.tileSize * 2;
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

        g2.drawString("Mana", textX, textY);
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
        textY += lineHeight + 10;

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

        characterScreenValues(gp.player.mana + "/" + gp.player.maxMana, tailX, textY);
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
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);

        /*So basically what happens is first the names get rendered, i,e the level, life etc., once that's done
         * we reset the textY value and move the alignment of the text towards the right and render tha value of
         * level, life etc.*/

    }

    // Method for inventory
    public void drawInventory() {

        //FRAME
        int frameX = gp.tileSize * 12;
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

            // EQUIP CURSOR
            if (gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

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

        // DESCRIPTION WINDOW
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight + gp.tileSize;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;

        // DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {

            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

            // For loop to basically split the line ifi "\n" comes in the sentence
            for (String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }

        }

    }

    // Method for drawing the option screen
    public void drawOptionsScreen() {

        // Setting the color and font
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int width = gp.tileSize * 8;
        int height = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, width, height);

        switch (subState) {
            case 0:
                options_top(frameX, frameY);
                break;
            case 1:
                optionsFullScreenNotification(frameX, frameY);
                break;
            case 2:
                break;
        }

        // This is imp as without this, enter key pressed will be pressed
        gp.keyH.enterPressed = false;

    }

    public void options_top(int frameX, int frameY) {

        int textX;
        int textY;

        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // FULL SCREEN ON OFF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            // Clicking on the full screen checkbox
            if (gp.keyH.enterPressed) {
                if (!gp.fullScreenOn) {
                    gp.fullScreenOn = true;
                } else {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        //MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
        }

        //CONTROL
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
        }

        //END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
        }

        //BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
        }

        //FULL SCREEN CHECK BOX
        textX = frameX + (int) (gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn) {
            g2.fillRect(textX, textY, 24, 24);
        }

        // MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);

        // SE VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);

    }

    // when the subState is 1, this function will be called, which will notify the user the respective message
    public void optionsFullScreenNotification(int frameX, int frameY) {

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "The change will take \neffect after restarting \nthe game.";

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // BACK
        // This is for back option, when enter is pressed, it'll go back to the menu
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
            }
        }

    }

    /*This basically returns the index of an element in a slot, it is done by calculating its slotCol
     * and slotRow, lets say an element is at (2,4)[assume inventory is a 4x5 matrix], them, the slot will
     * be at column 4 and row 2, thus 4 + (2 * 5 ) = 14 is the index*/
    public int getItemIndexOnSlot() {
        return slotCol + (slotRow * 5);
        //For clearer explanation for this if needed watch ryi snow vid #27 24:30
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
