package main;

import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_YinYang;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }


    public void setObject() {
        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 39 * gp.tileSize;
        gp.obj[0].worldY = 11 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 44 * gp.tileSize;
        gp.obj[1].worldY = 47 * gp.tileSize;

        gp.obj[2] = new OBJ_Key(gp);
        gp.obj[2].worldX = 17 * gp.tileSize;
        gp.obj[2].worldY = 3 * gp.tileSize;

        gp.obj[3] = new OBJ_Door(gp);
        gp.obj[3].worldX = 12 * gp.tileSize;
        gp.obj[3].worldY = 41 * gp.tileSize;

        gp.obj[4] = new OBJ_Chest(gp);
        gp.obj[4].worldX = 12 * gp.tileSize;
        gp.obj[4].worldY = 38 * gp.tileSize;

        gp.obj[5] = new OBJ_YinYang(gp);
        gp.obj[5].worldX = 13 * gp.tileSize;
        gp.obj[5].worldY = 39 * gp.tileSize;

    }

}
