package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[25];    // Number of tiles in the game
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map02.txt");
    }

    public void getTileImage() {
        for (int i = 0; i < tile.length; i++) {
            tile[i] = new Tile();
        }
        setup(0, "Grass1", false);
        setup(1, "Grass2", false);
        setup(2, "Grass3", false);
        setup(3, "Grass4", false);
        setup(4, "Grass5", false);
        setup(5, "Grass6", false);
        setup(6, "Grass7", false);
        setup(7, "Grass8", false);
        setup(8, "Grass9", false);
        setup(9, "Grass10", false);
        setup(10, "Grass11", false);
        setup(11, "Grass12", false);
        setup(12, "Grass13", false);
        setup(13, "Stone1", true);
        setup(14, "Stone2", true);
        setup(15, "Stone3", true);
        setup(16, "Stone4", true);
        setup(17, "Stone5", true);
        setup(18, "Stone6", true);
        setup(19, "Stone7", true);
        setup(20, "Stone8", true);
        setup(21, "Stone9", true);
        setup(22, "Water1", true);
        setup(23, "StoneFloor2", false);
        setup(24, "StoneFloor", false);

    }

    public void setup(int index, String imageName, boolean collision) {

        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void loadMap(String filePath) {

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g2) {

        //g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            /*int screenX = worldX - (int) Player.worldX + gp.player.screenX;
            int screenY = worldY - (int) Player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > Player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < Player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > Player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < Player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }*/

            // Comment out for UDP
            int screenX = worldX - (int)gp.player.worldX + gp.player.screenX;
            int screenY = worldY - (int)gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

    }

}
