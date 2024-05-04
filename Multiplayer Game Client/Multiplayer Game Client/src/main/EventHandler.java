package main;

import entities.Player;

import java.awt.*;

public class EventHandler
{
    GamePanel gp;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTriggerEvent = true;

    public EventHandler(GamePanel gp)
    {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow)
        {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            col++;
            if (col == gp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }

    }

    public void checkEvent()
    {
        // Check if Player is more than 1 tile away from the last triggered event
        int distanceX = Math.abs((int)Player.worldX - previousEventX);
        int distanceY = Math.abs((int)Player.worldY - previousEventY);
        float distanceXY = (float) Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
//        int distanceXY = Math.max(distanceX, distanceY);
        if (distanceXY > gp.tileSize)
        {
            canTriggerEvent = true;     // if so, the event can be triggered by player again
        }

        if (canTriggerEvent)
        {
//            if (trigger(15, 36, "any")) {damagePit(15, 36);}
        }
    }

    private void damagePit(int col, int row)
    {
        gp.player.life -= 10;
//        eventRect[col][row].eventTriggered = true;
        canTriggerEvent = false;
    }

    public boolean trigger(int col, int row, String reqDirection)
    {
        boolean trigger = false;

        // Get Player current solidArea position
        gp.player.solidArea.x = (int)Player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = (int)Player.worldY + gp.player.solidArea.y;

        // Get eventRect current solidArea position
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventTriggered)
        {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any"))
            {
                trigger = true;

                previousEventX = (int)Player.worldX;
                previousEventY = (int)Player.worldY;
            }
        }

        // Reset solid area x and y coordinates after checking event collision
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return trigger;
    }

}
