package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_BloodDroplet extends SuperObject
{

    GamePanel gp;

    public OBJ_BloodDroplet(GamePanel gp) {
        name = "BloodDroplet";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/BloodDroplet.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/objects/EmptyBloodDroplet.png"));
            image = uTool.scaleImage(image, gp.tileSize/2, gp.tileSize/2);
            image2 = uTool.scaleImage(image2, gp.tileSize/2, gp.tileSize/2);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
