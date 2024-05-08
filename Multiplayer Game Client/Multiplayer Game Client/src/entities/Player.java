/** Class: Player
 *  @author Jacob Groover
 *
 *  This class – Player class; subclass of Entity
 */

package entities;

import main.*;
import object.projectiles.OBJ_Fireball;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler keyH;
    MouseHandler mouseH;

    public final int screenX;
    public final int screenY;
    public static double startX;
    public static double startY;
    public static double worldX;  // UDP
    public static double worldY;
    int cooldown1Counter;

    public int hasKey = 0;
    public static String sendVelocity = "00";
    public static double sendVelocityX = 0;
    public static double sendVelocityY = 0;

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseH)
    {
        super(gp);

        this.keyH = keyH;
        this.mouseH = mouseH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getImages("/player/FillerPlayer");
        getPlayerAttackImages("/player/attack/FillerPlayerCast");
        {

        }
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
//        projectile = new OBJ_Fireball(gp);
        cooldown1Counter = 60;
    }

    private void getPlayerAttackImages(String imagePath)
    {
        attackUp1 = setup(imagePath + "Up1");
        attackDown1 = setup(imagePath + "Down1");
        attackLeft1 = setup(imagePath + "Left1");
        attackRight1 = setup(imagePath + "Right1");
        attackUpLeft1 = setup(imagePath + "UpLeft1");
        attackUpRight1 = setup(imagePath + "UpRight1");
        attackDownLeft1 = setup(imagePath + "DownLeft1");
        attackDownRight1 = setup(imagePath + "DownRight1");
    }

    public void update()
    {

//        if (gp.gameState == gp.PLAY_STATE && mouseH.playPressed1)
//        {
//            attacking = true;
//        }
        if (gp.gameState == gp.PLAY_STATE && attacking)
        {
            attackingAnimation();
        }

        // CHECK FOR SKILL/ABILITY INPUTS
        if (gp.gameState == gp.PLAY_STATE && !attacking && mouseH.playPressed1 && !mouseH.playPressed1Cooldown)
        {
            attacking = true;
            mouseH.playPressed1Cooldown = true;
            mouseH.playPressed1 = false;
            attacking();
        }
        else if (gp.gameState == gp.PLAY_STATE && !attacking && (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed))
        {
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

            // Check Event triggers
            gp.eventHandler.checkEvent();

            if (!xCollisionOn)
            {
                worldX += velocityX;
            }
            if (!yCollisionOn)
            {
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

        // UPDATE COOLDOWN TIMERS
        if (mouseH.playPressed1Cooldown)
        {
            cooldown1Counter--;
            if (cooldown1Counter <= 0)
            {
                mouseH.playPressed1Cooldown = false;
                cooldown1Counter = 60;
            }
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

    private void attacking()
    {
        projectileDestinationX = (int) worldX + mouseH.mouseX - gp.screenWidth/2;
        projectileDestinationY = (int)worldY + mouseH.mouseY - gp.screenHeight/2;

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

    private void attackingAnimation()
    {
        spriteCounter++;
//        if (spriteCounter <= 3)
//        {
//            spriteNum = 1;
//        }
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
//        gp.projectileAL.add(projectile);
        gp.projectileAL.add(p);
//        gp.playSFX(0);
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
