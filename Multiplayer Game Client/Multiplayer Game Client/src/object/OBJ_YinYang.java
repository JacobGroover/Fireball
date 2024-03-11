package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_YinYang extends SuperObject {

    GamePanel gp;

    public OBJ_YinYang(GamePanel gp) {
        name = "YinYang";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/YinYang.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
