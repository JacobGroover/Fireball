package entities;

import main.Client;
import main.GamePanel;
import object.projectiles.OBJ_Fireball;

import java.util.ArrayList;

public abstract class Projectile extends Entity
{

    public Entity owner;
    public boolean detonating;
    protected int deathTimer;

    // DOTS
    boolean applyDot;
    String dotType;
    public Projectile(GamePanel gp)
    {
        super(gp);
        detonating = false;
    }

    public void set(int projectileDestinationX, int projectileDestinationY, String direction, boolean alive, Entity owner)
    {
        this.worldX = owner.worldX;
        this.worldY = owner.worldY;
        if (owner instanceof Player)
        {
            this.worldX = Player.worldX;
            this.worldY = Player.worldY;
        }
        this.direction = direction;
        this.projectileDestinationX = projectileDestinationX;
        this.projectileDestinationY = projectileDestinationY;
        this.alive = alive;
        this.owner = owner;
        this.life = this.maxLife;
        spriteNum = 1;
        this.projectileDistanceX = owner.projectileDistanceX;
        this.projectileDistanceY = owner.projectileDistanceY;
        velocityX = owner.unitCircleX * speed;
        velocityY = owner.unitCircleY * speed;
    }

    public void update()
    {
        // NOTE: SOME PROJECTILE-SPECIFIC COLLISION INTERACTION CAN BE FOUND IN CollisionChecker CLASS

        // Reset collision
        xCollisionOn = false;
        yCollisionOn = false;

        // Check for tile collision
        gp.cChecker.checkTile(this);

        // Check for OtherPlayer collision
        int entityIndex = gp.cChecker.checkEntities(this, Client.otherPlayers);

        // Check for player collision
        boolean collideWithPlayer = gp.cChecker.checkEntity(this, gp.player);

        if (!detonating)
        {
            worldX += velocityX;
            projectileDistanceX = Math.abs(projectileDistanceX) - Math.abs(velocityX);

            worldY += velocityY;
            projectileDistanceY = Math.abs(projectileDistanceY) - Math.abs(velocityY);

            if (projectileDistanceX <= 0 || projectileDistanceY <= 0 || xCollisionOn || yCollisionOn)
            {
                detonating = true;
                spriteCounter = 0;
            }

            if (!detonating)
            {
                spriteCounter++;
                if (spriteCounter > 12)
                {
                    if (spriteNum == 1)
                    {
                        spriteNum = 2;
                    }
                    else if (spriteNum == 2)
                    {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            }
        }
        else
        {
            if (entityIndex != -1)
            {
                collideWithEntity(Client.otherPlayers, entityIndex);
            }
            if (collideWithPlayer)
            {
                collideWithEntity(gp.player);
            }
            detonateOnImpact();
        }
    }

    private void collideWithEntity(ArrayList<? extends Entity> fromList, int entityIndex)
    {
        // call child-specific projectile collision behavior
        if (this instanceof OBJ_Fireball)
        {
            if (!applyDot && this.owner != fromList.get(entityIndex))
            {
                int damage = this.attack;
                fromList.get(entityIndex).life -= damage;
                applyDot = true;
            }
            else if (detonating)
            {
                fromList.get(entityIndex).dotBurningDmgCounter = 180;
                fromList.get(entityIndex).isBurning = true;
            }
        }

    }

    public void collideWithEntity(Entity entity)
    {
        // call child-specific projectile collision behavior
        if (this instanceof OBJ_Fireball)
        {
            if (!applyDot && this.owner != entity)
            {
                int damage = this.attack;
                entity.life -= damage;
                applyDot = true;
            }
            else if (detonating)
            {
                entity.dotBurningDmgCounter = 180;
                entity.isBurning = true;
            }
        }
    }

    protected abstract void detonateOnImpact();

    @Override
    public void getImages(String imagePath)
    {
        up1 = setup2(imagePath + "Up1");
        up2 = setup2(imagePath + "Up2");
        up3 = setup(imagePath + "Detonate1");
        down1 = setup2(imagePath + "Down1");
        down2 = setup2(imagePath + "Down2");
        down3 = setup2(imagePath + "Detonate1");
        left1 = setup2(imagePath + "Left1");
        left2 = setup2(imagePath + "Left2");
        left3 = setup2(imagePath + "Detonate1");
        right1 = setup2(imagePath + "Right1");
        right2 = setup2(imagePath + "Right2");
        right3 = setup2(imagePath + "Detonate1");
        upLeft1 = setup2(imagePath + "UpLeft1");
        upLeft2 = setup2(imagePath + "UpLeft2");
        upLeft3 = setup2(imagePath + "Detonate1");
        upRight1 = setup2(imagePath + "UpRight1");
        upRight2 = setup2(imagePath + "UpRight2");
        upRight3 = setup2(imagePath + "Detonate1");
        downLeft1 = setup2(imagePath + "DownLeft1");
        downLeft2 = setup2(imagePath + "DownLeft2");
        downLeft3 = setup2(imagePath + "Detonate1");
        downRight1 = setup2(imagePath + "DownRight1");
        downRight2 = setup2(imagePath + "DownRight2");
        downRight3 = setup2(imagePath + "Detonate1");

        attackUp1 = setup(imagePath + "Detonate2");
        attackDown1 = setup(imagePath + "Detonate2");
        attackLeft1 = setup(imagePath + "Detonate2");
        attackRight1 = setup(imagePath + "Detonate2");
        attackUpLeft1 = setup(imagePath + "Detonate2");
        attackUpRight1 = setup(imagePath + "Detonate2");
        attackDownLeft1 = setup(imagePath + "Detonate2");
        attackDownRight1 = setup(imagePath + "Detonate2");

        image = setup2(imagePath + "Detonate3");
        image2 = setup2(imagePath + "Detonate4");

        // SET ANGLE OF PROJECTILE
    }
}
