package main;

import debugger.Debugger;
import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16 x 16 Tile Size
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

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
    Thread gameThread;

    //DEBUGGING
    Debugger debugger = new Debugger();

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;

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

        gameState = titleState;

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
                repaint();
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
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }

            // MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (!monster[i].alive) {
                        monster[i] = null;
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

        }
        if (gameState == pauseState) {
            //NOTHING FOR NOW
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

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

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADD ENTITIES TO THE LIST
            entityList.add(player);

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING NPC's
            for (Entity entity : npc) {

                if (entity != null) {
                    entityList.add(entity);
                }

            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING OBJECTS
            for (Entity entity : obj) {

                if (entity != null) {
                    entityList.add(entity);
                }

            }

            // IF YOU'RE CONFUSED ABOUT WHY THIS IS BEING DONE, GO TO THE END OF THIS FILE
            // ADDING MONSTERS
            for (Entity entity : monster) {

                if (entity != null) {
                    entityList.add(entity);
                }

            }

            // ADDING PROJECTILES
            for (Entity value : projectileList) {
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

        g2.dispose();

    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();

    }

    public void stopMusic() {

        music.stop();

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