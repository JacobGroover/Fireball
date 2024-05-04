package object;

import entities.Entity;
import main.GamePanel;

public class OBJ_YinYang extends Entity
{

    public OBJ_YinYang(GamePanel gp)
    {
        super(gp);
        name = "YinYang";
        down1 = setup("/objects/YinYang");
    }
}
