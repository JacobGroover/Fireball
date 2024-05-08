package object.projectiles;

import entities.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile
{

    GamePanel gp;
    public OBJ_Fireball(GamePanel gp)
    {
        super(gp);
        this.gp = gp;

        name = "Fireball";
        speed = 5;
        maxLife = 20;
        life = maxLife;
        attack = 20;
        useCost = 10;
        alive = false;
        getImages("/objects/projectiles/Fireball");
    }

    @Override
    protected void detonateOnImpact()
    {
        // detonate fireball

        // despawn projectile
        this.alive = false;
    }

}
