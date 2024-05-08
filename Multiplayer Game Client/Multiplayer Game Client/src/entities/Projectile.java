package entities;

import main.Client;
import main.GamePanel;
import object.projectiles.OBJ_Fireball;

import java.util.ArrayList;

public abstract class Projectile extends Entity
{

    Entity owner;
    public Projectile(GamePanel gp)
    {
        super(gp);
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



        // Check for tile collision
        xCollisionOn = false;
        yCollisionOn = false;
        gp.cChecker.checkTile(this);

        if (owner instanceof Player)
        {
            // Check for OtherPlayer collision
            int entityIndex = gp.cChecker.checkEntity(this, Client.otherPlayers);
            if (entityIndex != -1)
            {
                collideWithEntity(Client.otherPlayers, entityIndex);
            }
        }
        else
        {
            // Check for Player collision
            gp.cChecker.checkPlayer(this);
        }

        if (!xCollisionOn) {
            worldX += velocityX;
            projectileDistanceX = Math.abs(projectileDistanceX) - Math.abs(velocityX);
        }
        if (!yCollisionOn) {
            worldY += velocityY;
            projectileDistanceY = Math.abs(projectileDistanceY) - Math.abs(velocityY);
        }

        if (projectileDistanceX <= 0 || projectileDistanceY <= 0 || xCollisionOn || yCollisionOn)
        {
            // call child-specific projectile detonation behavior
            detonateOnImpact();
        }

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

    private void collideWithEntity(ArrayList<? extends Entity> fromList, int entityIndex)
    {
        if (this instanceof OBJ_Fireball)
        {
            fromList.get(entityIndex).life -= 20;
        }
    }

    protected abstract void detonateOnImpact();

    @Override
    public void getImages(String imagePath)
    {
        up1 = setup2(imagePath + "Up1");
        up2 = setup2(imagePath + "Up2");
//        up3 = setup(imagePath + "Up3");
        down1 = setup2(imagePath + "Down1");
        down2 = setup2(imagePath + "Down2");
//        down3 = setup2(imagePath + "Down3");
        left1 = setup2(imagePath + "Left1");
        left2 = setup2(imagePath + "Left2");
//        left3 = setup2(imagePath + "Left3");
        right1 = setup2(imagePath + "Right1");
        right2 = setup2(imagePath + "Right2");
//        right3 = setup2(imagePath + "Right3");
        upLeft1 = setup2(imagePath + "UpLeft1");
        upLeft2 = setup2(imagePath + "UpLeft2");
//        upLeft3 = setup2(imagePath + "UpLeft3");
        upRight1 = setup2(imagePath + "UpRight1");
        upRight2 = setup2(imagePath + "UpRight2");
//        upRight3 = setup2(imagePath + "UpRight3");
        downLeft1 = setup2(imagePath + "DownLeft1");
        downLeft2 = setup2(imagePath + "DownLeft2");
//        downLeft3 = setup2(imagePath + "DownLeft3");
        downRight1 = setup2(imagePath + "DownRight1");
        downRight2 = setup2(imagePath + "DownRight2");
//        downRight3 = setup2(imagePath + "DownRight3");

        // SET ANGLE OF PROJECTILE
    }
}
