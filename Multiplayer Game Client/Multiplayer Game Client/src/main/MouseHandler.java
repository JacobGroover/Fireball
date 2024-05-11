package main;

import entities.Player;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener
{

    GamePanel gp;
    public int mouseX;
    public int mouseY;
    public boolean mainMenuHover1, mainMenuHover2, mainMenuHover3;
    public boolean characterMenuHover1;
    public boolean gameMenuHover1;
    public boolean gameOverHover1;
    public boolean playPressed1;
    public boolean playPressed1Cooldown;

    public MouseHandler(GamePanel gp)     // GAME STATE
    {
        this.gp = gp;
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (gp.gameState == gp.PLAY_STATE)
        {

        }
        else if (gp.gameState == gp.MAIN_MENU_STATE)
        {
            if (mainMenuHover1)
            {
                gp.joinedGame = true;
                gp.gameState = gp.PLAY_STATE;
            }
            else if (mainMenuHover2)
            {
                gp.gameState = gp.SELECT_CHARACTER_STATE;
            }
            else if (mainMenuHover3)
            {
                System.exit(0);
            }
        }

        else if (gp.gameState == gp.SELECT_CHARACTER_STATE)
        {
            if (characterMenuHover1)
            {
                gp.gameState = gp.MAIN_MENU_STATE;
            }
        }

        else if (gp.gameState == gp.GAME_MENU_STATE)
        {
            if (gameMenuHover1)
            {
                gp.joinedGame = false;
                gp.player.setDefaultValues();
                gp.gameState = gp.MAIN_MENU_STATE;
            }
        } else if (gp.gameState == gp.GAME_OVER_STATE)
        {
            if (gameOverHover1)
            {
//                gameOverHover1 = false;
                gp.joinedGame = false;
                gp.player.setDefaultValues();
//                gp.gameState = gp.MAIN_MENU_STATE;
                Player.respawned = true;
                gp.gameState = gp.PLAY_STATE;
//                gp.joinedGame = true;
//                gp.gameState = gp.PLAY_STATE;

//                gp.joinedGame = true;
//                gp.gameState = gp.PLAY_STATE;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (gp.gameState == gp.PLAY_STATE)
        {
            if (gp.player.alive)
            {
                if (e.getButton() == MouseEvent.BUTTON1 && !playPressed1Cooldown)
                {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    playPressed1 = true;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (gp.gameState == gp.PLAY_STATE)
        {

        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (gp.gameState == gp.MAIN_MENU_STATE)
        {
            if (e.getX() > gp.ui.getCenteredTextX("PLAY GAME") &&
                    e.getX() < gp.ui.getCenteredTextX("PLAY GAME") + (int)gp.ui.g2.getFontMetrics().getStringBounds("PLAY GAME", gp.ui.g2).getWidth() &&
                    e.getY() > gp.screenHeight/4 - (int)gp.ui.g2.getFontMetrics().getStringBounds("PLAY GAME", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < gp.screenHeight/4 + 15)
            {
                mainMenuHover1 = true;
            }
            else
            {
                mainMenuHover1 = false;
            }
            if (e.getX() > gp.ui.getCenteredTextX("SELECT CHARACTER") &&
                    e.getX() < gp.ui.getCenteredTextX("SELECT CHARACTER") + (int)gp.ui.g2.getFontMetrics().getStringBounds("SELECT CHARACTER", gp.ui.g2).getWidth() &&
                    e.getY() > gp.screenHeight/2 - gp.screenHeight/8 - (int)gp.ui.g2.getFontMetrics().getStringBounds("SELECT CHARACTER", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < gp.screenHeight/2 - gp.screenHeight/8 + 15)
            {
                mainMenuHover2 = true;
            }
            else
            {
                mainMenuHover2 = false;
            }
            if (e.getX() > gp.ui.getCenteredTextX("QUIT") &&
                    e.getX() < gp.ui.getCenteredTextX("QUIT") + (int)gp.ui.g2.getFontMetrics().getStringBounds("QUIT", gp.ui.g2).getWidth() &&
                    e.getY() > gp.screenHeight/2 - (int)gp.ui.g2.getFontMetrics().getStringBounds("QUIT", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < gp.screenHeight/2 + 15)
            {
                mainMenuHover3 = true;
            }
            else
            {
                mainMenuHover3 = false;
            }
        }
        else if (gp.gameState == gp.SELECT_CHARACTER_STATE)
        {
            if (e.getX() > gp.ui.getCenteredTextX("CREATE CHARACTER") &&
                    e.getX() < gp.ui.getCenteredTextX("CREATE CHARACTER") + (int)gp.ui.g2.getFontMetrics().getStringBounds("CREATE CHARACTER", gp.ui.g2).getWidth() &&
                    e.getY() > (int)(gp.screenHeight * 0.9) - (int)gp.ui.g2.getFontMetrics().getStringBounds("CREATE CHARACTER", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < (int)(gp.screenHeight * 0.9) + 15)
            {
                characterMenuHover1 = true;
            }
            else
            {
                characterMenuHover1 = false;
            }
        }
        else if (gp.gameState == gp.PLAY_STATE)
        {
//            if (!gp.player.alive)
//            {
//                if (e.getX() > gp.ui.getCenteredTextX("RESPAWN") &&
//                        e.getX() < gp.ui.getCenteredTextX("RESPAWN") + (int)gp.ui.g2.getFontMetrics().getStringBounds("RESPAWN", gp.ui.g2).getWidth() &&
//                        e.getY() > (int)(gp.screenHeight * 0.75) - (int)gp.ui.g2.getFontMetrics().getStringBounds("RESPAWN", gp.ui.g2).getHeight() + 20 &&
//                        e.getY() < (int)(gp.screenHeight * 0.75) + 15)
//                {
//                    gameOverHover1 = true;
//                }
//                else
//                {
//                    gameOverHover1 = false;
//                }
//            }
        }
        else if (gp.gameState == gp.GAME_MENU_STATE)
        {
            if (e.getX() > gp.ui.getCenteredTextX("BACK TO MAIN MENU") &&
                    e.getX() < gp.ui.getCenteredTextX("BACK TO MAIN MENU") + (int)gp.ui.g2.getFontMetrics().getStringBounds("BACK TO MAIN MENU", gp.ui.g2).getWidth() &&
                    e.getY() > gp.screenHeight/4 - (int)gp.ui.g2.getFontMetrics().getStringBounds("BACK TO MAIN MENU", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < gp.screenHeight/4 + 15)
            {
                gameMenuHover1 = true;
            }
            else
            {
                gameMenuHover1 = false;
            }

            // This line prevents a bug where hovering over RESPAWN text after dying was causing player to teleport back to default spawn if they instead chose to exit to main menu and then clicked play
//            gameOverHover1 = false;
        }
        else if (gp.gameState == gp.GAME_OVER_STATE)
        {
            if (e.getX() > gp.ui.getCenteredTextX("RESPAWN") &&
                    e.getX() < gp.ui.getCenteredTextX("RESPAWN") + (int)gp.ui.g2.getFontMetrics().getStringBounds("RESPAWN", gp.ui.g2).getWidth() &&
                    e.getY() > (int)(gp.screenHeight * 0.75) - (int)gp.ui.g2.getFontMetrics().getStringBounds("RESPAWN", gp.ui.g2).getHeight() + 20 &&
                    e.getY() < (int)(gp.screenHeight * 0.75) + 15)
            {
                gameOverHover1 = true;
            }
            else
            {
                gameOverHover1 = false;
            }
        }
    }

}
