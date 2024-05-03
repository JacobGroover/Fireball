package object;

import entities.Player;
import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image, image2;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    UtilityTool uTool = new UtilityTool();

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - (int) Player.worldX + gp.player.screenX;
        int screenY = worldY - (int)Player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > Player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < Player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > Player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < Player.worldY + gp.player.screenY)
        {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            //g2.drawImage(image2, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }

        /*int screenX = worldX - (int)gp.player.worldX + gp.player.screenX;
        int screenY = worldY - (int)gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
        {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }*/

    }

}
