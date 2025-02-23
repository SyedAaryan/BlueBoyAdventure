package main;

import debugger.Debugger;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16 x 16 Tile Size
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10; // Max number of maps the game can have, we can any number, but for this game only 10
    public int currentMap = 1;

    //FOR FULL SCREEN
    public boolean fullScreenOn = false;
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // FPS
    int FPS = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound(); // For BG Music
    public Sound se = new Sound(); // For Sound Effects
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    Thread gameThread;

    //DEBUGGING
    Debugger debugger = new Debugger();

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[][] obj = new Entity[maxMap][20];
    public Entity[][] npc = new Entity[maxMap][10];
    public Entity[][] monster = new Entity[maxMap][20];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void setUpGame() {

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();

        gameState = titleState;

        // If u want to know what this is, look at the end of this file
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn) {
            setFullScreen();
        }

    }

    public void retry() {

        player.setDefaultValues();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();

    }

    public void restart() {

        player.setDefaultValues();
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        player.setItems();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();

    }

    public void setFullScreen() {

        // GET LOCAL DEVICE'S LOCAL SCREEN INFORMATION
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {

                update();
                drawToTempScreen(); // draw everything to the buffered image
                drawToScreen(); // draw the buffered image to the screen
                delta--;
                drawCount++;

            }

        }

    }

    // Updates the game every frame
    public void update() {

        if (gameState == playState) {

            // PLAYER
            player.update();

            //NPC
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            // MONSTER
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if (!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILES
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            //PARTICLES
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if (!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

            // INTERACTIVE TILES
            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }

        }
        if (gameState == pauseState) {
            //NOTHING FOR NOW
        }

    }

    public void drawToTempScreen() {

        //DEBUG
        long drawStart = 0;
        if (debugger.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        //TITLE STATE
        if (gameState == titleState) {
            ui.draw(g2);
        }

        //OTHER STATES
        else {

            //THE TILES
            tileM.draw(g2);

            //INTERACTIVE TILES
            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
                }
            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADD ENTITIES TO THE LIST
            entityList.add(player);

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING NPC's
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING OBJECTS
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING MONSTERS
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            // ADDING PROJECTILES
            for (Entity value : projectileList) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            //PARTICLES
            for (Entity value : particleList) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // SORTING
            entityList.sort(Comparator.comparingInt(e -> e.worldY));

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // DRAW ENTITIES
            for (Entity entity : entityList) {

                entity.draw(g2);

            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            //  EMPTY ENTITY LIST
            entityList.clear();


            //UI
            ui.draw(g2);

        }


        //DEBUG
        if (debugger.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX " + player.worldX, x, y);
            y += lineHeight;

            g2.drawString("WorldY " + player.worldY, x, y);
            y += lineHeight;

            g2.drawString("Col " + (player.worldX + player.solidArea.x) / tileSize, x, y);
            y += lineHeight;

            g2.drawString("Row " + (player.worldY + player.solidArea.y) / tileSize, x, y);
            y += lineHeight;

            g2.drawString("Draw Time " + passed, x, y);
        }

    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();

    }

    public void stopMusic() {

        music.stop();

    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    // SE = Sound Effect
    public void playSE(int i) {

        se.setFile(i);
        se.play();

    }

}

/*The reason for having all the entities in an array is that, once we have them in an array, we can
 * sort them with their worldY, and we can draw them in an order which look realistic
 * IF YOU ARE STILL CONFUSED, I KNOW YOU ARE, WATCH "How to Make a 2D Game in Java #21" FROM RuiSnow*/


/*Regarding the temp screen, when we want to make the game work in a full screen, earlier we would just draw it
 * now, we draw everything in a temp screen, and then resize it, this is done cause if we resize everything manually,
 * it'll be very bad, and this method is heavier in terms of performance than the previous one
 * IF YOU ARE STILL CONFUSED, I KNOW YOU ARE, WATCH "How to Make a 2D Game in Java #34" FROM RuiSnow*/