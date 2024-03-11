package main;

import entities.OtherPlayer;
import entities.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;    // 16x16 tile for characters and NPCs
    final int scale = 3;    // scales the 16x16 characters to look like 48x48 pixel size, so they are easier to see

    public final int tileSize = originalTileSize * scale;  // sets in-game tiles to fit the size of each object, adjusted for our scale

    // Set game screen size to 4x3 (Length x Width) ratio
    public final int maxScreenCol = 32;    // 16
    public final int maxScreenRow = 24;    // 12
    public final int screenWidth = tileSize * maxScreenCol; // 48 * 16 = 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;   // 48 * 12 = 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // Set FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileManager = new TileManager(this);    // Instantiate TileManager object for game background tiles
    KeyHandler keyHandler = new KeyHandler();
    Sound music = new Sound();
    Sound sfx = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;  // needs 'implements Runnable' in class header


    // ENTITIES AND OBJECTS
    public Player player = new Player(this, keyHandler);   // Instantiate player class
    public SuperObject[] obj = new SuperObject[10];


    /**
     * No-argument constructor
     * Sets the size of the GamePanel extended JPanel class based on the screen width and height
     * specified in the constants above.
     * Sets background color for the game panel to the specified color.
     *
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);   // improves game's rendering performance by drawing rendered images off-screen
        this.addKeyListener(keyHandler);
        this.setFocusable(true);    // allows GamePanel to be focused to receive key input [over other applications?]
    }

    public void setupGame() {
        aSetter.setObject();

        // playMusic(0);
    }

    /** Method startGameThread
     * Instantiates gameThread as a new Thread and starts it, thereby calling the run method.
     *
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Alternative methodology for run
    /*public void run() {

        double drawInterval = (double)1000000000/FPS;   // Time for a single loop is approx. 0.0167 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {


            // Update character and object positions
            update();

            // Draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }*/

    /** Method: run
     * Necessary to run the gameThread. Starting the gameThread causes this object's run method to be called
     * in that gameThread instance.
     */
    @Override
    public void run() {

        double drawInterval = (double)1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        /*long timer = 0;
        int drawCount = 0;*/

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            //timer += currentTime - lastTime;

            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                //drawCount++;
            }
            /*if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }*/

        }

    }

    /** Method: update
     * Updates run method with changes
     *
     */
    public void update() {
        player.update();

        for (OtherPlayer otherPlayer : Client.otherPlayers)
        {
            otherPlayer.update();
        }
    }

    /** Method: paintComponent
     * Draws updates on JPanel
     *
     * @param graphics to be drawn on the JPanel
     */
    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics); // super class in this case is JPanel

        Graphics2D g2 = (Graphics2D)graphics;

        // DEBUG
        long drawStart = 0;
        if (keyHandler.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        tileManager.draw(g2);   // Draw background tiles before drawing players, so players will be on top of background

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // PLAYER
        player.draw(g2);
        for (OtherPlayer otherPlayer : Client.otherPlayers)
        {
            otherPlayer.draw(g2);
        }

        // UI
        ui.draw(g2);

        // DEBUG
        if (keyHandler.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
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

    public void playSFX(int i) {
        sfx.setFile(i);
        sfx.play();
    }

}
