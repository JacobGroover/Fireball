/** Class: OtherPlayer
 *  @author Jacob Groover
 *
 *  This class – OtherPlayer class; subclass of Entity, represents an object of another player on the server.
 */

package entities;

import main.GamePanel;
import object.projectiles.OBJ_Fireball;

import java.awt.*;

public class OtherPlayer extends Entity {

    public String clientUserName;
    //private double lastWorldX;  // UDP
    //private double lastWorldY;  // UDP
    public int hasKey = 0;
    public boolean joinedGame;
    public int mouseX;
    public int mouseY;

    public OtherPlayer(GamePanel gp, String clientUserName)
    {

        super(gp);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues(clientUserName);
        getImages("/player/FillerPlayer");
    }

    public void setDefaultValues(String clientUserName) {
        //this.lastWorldX = 0.0;   // UDP
        //this.lastWorldY = 0.0;   // UDP
        startX = 23.00;
        startY = 21.00;
        this.worldX = gp.tileSize * startX;
        this.worldY = gp.tileSize * startY;
        speed = 4;
        direction = "down";
        this.clientUserName = clientUserName;
        velocityX = 0;
        velocityY = 0;
        joinedGame = true;

        maxLife = 100;
        life = maxLife;
    }

    public void update()
    {

        if (attacking)
        {
            attackingAnimation();
        }

        // CHECK FOR DAMAGE OVER TIME
        if (isBurning)
        {
            if (dotBurningDmgCounter <= 0)
            {
                isBurning = false;
            }
            else if (dotBurningDmgCounter % 18 == 0)
            {
                life--;
            }
            dotBurningDmgCounter--;
        }

        // CHECK FOR SKILL/ABILITY INPUTS
        if (gp.gameState == gp.PLAY_STATE && skill1)
        {
            attacking = true;
            skill1 = false;
            attacking();
        }

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
            gp.cChecker.checkTile(this);

            // Check for object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check for Player collision (unnecessary due to OtherPlayer collision check in Player.java)
            //gp.cChecker.checkPlayer(this);

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

        xCollisionOn = false;
        yCollisionOn = false;

        velocityX = 0;
        velocityY = 0;
    }

    private void attackingAnimation()
    {
        spriteCounter++;
        if (spriteCounter <= 12)
        {
            spriteNum = 2;
        }
        if (spriteCounter > 12)
        {
            castFireball();
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void castFireball()
    {
        // SPAWN PROJECTILE
        Projectile p = new OBJ_Fireball(gp);
        p.set(projectileDestinationX, projectileDestinationY, direction, true, this);

        // ADD PROJECTILE TO ARRAYLIST
        gp.projectileAL.add(p);
//        gp.playSFX(0);
    }

    private void attacking()
    {
        projectileDestinationX = (int) worldX + mouseX - gp.screenWidth/2;
        projectileDestinationY = (int)worldY + mouseY - gp.screenHeight/2;

        projectileDistanceX = projectileDestinationX - worldX;
        projectileDistanceY = projectileDestinationY - worldY;

        double hyp = Math.hypot(projectileDistanceX, projectileDistanceY);
        if (projectileDistanceX != 0)
        {
            unitCircleX = projectileDistanceX / hyp;
//            projectileDistanceX /= hyp;
        }
        if (projectileDistanceY != 0)
        {
            unitCircleY = projectileDistanceY / hyp;
//            projectileDistanceY /= hyp;
        }

        double radian = Math.atan2(unitCircleY, unitCircleX);
        angle = radian * (180 / Math.PI);
        if (angle < 0.0)
        {
            angle += 360.0;
        }

        if (angle > 202.5 && angle < 247.5)
        {
            direction = "upLeft";
        } else if (angle >= 247.5 && angle <= 292.5)
        {
            direction = "up";
        } else if (angle > 292.5 && angle < 337.5)
        {
            direction = "upRight";
        }
        else if ((angle >= 337.5 && angle <= 360.0) || angle <= 22.5)
        {
            direction = "right";
        } else if (angle > 22.5 && angle < 67.5)
        {
            direction = "downRight";
        } else if (angle >= 67.5 && angle <= 112.5)
        {
            direction = "down";
        } else if (angle > 112.5 && angle < 157.5)
        {
            direction = "downLeft";
        } else if (angle >= 157.5 && angle <= 202.5)
        {
            direction = "left";
        }
        else
        {
            attacking = false;
        }

    }

    public void pickUpObject(int index) {

        if (index != -1) {
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

}
