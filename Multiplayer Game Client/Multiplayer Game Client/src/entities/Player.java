/** Class: Player
 *  @author Jacob Groover
 *
 *  This class – Player class; subclass of Entity
 */

package entities;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
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

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
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
        life = 30;
    }

    public void getPlayerImage() {

        up1 = setup("FillerPlayerUp1");
        up2 = setup("FillerPlayerUp2");
        up3 = setup("FillerPlayerUp3");
        down1 = setup("FillerPlayerDown1");
        down2 = setup("FillerPlayerDown2");
        down3 = setup("FillerPlayerDown3");
        left1 = setup("FillerPlayerLeft1");
        left2 = setup("FillerPlayerLeft2");
        left3 = setup("FillerPlayerLeft3");
        right1 = setup("FillerPlayerRight1");
        right2 = setup("FillerPlayerRight2");
        right3 = setup("FillerPlayerRight3");
        upLeft1 = setup("FillerPlayerUpLeft1");
        upLeft2 = setup("FillerPlayerUpLeft2");
        upLeft3 = setup("FillerPlayerUpLeft3");
        upRight1 = setup("FillerPlayerUpRight1");
        upRight2 = setup("FillerPlayerUpRight2");
        upRight3 = setup("FillerPlayerUpRight3");
        downLeft1 = setup("FillerPlayerDownLeft1");
        downLeft2 = setup("FillerPlayerDownLeft2");
        downLeft3 = setup("FillerPlayerDownLeft3");
        downRight1 = setup("FillerPlayerDownRight1");
        downRight2 = setup("FillerPlayerDownRight2");
        downRight3 = setup("FillerPlayerDownRight3");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return image;
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

    public void pickUpObject(int index) {

        if (index != 999) {
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

    public void draw(Graphics2D graphics2) {

        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up3;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                break;
            case "upLeft":
                if (spriteNum == 1) {
                    image = upLeft1;
                }
                if (spriteNum == 2) {
                    image = upLeft2;
                }
                if (spriteNum == 3) {
                    image = upLeft3;
                }
                break;
            case "upRight":
                if (spriteNum == 1) {
                    image = upRight1;
                }
                if (spriteNum == 2) {
                    image = upRight2;
                }
                if (spriteNum == 3) {
                    image = upRight3;
                }
                break;
            case "downLeft":
                if (spriteNum == 1) {
                    image = downLeft1;
                }
                if (spriteNum == 2) {
                    image = downLeft2;
                }
                if (spriteNum == 3) {
                    image = downLeft3;
                }
                break;
            case "downRight":
                if (spriteNum == 1) {
                    image = downRight1;
                }
                if (spriteNum == 2) {
                    image = downRight2;
                }
                if (spriteNum == 3) {
                    image = downRight3;
                }
                break;
        }

        graphics2.drawImage(image, screenX, screenY, null);
    }

}
