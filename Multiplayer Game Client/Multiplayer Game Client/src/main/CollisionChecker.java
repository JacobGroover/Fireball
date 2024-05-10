package main;

import entities.Entity;
import entities.OtherPlayer;
import entities.Player;
import entities.Projectile;

import java.util.ArrayList;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity)
    {
        double worldX = Player.worldX;
        double worldY = Player.worldY;
        if (!(entity instanceof Player))
        {
            worldX = entity.worldX;
            worldY = entity.worldY;
        }
        int entityLeftWorldX = (int)worldX + entity.solidArea.x;
        int entityRightWorldX = (int)worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = (int)worldY + entity.solidArea.y;
        int entityBottomWorldY = (int)worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;


        int tileNum1, tileNum2, tileNum3;

        boolean continueCheck = true;
        if ( entity instanceof Projectile && ((Projectile) entity).detonating)
        {
            continueCheck = false;
        }

        if (continueCheck)
        {
            switch (entity.direction)
            {
                case "up":
                    entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                    }
                    break;
                case "down":
                    entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                    }
                    break;
                case "left":
                    entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    break;
                case "right":
                    entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    break;
                case "upLeft":
                    entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                    entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum3 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    if (gp.tileManager.tile[tileNum1].collision) {
                        entity.yCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum3].collision &&
                            !gp.tileManager.tile[tileNum1].collision && !gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                        entity.xCollisionOn = true;
                    }
                    break;
                case "upRight":
                    entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                    entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    tileNum3 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    if (gp.tileManager.tile[tileNum1].collision) {
                        entity.yCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum3].collision &&
                            !gp.tileManager.tile[tileNum1].collision && !gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                        entity.xCollisionOn = true;
                    }
                    break;
                case "downLeft":
                    entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                    entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum3 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNum1].collision) {
                        entity.yCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum3].collision &&
                            !gp.tileManager.tile[tileNum1].collision && !gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                        entity.xCollisionOn = true;
                    }
                    break;
                case "downRight":
                    entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                    entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                    tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    tileNum3 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNum1].collision) {
                        entity.yCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum2].collision) {
                        entity.xCollisionOn = true;
                    }
                    if (gp.tileManager.tile[tileNum3].collision &&
                            !gp.tileManager.tile[tileNum1].collision && !gp.tileManager.tile[tileNum2].collision) {
                        entity.yCollisionOn = true;
                        entity.xCollisionOn = true;
                    }
                    break;
            }
        }

    }

    public int checkObject(Entity entity, boolean isPlayer)
    {

        double worldX = Player.worldX;
        double worldY = Player.worldY;
        if (!(entity instanceof Player))
        {
            worldX = entity.worldX;
            worldY = entity.worldY;
        }

        int index = -1;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // Get entity's solid area position
                entity.solidArea.x = (int)worldX + entity.solidArea.x;
                entity.solidArea.y = (int)worldY + entity.solidArea.y;

                // Get object's solid area position
                gp.obj[i].solidArea.x = (int)gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = (int)gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "upRight":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "upLeft":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "downRight":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                    case "downLeft":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                        break;
                }
            }
            if (gp.obj[i] != null) {
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x =gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y =gp.obj[i].solidAreaDefaultY;
            }

        }
        return index;
    }

    // Check Entity collision
    public int checkEntities(Entity entity, ArrayList<? extends Entity> target)
    {
        double worldX = Player.worldX;
        double worldY = Player.worldY;
        if (!(entity instanceof Player))
        {
            worldX = entity.worldX;
            worldY = entity.worldY;
        }

        int index = -1;

        for (int i = 0; i < target.size(); i++)
        {
            boolean continueCheck = true;

            if (entity instanceof Projectile && target.get(i) instanceof Projectile)                // If both are projectiles...
            {
                if (!((Projectile) entity).detonating && !((Projectile) target.get(i)).detonating)  // If neither are detonated...
                {
                    if (entity == target.get(i))
                    {
                        continueCheck = false;
                    }
                }
                if (!((Projectile) entity).detonating && ((Projectile) target.get(i)).detonating)   // If this projectile is NOT detonated and that projectile IS detonated
                {
                    continueCheck = false;
                }
                if (((Projectile) entity).detonating && ((Projectile) target.get(i)).detonating)
                {
                    continueCheck = false;
                }

                if (((Projectile) entity).detonating && !((Projectile) target.get(i)).detonating)
                {
                    continueCheck = false;
                }

            }

            if (target.get(i) instanceof OtherPlayer && !((OtherPlayer) target.get(i)).joinedGame)
            {
                continueCheck = false;
            }
            // If target is the owner of the projectile, and the projectile is not detonating, then check collision (implication logic)
            // equivalent implication logic: (Client.otherPlayers.get(entityIndex) == this.owner && detonating) || (Client.otherPlayers.get(entityIndex) != this.owner && detonating) || (Client.otherPlayers.get(entityIndex) != this.owner && !detonating)
            if (entity instanceof Projectile && (((Projectile) entity).owner == target.get(i) && !((Projectile) entity).detonating))
            {
                continueCheck = false;
            }
//            if (target.get(i) instanceof Projectile && entity instanceof Projectile && ((Projectile) entity).owner == ((Projectile) target.get(i)).owner)
//            if (entity instanceof Projectile && target.get(i) instanceof Projectile)                // If both are projectiles...
//            {
//                if (((Projectile) entity).detonating)                                               // ... and this projectile is detonating...
//                {
//                    continueCheck = false;                                                          // ... then no collision.
//                }
//                else if (entity != target.get(i))                                                   // Otherwise, if this projectile is NOT the projectile being compared...
//                {
//                    if (((Projectile) target.get(i)).detonating)                                    // ... then if the projectile being compared is detonating...
//                    {
//                        if (i == target.size() - 1)                                                 // ... and if the projectile being compared is at the end of the list...
//                        {
//                            continueCheck = false;                                                  // ... then no collision ...
//                        }
//                        else                                                                        // ... otherwise...
//                        {
//                            i++;                                                                    // ... move to the next projectile to compare...
//                        }
//                    }
//                }
//                else                                                                                // However, if this projectile IS the projectile being compared...
//                {
//                    if (i == target.size() - 1)                                                     // ... and if this projectile is at the end of the list...
//                    {
//                        continueCheck = false;                                                      // ... then no collision...
//                    }
//                    else                                                                            // ... otherwise...
//                    {
//                        i++;                                                                        // ... move to the next projectile to compare.
//                    }
//
//                }
//            }

            if (continueCheck)
            {
                if (target.get(i) != null) {
                    // Get entity's solid area position
                    entity.solidArea.x = (int)worldX + entity.solidArea.x;
                    entity.solidArea.y = (int)worldY + entity.solidArea.y;

                    // Get object's solid area position
                    target.get(i).solidArea.x = (int)target.get(i).worldX + target.get(i).solidArea.x;
                    target.get(i).solidArea.y = (int)target.get(i).worldY + target.get(i).solidArea.y;

                    switch (entity.direction) {
                        case "up":
                            entity.solidArea.y -= entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "down":
                            entity.solidArea.y += entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "upRight":
                            entity.solidArea.x += entity.speed;
                            entity.solidArea.y -= entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "upLeft":
                            entity.solidArea.x -= entity.speed;
                            entity.solidArea.y -= entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "downRight":
                            entity.solidArea.x += entity.speed;
                            entity.solidArea.y += entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                        case "downLeft":
                            entity.solidArea.x -= entity.speed;
                            entity.solidArea.y += entity.speed;
                            if (entity.solidArea.intersects(target.get(i).solidArea))
                            {
                                entity.xCollisionOn = true;
                                entity.yCollisionOn = true;
                                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
                                {
//                                    target.get(i).xCollisionOn = true;
//                                    target.get(i).yCollisionOn = true;
                                    ((Projectile) target.get(i)).detonating = true;
                                }
                                index = i;
                            }
                            break;
                    }
                }
            }

            if (target.get(i) != null)
            {
//                if (entity instanceof Projectile && target.get(i) instanceof Projectile)
//                {
//                    entity.xCollisionOn = false;
//                    entity.yCollisionOn = false;
//
//                    target.get(i).xCollisionOn = false;
//                    target.get(i).yCollisionOn = false;
//                }
                if (entity instanceof Projectile && index != -1 && !((Projectile) entity).detonating && ((Projectile) entity).owner != target.get(i))
                {
                    entity.worldX = target.get(i).worldX;
                    entity.worldY = target.get(i).worldY;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }

        }
        return index;
    }

    public boolean checkEntity(Entity entity, Entity target)
    {
        double entityWorldX = entity.worldX;
        double entityWorldY = entity.worldY;
        if (entity instanceof Player)
        {
            entityWorldX = Player.worldX;
            entityWorldY = Player.worldY;
        }

        double targetWorldX = target.worldX;
        double targetWorldY = target.worldY;
        if (target instanceof Player)
        {
            targetWorldX = Player.worldX;
            targetWorldY = Player.worldY;
        }

        boolean continueCheck = true;
        if (target instanceof OtherPlayer && !((OtherPlayer) target).joinedGame)
        {
            continueCheck = false;
        }
        // If target is the owner of the projectile, and the projectile is not detonating, then check collision (implication logic)
        // equivalent implication logic: (Client.otherPlayers.get(entityIndex) == this.owner && detonating) || (Client.otherPlayers.get(entityIndex) != this.owner && detonating) || (Client.otherPlayers.get(entityIndex) != this.owner && !detonating)
        if ( entity instanceof Projectile && (((Projectile) entity).owner == target && !((Projectile) entity).detonating))
        {
            continueCheck = false;
        }

        boolean collideWithThis = false;
        if (continueCheck)
        {
            // Get this entity's solid area position
            entity.solidArea.x = (int)entityWorldX + entity.solidArea.x;
            entity.solidArea.y = (int)entityWorldY + entity.solidArea.y;

            // Get other entity's solid area position
            target.solidArea.x = (int)targetWorldX + target.solidArea.x;
            target.solidArea.y = (int)targetWorldY + target.solidArea.y;

            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "upRight":
                    entity.solidArea.x += entity.speed;
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "upLeft":
                    entity.solidArea.x -= entity.speed;
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "downRight":
                    entity.solidArea.x += entity.speed;
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
                case "downLeft":
                    entity.solidArea.x -= entity.speed;
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(target.solidArea))
                    {
                        entity.xCollisionOn = true;
                        entity.yCollisionOn = true;
                        collideWithThis = true;
                    }
                    break;
            }
        }

        if (entity instanceof Projectile && collideWithThis && !((Projectile) entity).detonating && ((Projectile) entity).owner != target)
        {
            entity.worldX = target.worldX;
            entity.worldY = target.worldY;
            if (target instanceof Player)
            {
                entity.worldX = Player.worldX;
                entity.worldY = Player.worldY;
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;

        return collideWithThis;
    }

}
