package main;

import entities.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = (int)entity.worldX + entity.solidArea.x;
        int entityRightWorldX = (int)entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = (int)entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = (int)entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;


        int tileNum1, tileNum2, tileNum3;

        switch (entity.direction) {
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

    public int checkObject(Entity entity, boolean isPlayer) {
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // Get entity's solid area position
                entity.solidArea.x = (int)entity.worldX + entity.solidArea.x;
                entity.solidArea.y = (int)entity.worldY + entity.solidArea.y;

                // Get object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                /*entity.solidArea.x += (int)entity.velocityX;
                entity.solidArea.y += (int)entity.velocityY;
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    if (gp.obj[i].collision) {
                        entity.xCollisionOn = true;
                        entity.yCollisionOn = true;
                    }
                }*/

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

}
