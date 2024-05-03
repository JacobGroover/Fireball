/** Class: Entity
 *  @author Jacob Groover
 *
 *  This class – Is the super class/parent class for entities (characters, monsters, NPCs, etc.)
 */

package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {

    public double worldX, worldY;
    public double startX, startY;
    public int speed;
    public double velocityX;
    public double velocityY;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3,
            upLeft1, upLeft2, upLeft3, upRight1, upRight2, upRight3, downLeft1, downLeft2, downLeft3, downRight1, downRight2, downRight3;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean xCollisionOn = false;
    public boolean yCollisionOn = false;

    // ENTITY STATUS
    public int maxLife;
    public int life;

}
