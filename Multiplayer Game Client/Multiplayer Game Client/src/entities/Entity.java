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
    public String direction = "down";
    public boolean attacking = false;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean xCollisionOn = false;
    public boolean yCollisionOn = false;

    public BufferedImage image, image2;
    public String name;
    public boolean collision = false;

    // ENTITY MATH
    public double unitCircleX;
    public double unitCircleY;
    public double angle;

    // ENTITY STATS
    public boolean alive;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int attack;
    public Projectile projectile;

    // SKILL STATS

    // ITEM STATS
    public int projectileDestinationX;
    public int projectileDestinationY;
    public double projectileDistanceX;
    public double projectileDistanceY;
    public int useCost;

    public Entity(GamePanel gp)
    {
        this.gp = gp;
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
                }
                else
                {
                    if (spriteNum == 1) {image = downRight1;}
                    if (spriteNum == 2) {image = downRight2;}
                    if (spriteNum == 3) {image = downRight3;}
                }
                break;
        }

        drawImage(graphics2, image);

    }

    protected void drawImage(Graphics2D graphics2, BufferedImage image)
    {
        int screenX = (int)this.worldX - (int) Player.worldX + gp.player.screenX;  // gp.player.worldX
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
