package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject {

    GamePanel gp;

    public OBJ_Key(GamePanel gp) {
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/Key.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
