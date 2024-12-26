package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Logger;
import java.util.logging.Level;

public class UI {

    private static final Logger logger = Logger.getLogger(UI.class.getName());

    GamePanel gp;
    Graphics2D g2;
    Font purisaB, maruMonica;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    public boolean gameFinished = false;

    public String currentDialogue = "";

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


    }

    public void showMessage(String text) {

        message = text;
        messageOn = true;

    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // PLAY STATE
        if (gp.gameState == gp.playState) {

            // DRAW THE STUFF WHEN THE GAME IS BEING PLAYED

        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
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

}
