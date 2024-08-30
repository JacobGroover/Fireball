package object;

import entities.Entity;
import main.GamePanel;

public class OBJ_BloodDroplet extends Entity
{
    public OBJ_BloodDroplet(GamePanel gp)
    {
        super(gp);
        name = "BloodDroplet";
        image = setup2("/objects/BloodDroplet");
        image2 = setup2("/objects/EmptyBloodDroplet");
    }

}
