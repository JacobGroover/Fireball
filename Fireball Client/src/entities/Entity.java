/** Class: Entity
 *  @author Jacob Groover
 *
 *  This class – Is the super class/parent class for entities (characters, monsters, NPCs, etc.)
 */

package entities;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity {

    GamePanel gp;
    public double worldX, worldY;
    public double startX, startY;
    public double velocityX;
    public double velocityY;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3,
            upLeft1, upLeft2, upLeft3, upRight1, upRight2, upRight3, downLeft1, downLeft2, downLeft3, downRight1, downRight2, downRight3;
    public BufferedImage attackUp1, attackDown1, attackLeft1, attackRight1, attackUpLeft1, attackUpRight1, attackDownLeft1, attackDownRight1;
    public BufferedImage burningDeath1, burningDeath2, burningDeath3, burningDeath4, burningDeath5, burningDeath6, burningDeath7, burningDeath8, burningDeath9, burningDeath10;

    // Images for visual effects applied to an Entity
    public BufferedImage burning1, burning2;
    public String direction = "down";
    public boolean attacking = false;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean xCollisionOn = false;
    public boolean yCollisionOn = false;

    public boolean xTileCollisionOn;    // Not currently used, might be useful later
    public boolean yTileCollisionOn;    // Not currently used, might be useful later

    public BufferedImage image, image2;
    public String name;
    public boolean collision = false;

    // ENTITY MATH
    public double unitCircleX;
    public double unitCircleY;
    public double angle360;

    // ENTITY STATS
    public boolean alive;
    public boolean dying;
    public int dyingCounter1;
    public int dyingCounter2;
    protected String deathType = "fire";    // Once more deathTypes are added to the game, another "generic" deathType needs to be added and set as the default. Until then, this avoids possible null pointer exceptions
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int attack;
    boolean isBurning;
    int dotBurningVfxCounter;
    int dotBurningDmgCounter;

    // SKILL STATS
    public boolean skill1; // currently used for getting skills used by otherPlayers from server

    // ITEM STATS
    public int projectileDestinationX;
    public int projectileDestinationY;
    public double projectileDistanceX;
    public double projectileDistanceY;
    public int useCost;

    public Entity(GamePanel gp)
    {
        this.gp = gp;

        if (this instanceof Player || this instanceof OtherPlayer)
        {
            getVFX();
        }
    }

    protected void attacking(int pointX, int pointY)
    {
        double worldX = this.worldX;
        double worldY = this.worldY;

        if (this instanceof Player)
        {
            worldX = Player.worldX;
            worldY = Player.worldY;
        }

        projectileDestinationX = (int) worldX + pointX - gp.screenWidth/2;
        projectileDestinationY = (int)worldY + pointY - gp.screenHeight/2;

        projectileDistanceX = projectileDestinationX - worldX;
        projectileDistanceY = projectileDestinationY - worldY;

        double hyp = Math.hypot(projectileDistanceX, projectileDistanceY);
        if (projectileDistanceX != 0)
        {
            unitCircleX = projectileDistanceX / hyp;
        }
        if (projectileDistanceY != 0)
        {
            unitCircleY = projectileDistanceY / hyp;
        }

        double radian = Math.atan2(unitCircleY, unitCircleX);
        angle360 = radian * (180 / Math.PI);
        if (angle360 < 0.0)
        {
            angle360 += 360.0;
        }

        if (angle360 > 202.5 && angle360 < 247.5)
        {
            direction = "upLeft";
        } else if (angle360 >= 247.5 && angle360 <= 292.5)
        {
            direction = "up";
        } else if (angle360 > 292.5 && angle360 < 337.5)
        {
            direction = "upRight";
        }
        else if ((angle360 >= 337.5 && angle360 <= 360.0) || angle360 <= 22.5)
        {
            direction = "right";
        } else if (angle360 > 22.5 && angle360 < 67.5)
        {
            direction = "downRight";
        } else if (angle360 >= 67.5 && angle360 <= 112.5)
        {
            direction = "down";
        } else if (angle360 > 112.5 && angle360 < 157.5)
        {
            direction = "downLeft";
        } else if (angle360 >= 157.5 && angle360 <= 202.5)
        {
            direction = "left";
        }
        else
        {
            attacking = false;
        }

    }

    public void getImages(String imagePath) {
        up1 = setup(imagePath + "Up1");
        up2 = setup(imagePath + "Up2");
        up3 = setup(imagePath + "Up3");
        down1 = setup(imagePath + "Down1");
        down2 = setup(imagePath + "Down2");
        down3 = setup(imagePath + "Down3");
        left1 = setup(imagePath + "Left1");
        left2 = setup(imagePath + "Left2");
        left3 = setup(imagePath + "Left3");
        right1 = setup(imagePath + "Right1");
        right2 = setup(imagePath + "Right2");
        right3 = setup(imagePath + "Right3");
        upLeft1 = setup(imagePath + "UpLeft1");
        upLeft2 = setup(imagePath + "UpLeft2");
        upLeft3 = setup(imagePath + "UpLeft3");
        upRight1 = setup(imagePath + "UpRight1");
        upRight2 = setup(imagePath + "UpRight2");
        upRight3 = setup(imagePath + "UpRight3");
        downLeft1 = setup(imagePath + "DownLeft1");
        downLeft2 = setup(imagePath + "DownLeft2");
        downLeft3 = setup(imagePath + "DownLeft3");
        downRight1 = setup(imagePath + "DownRight1");
        downRight2 = setup(imagePath + "DownRight2");
        downRight3 = setup(imagePath + "DownRight3");
    }

    public void getVFX()
    {
        burning1 = setup("/objects/projectiles/FireballDetonate3");
        burning2 = setup("/objects/projectiles/FireballDetonate4");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return image;
    }

    public BufferedImage setup2(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize/2, gp.tileSize/2);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return image;
    }

    public void draw(Graphics2D graphics2)
    {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (attacking)
                {
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = attackUp1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
                    if (spriteNum == 3) {image = up3;}
                }
                break;
            case "down":
                if (attacking)
                {
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = attackDown1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                    if (spriteNum == 3) {image = down3;}
                }
                break;
            case "left":
                if (attacking)
                {
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = attackLeft1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                    if (spriteNum == 3) {image = left3;}
                }
                break;
            case "right":
                if (attacking)
                {
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = attackRight1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                    if (spriteNum == 3) {image = right3;}
                }
                break;
            case "upLeft":
                if (attacking)
                {
                    if (spriteNum == 1) {image = upLeft1;}
                    if (spriteNum == 2) {image = attackUpLeft1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = upLeft1;}
                    if (spriteNum == 2) {image = upLeft2;}
                    if (spriteNum == 3) {image = upLeft3;}
                }
                break;
            case "upRight":
                if (attacking)
                {
                    if (spriteNum == 1) {image = upRight1;}
                    if (spriteNum == 2) {image = attackUpRight1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = upRight1;}
                    if (spriteNum == 2) {image = upRight2;}
                    if (spriteNum == 3) {image = upRight3;}
                }
                break;
            case "downLeft":
                if (attacking)
                {
                    if (spriteNum == 1) {image = downLeft1;}
                    if (spriteNum == 2) {image = attackDownLeft1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = downLeft1;}
                    if (spriteNum == 2) {image = downLeft2;}
                    if (spriteNum == 3) {image = downLeft3;}
                }
                break;
            case "downRight":
                if (attacking)
                {
                    if (spriteNum == 1) {image = downRight1;}
                    if (spriteNum == 2) {image = attackDownRight1;}
                    if (spriteNum == -1) {image = this.image;}
                    if (spriteNum == -2) {image = this.image2;}
                }
                else
                {
                    if (spriteNum == 1) {image = downRight1;}
                    if (spriteNum == 2) {image = downRight2;}
                    if (spriteNum == 3) {image = downRight3;}
                }
                break;
        }


        if (dying)
        {
            image = dyingAnimation(graphics2, image);
        }

        if (isBurning)
        {
            if (dotBurningVfxCounter == 0)
            {
                dotBurningVfxCounter = 24;
            }
            if (dotBurningVfxCounter > 12 && dotBurningVfxCounter <= 24)
            {
                drawImage(graphics2, burning1);
            }
            if (dotBurningVfxCounter <= 12)
            {
                drawImage(graphics2, burning2);
            }
            dotBurningVfxCounter--;
        }
        else
        {
            dotBurningVfxCounter = 0;
        }

        drawImage(graphics2, image);

    }

    private BufferedImage dyingAnimation(Graphics2D graphics2, BufferedImage image)
    {
        if (deathType.equals("fire"))
        {
            if (dyingCounter1 > 348 && dyingCounter1 <= 360)
            {
                image = burningDeath1;
            }
            if (dyingCounter1 > 336 && dyingCounter1 <= 348)
            {
                image = burningDeath2;
            }
            if (dyingCounter1 > 324 && dyingCounter1 <= 336)
            {
                image = burningDeath3;
            }
            if (dyingCounter1 > 312 && dyingCounter1 <= 324)
            {
                image = burningDeath4;
            }
            if (dyingCounter1 > 300 && dyingCounter1 <= 312)
            {
                image = burningDeath5;
            }
            if (dyingCounter1 > 288 && dyingCounter1 <= 300)
            {
                image = burningDeath6;
            }
            if (dyingCounter1 > 0 && dyingCounter1 <= 288)
            {
                if (dyingCounter1 % 48 == 0)
                {
                    dyingCounter2 = 48;
                }
                if ((dyingCounter2 > 36 && dyingCounter2 <= 48) || (dyingCounter2 > 12 && dyingCounter2 <= 24))
                {
                    image = burningDeath7;
                }
                if ((dyingCounter2 > 24 && dyingCounter2 <= 36) || dyingCounter2 <= 12)
                {
                    image = burningDeath8;
                }
                if (dyingCounter2 > 24 && dyingCounter2 <= 48)
                {
                    drawImage(graphics2, burningDeath9);
                }
                if (dyingCounter2 <= 24)
                {
                    drawImage(graphics2, burningDeath10);
                }

                dyingCounter2--;
            }
        }
        dyingCounter1--;
        if (dyingCounter1 <= 0)
        {
            if (this instanceof Player)
            {
                if (gp.gameState != gp.GAME_MENU_STATE)
                {
                    gp.gameState = gp.GAME_OVER_STATE;
                }
            }
            if (this instanceof Player || this instanceof OtherPlayer)
            {
                dyingCounter1 = 48;     // For multiplayer games. Keeps death animation running until the dead player leaves the game or respawns
            }
        }
        return image;
    }

    protected void drawImage(Graphics2D graphics2, BufferedImage image)
    {
        int screenX = (int)this.worldX - (int) Player.worldX + gp.player.screenX;
        int screenY = (int)this.worldY - (int) Player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > Player.worldX - gp.player.screenX &&
                this.worldX - gp.tileSize < Player.worldX + gp.player.screenX &&
                this.worldY + gp.tileSize > Player.worldY - gp.player.screenY &&
                this.worldY - gp.tileSize < Player.worldY + gp.player.screenY)
        {
            graphics2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

}
