package object.projectiles;

import entities.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fireball extends Projectile
{

    GamePanel gp;
    public OBJ_Fireball(GamePanel gp)
    {
        super(gp);
        this.gp = gp;

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        name = "Fireball";
        speed = 5;
        maxLife = 20;
        life = maxLife;
        attack = 20;
        useCost = 10;
        alive = false;
        deathTimer = 10;
        getImages("/objects/projectiles/Fireball");
    }

    @Override
    protected void detonateOnImpact()
    {
        // detonate fireball
        spriteCounter++;
        if (spriteCounter <= 12)
        {
            spriteNum = 3;
        }
        if (spriteCounter > 12 && spriteCounter <= 24)
        {
            spriteNum = 2;
            attacking = true;
        }
        if (spriteCounter > 24 && spriteCounter <= 36)
        {
            spriteNum = -1;
        }
        if (spriteCounter > 36 && spriteCounter <= 48)
        {
            spriteNum = -2;
        }
        if (spriteCounter > 48)
        {
            spriteCounter = 24;
            deathTimer--;
        }
        if (deathTimer <= 0)
        {
            // despawn projectile
            this.alive = false;
        }

    }

}
