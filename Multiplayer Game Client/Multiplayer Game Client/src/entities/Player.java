/** Class: Player
 *  @author Jacob Groover
 *
 *  This class – Player class; subclass of Entity
 */

package entities;

import main.Client;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public static double startX;
    public static double startY;
    public static double worldX;  // UDP
    public static double worldY;

    public int hasKey = 0;
    public static String sendVelocity = "00";
    public static double sendVelocityX = 0;
    public static double sendVelocityY = 0;

    public Player(GamePanel gp, KeyHandler keyH)
    {
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getImages("/player/FillerPlayer");
    }

    public void setDefaultValues() {
        startX = 23;
        startY = 21;
        worldX = gp.tileSize * startX;
        worldY = gp.tileSize * startY;
        speed = 4;
        direction = "down";
        velocityX = 0;
        velocityY = 0;
        maxLife = 100;
        life = maxLife;
    }

    public void update() {

        if (gp.gameState == gp.PLAY_STATE && (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)) {

            if (keyH.upPressed) {
                velocityY -= 1;
            }
            if (keyH.downPressed) {
                velocityY += 1;
            }
            if (keyH.leftPressed) {
                velocityX -= 1;
            }
            if (keyH.rightPressed) {
                velocityX += 1;
            }

            if (keyH.upPressed && velocityY != 0) {
                direction = "up";
            }
            if (keyH.downPressed && velocityY != 0) {
                direction = "down";
            }
            if (keyH.leftPressed && velocityX != 0) {
                direction = "left";
            }
            if (keyH.rightPressed && velocityX != 0) {
                direction = "right";
            }

            if (velocityX != 0 && velocityY != 0) {
                if (keyH.upPressed && keyH.leftPressed) {
                    direction = "upLeft";
                }
                if (keyH.upPressed && keyH.rightPressed) {
                    direction = "upRight";
                }
                if (keyH.downPressed && keyH.leftPressed) {
                    direction = "downLeft";
                }
                if (keyH.downPressed && keyH.rightPressed) {
                    direction = "downRight";
                }
            }

            // Normalize movement vector
            float length = (float) Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));

            if (velocityX != 0) {
                velocityX /= length;
            }
            if (velocityY != 0) {
                velocityY /= length;
            }

            velocityX *= speed;
            velocityY *= speed;

            // Check for tile collision
            xCollisionOn = false;
            yCollisionOn = false;
            gp.cChecker.checkTile(this);

            // Check for object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check for OtherPlayer collision
            int entityIndex = gp.cChecker.checkEntity(this, Client.otherPlayers);
//            collideOtherPlayer(entityIndex);

            // Check for NPC collision
//            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
//            interactNPC(npcIndex);

            if (!xCollisionOn) {
                worldX += velocityX;
            }
            if (!yCollisionOn) {
                worldY += velocityY;
            }

            spriteCounter++;
            if (velocityX == 0 && velocityY == 0) {
                spriteNum = 1;
            } else if (spriteCounter > 8) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 2;
                }
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1;
        }

        sendVelocityX = velocityX;
        sendVelocityY = velocityY;

        if (velocityX > 0)
        {
            sendVelocity = "+" + (int)velocityX;
        } else if (velocityX < 0)
        {
            sendVelocity = String.valueOf((int)velocityX);
        } else
        {
            sendVelocity = "00";
        }

        if (velocityY > 0)
        {
            sendVelocity += "+" + (int)velocityY;
        } else if (velocityY < 0)
        {
            sendVelocity += String.valueOf((int)velocityY);
        } else
        {
            sendVelocity += "00";
        }

        velocityX = 0;
        velocityY = 0;
    }

    public void pickUpObject(int index)
    {

        if (index != -1) {
            String  objectName = gp.obj[index].name;


            switch (objectName) {
                case "Key":
                    gp.playSFX(0);
                    hasKey++;
                    gp.obj[index] = null;
                    gp.ui.displayMessage("You found a key!");
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.playSFX(0);
                        gp.obj[index] = null;
                        hasKey--;
                        gp.ui.displayMessage("You unlocked a door!");
                    } else {
                        gp.ui.displayMessage("This door is locked.");
                    }
                    break;
                case "Chest":
                    if (hasKey > 0) {
                        gp.playSFX(0);
                        gp.obj[index] = null;
                        hasKey--;
                        gp.ui.gameFinished = true;
                    } else {
                        gp.ui.displayMessage("This chest is locked.");
                    }
                    break;
                case "YinYang":
                    gp.playSFX(0);
                    speed += 2;
                    gp.obj[index] = null;
                    gp.ui.displayMessage("Speed Increased!");
                    break;
            }
        }

    }

    public void collideOtherPlayer(int index)
    {
        if (index <= -1)
        {
            // Any additional interactions related to colliding with this particular otherPlayer
        }
    }

    public void interactNPC(int index)
    {
        if (index != -1)
        {
            // NPC interaction code
        }
    }

    @Override
    protected void drawImage(Graphics2D graphics2, BufferedImage image)
    {
        graphics2.drawImage(image, screenX, screenY, null);
    }

}
