package main;

import object.OBJ_Key;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI
{

    GamePanel gp;
    Graphics2D g2;  // GAME STATE
    Font arial_40, arial_80B;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message;
    int messageCounter = 0;
    public boolean gameFinished = false;
    public int commandNum = 0;

    double playTime;
    DecimalFormat df = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;
    }

    public void displayMessage(String text) {

        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;   // GAME STATE

        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);

        if (gp.gameState == gp.MAIN_MENU_STATE)
        {
            drawMainMenuScreen();
        }

        if (gp.gameState == gp.SELECT_CHARACTER_STATE)
        {
            drawSelectCharacterScreen();
        }

        if (gp.gameState == gp.PLAY_STATE || gp.gameState == gp.GAME_MENU_STATE)
        {
            playTime += (double)1/60;
            if (gp.gameState == gp.PLAY_STATE)
            {
                drawPlayScreen();
            }
            if (gp.gameState == gp.GAME_MENU_STATE)
            {
                drawGameMenuScreen();
            }
        }

    }

    public void drawMainMenuScreen()
    {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

        String play = "PLAY GAME";
        int x1 = getCenteredTextX(play);
        int y1 = gp.screenHeight/4;

        String selectCharacter = "SELECT CHARACTER";
        int x2 = getCenteredTextX(selectCharacter);
        int y2 = gp.screenHeight/2 - gp.screenHeight/8;

        String quit = "QUIT";
        int x3 = getCenteredTextX(quit);
        int y3 = gp.screenHeight/2;


        // draw text shadow
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(play, x1+3, y1+3);
        g2.drawString(selectCharacter, x2+3, y2+3);
        g2.drawString(quit, x3+3, y3+3);

        // draw text
        g2.setColor(Color.WHITE);

        if (gp.mouseHandler.mainMenuHover1)
        {
            g2.setColor(Color.GREEN);
        }
        g2.drawString(play, x1, y1);

        g2.setColor(Color.WHITE);
        if (gp.mouseHandler.mainMenuHover2)
        {
            g2.setColor(Color.GREEN);
        }
        g2.drawString(selectCharacter, x2, y2);

        g2.setColor(Color.WHITE);
        if (gp.mouseHandler.mainMenuHover3)
        {
            g2.setColor(Color.GREEN);
        }
        g2.drawString(quit, x3, y3);

    }

    public void drawSelectCharacterScreen()
    {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

        String create = "CREATE CHARACTER";
        int x1 = getCenteredTextX(create);
        int y1 = (int)(gp.screenHeight * 0.8);

        // draw text shadow
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(create, x1+3, y1+3);

        // draw text
        g2.setColor(Color.WHITE);

        if (gp.mouseHandler.characterMenuHover1)
        {
            g2.setColor(Color.GREEN);
        }
        g2.drawString(create, x1, y1);
    }

    public void drawPlayScreen()
    {
        if (gameFinished) {

            String text;
            int textLength;
            int x;
            int y;

            text = "You won the game!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);

            text = "Your time is: " + df.format(playTime) + "!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*4);
            g2.drawString(text, x, y);

            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "CONGRATULATIONS!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);

            gp.gameThread = null;

        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.lightGray);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            // GAME TIME
            g2.drawString("Time: " + df.format(playTime), gp.screenWidth - (gp.tileSize * 5), 65);

            // MESSAGE
            if (messageOn) {

                g2.setFont(g2.getFont().deriveFont(25F));
                g2.drawString(message, (gp.screenWidth/2) - (gp.tileSize * 2), (gp.screenHeight/2) - (gp.tileSize*2));

                messageCounter++;

                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }

    public void drawGameMenuScreen()
    {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.fillRect(gp.screenWidth/6, gp.screenHeight/16, 4 * (gp.screenWidth/6), 14 * (gp.screenHeight/16));

        g2.setColor(Color.GRAY);
        g2.fillRect(gp.screenWidth/6, gp.screenHeight/16, 4, 14 * (gp.screenHeight/16));
        g2.fillRect(5 * (gp.screenWidth/6) - 4, gp.screenHeight/16, 4, 14 * (gp.screenHeight/16));
        g2.fillRect(gp.screenWidth/6, gp.screenHeight/16, 4 * (gp.screenWidth/6), 4);
        g2.fillRect(gp.screenWidth/6, 15 * (gp.screenHeight/16) - 4, 4 * (gp.screenWidth/6), 4);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String backToMenu = "BACK TO MAIN MENU";
        int x1 = getCenteredTextX(backToMenu);
        int y1 = gp.screenHeight/4;

        // draw text shadow
        g2.setColor(Color.BLACK);
        g2.drawString(backToMenu, x1+3, y1+3);

        g2.setColor(Color.WHITE);

        if (gp.mouseHandler.gameMenuHover1)
        {
            g2.setColor(Color.GREEN);
        }
        g2.drawString(backToMenu, x1, y1);

        g2.setColor(Color.WHITE);
    }

    public int getCenteredTextX(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }


}
