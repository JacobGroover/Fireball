package object;

import entities.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity
{

    public OBJ_Chest(GamePanel gp)
    {
        super(gp);
        name = "Chest";
        down1 = setup("/objects/Chest");
    }
}
