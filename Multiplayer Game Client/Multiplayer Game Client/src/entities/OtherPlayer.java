/** Class: OtherPlayer
 *  @author Jacob Groover
 *
 *  This class – OtherPlayer class; subclass of Entity, represents an object of another player on the server.
 */

package entities;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OtherPlayer extends Entity {

    GamePanel gp;
    public String clientUserName;
    //private double lastWorldX;  // UDP
    //private double lastWorldY;  // UDP
    public int hasKey = 0;

    public OtherPlayer(GamePanel gp, String messageReceived) {

        this.gp = gp;

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues(messageReceived);
        getPlayerImage();
    }

    public void setDefaultValues(String messageReceived) {
        //this.lastWorldX = 0.0;   // UDP
        //this.lastWorldY = 0.0;   // UDP
        startX = 23.00;
        startY = 21.00;
        worldX = gp.tileSize * startX;
        worldY = gp.tileSize * startY;
        speed = 4;
        direction = "down";
        clientUserName = messageReceived;
        velocityX = 0;
        velocityY = 0;
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
        /*System.out.println("VelocityX: " + velocityX);
        System.out.println("VelocityY " + velocityY);*/

        //velocityX = worldX - lastWorldX;  // UDP
        //velocityY = worldY - lastWorldY;  // UDP
        if (velocityX != 0 || velocityY != 0) {


            if (velocityX == 0 && velocityY < 0) {
                direction = "up";
            }
            if (velocityX == 0 && velocityY > 0) {
                direction = "down";
            }
            if (velocityX < 0 && velocityY == 0) {
                direction = "left";
            }
            if (velocityX > 0 && velocityY == 0) {
                direction = "right";
            }

            if (velocityX < 0 && velocityY < 0) {
                direction = "upLeft";
            }
            if (velocityX > 0 && velocityY < 0) {
                direction = "upRight";
            }
            if (velocityX < 0 && velocityY > 0) {
                direction = "downLeft";
            }
            if (velocityX > 0 && velocityY > 0) {
                direction = "downRight";
            }

            // Normalize movement vector
            // commented out for UDP
            double length = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));

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
                //lastWorldX += velocityX;    // UDP
                worldX += velocityX;
            }
            if (!yCollisionOn) {
                //lastWorldY += velocityY;    // UDP
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
        velocityX = 0;
        velocityY = 0;
    }

    public void pickUpObject(int index) {

        if (index != 999) {
            String  objectName = gp.obj[index].name;


            switch (objectName) {
                case "Key":
                    //gp.playSFX(0);
                    hasKey++;
                    gp.obj[index] = null;
                    //gp.ui.displayMessage("You found a key!");
                    break;
                case "Door":
                    if (hasKey > 0) {
                        //gp.playSFX(0);
                        gp.obj[index] = null;
                        hasKey--;
                        //gp.ui.displayMessage("You unlocked a door!");
                    } /*else {
                        gp.ui.displayMessage("This door is locked.");
                    }*/
                    break;
                case "Chest":
                    if (hasKey > 0) {
                        //gp.playSFX(0);
                        gp.obj[index] = null;
                        hasKey--;
                        gp.ui.gameFinished = true;
                    } /*else {
                        gp.ui.displayMessage("This chest is locked.");
                    }*/
                    break;
                case "YinYang":
                    //gp.playSFX(0);
                    speed += 2;
                    gp.obj[index] = null;
                    //gp.ui.displayMessage("Speed Increased!");
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

        // UDP
        /*int screenX = (int)lastWorldX - (int) Player.worldX + gp.player.screenX;  // gp.player.worldX
        int screenY = (int)lastWorldY - (int) Player.worldY + gp.player.screenY;

        if (lastWorldX + gp.tileSize > Player.worldX - gp.player.screenX &&
                lastWorldX - gp.tileSize < Player.worldX + gp.player.screenX &&
                lastWorldY + gp.tileSize > Player.worldY - gp.player.screenY &&
                lastWorldY - gp.tileSize < Player.worldY + gp.player.screenY)
        {
            graphics2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }*/

        // commented out for UDP
        int screenX = (int)worldX - (int) gp.player.worldX + gp.player.screenX;  // gp.player.worldX
        int screenY = (int)worldY - (int) gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
        {
            graphics2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }

        //graphics2.drawImage(image, screenX, screenY, null);
    }

}
