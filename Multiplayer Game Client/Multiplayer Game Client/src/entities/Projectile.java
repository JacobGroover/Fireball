package entities;

import main.Client;
import main.GamePanel;
import object.projectiles.OBJ_Fireball;

import java.util.ArrayList;

public abstract class Projectile extends Entity
{

    Entity owner;
    boolean detonating;
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

//        projectileDestinationX = Math.abs(Math.abs(projectileDestinationX) - (int)worldX);
//        projectileDestinationY = Math.abs(Math.abs(projectileDestinationY) - (int)worldY);

//        if (this.projectileX < owner.worldX)
//        {
//            projectileDistanceX *= -1;
//            this.projectileX *= -1;
//        }
//        if (this.projectileY < owner.worldY)
//        {
//            projectileDistanceY *= -1;
//            this.projectileY *= -1;
//        }

//        switch (this.direction)
//        {
//            case "up":
//                break;
//            case "down":
//                break;
//            case "left":
//                break;
//            case "right":
//                break;
//            case "upLeft":
//                break;
//            case "upRight":
//                break;
//            case "downLeft":
//                break;
//            case "downRight":
//                break;
//        }
    }

    public void update()
    {
        checkProjectileCollision();

        if (!detonating)
        {

            if (!xCollisionOn && !xTileCollisionOn)
            {
                worldX += velocityX;
                projectileDistanceX = Math.abs(projectileDistanceX) - Math.abs(velocityX);
            }
            if (!yCollisionOn && !yTileCollisionOn)
            {
                worldY += velocityY;
                projectileDistanceY = Math.abs(projectileDistanceY) - Math.abs(velocityY);
            }

            if (projectileDistanceX <= 0 || projectileDistanceY <= 0 || xCollisionOn || yCollisionOn || xTileCollisionOn || yTileCollisionOn)
            {
                detonating = true;
                applyDot = true;
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
            detonateOnImpact();
        }
    }

    protected void checkProjectileCollision()
    {
        // NOTE: SINCE PROJECTILES CAN CONTINUALLY COLLIDE WITH TILES AFTER DETONATION, MUST RESET COLLISION TO FALSE AFTER CHECKING TILE COLLISION

        // Check for tile collision
        xCollisionOn = false;
        yCollisionOn = false;
        gp.cChecker.checkTile(this);

        if (xCollisionOn)
        {
            xTileCollisionOn = true;
        }
        if (yCollisionOn)
        {
            yTileCollisionOn = true;
        }

        xCollisionOn = false;
        yCollisionOn = false;

        // Check for OtherPlayer collision
        int entityIndex = gp.cChecker.checkEntity(this, Client.otherPlayers);
        if (entityIndex != -1)
        {
            if (Client.otherPlayers.get(entityIndex) != this.owner)
            {
                collideWithEntity(Client.otherPlayers, entityIndex);
            }
        }

        // Check for player collision
        if (detonating && gp.cChecker.checkEntity(this, gp.player))
        {
            collideWithEntity(gp.player);
        }

//        if (owner instanceof Player)
//        {
//            // Check for OtherPlayer collision
//            int entityIndex = gp.cChecker.checkEntity(this, Client.otherPlayers);
//            if (entityIndex != -1)
//            {
//                collideWithEntity(Client.otherPlayers, entityIndex);
//            }
//            if (detonating)
//            {
//                gp.cChecker.checkPlayer(this);
//            }
//        }
//        else
//        {
//            // Check for Player collision
//            gp.cChecker.checkPlayer(this);
//        }

    }

    private void collideWithEntity(ArrayList<? extends Entity> fromList, int entityIndex)
    {
        // call child-specific projectile collision behavior
        if (this instanceof OBJ_Fireball)
        {
            if (!applyDot)
            {
                int damage = this.attack;
                fromList.get(entityIndex).life -= damage;
//                applyDot = true;
            }
            else if (detonating)
            {
                boolean applied = false;
                for (int i = 0; i < fromList.get(entityIndex).dotAL.size(); i++)
                {
                    if (fromList.get(entityIndex).dotAL.get(i).equals("fire"))
                    {
                        applied = true;
                        fromList.get(entityIndex).dotFireCounter = 10;
                    }
                }
                if (!applied)
                {
                    fromList.get(entityIndex).dotAL.add("fire");
                }
            }
        }

    }

    public void collideWithEntity(Entity entity)
    {
        // call child-specific projectile collision behavior
        if (this instanceof OBJ_Fireball)
        {

            if (!applyDot)
            {
                int damage = this.attack;
                entity.life -= damage;
//                applyDot = true;
            }
            else if (detonating)
            {
                boolean applied = false;
                for (int i = 0; i < entity.dotAL.size(); i++)
                {
                    if (entity.dotAL.get(i).equals("fire"))
                    {
                        applied = true;
                        entity.dotFireCounter = 10;
                    }
                }
                if (!applied)
                {
                    entity.dotAL.add("fire");
                    entity.hasDot = true;
                }
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
