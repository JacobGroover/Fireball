import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class: // Each object of this class will be responsible for communicating with a client from Server class.
 * Implements Runnable interface, indicating that each instance of this class will be executed on a separate thread;
 * Specifically, code in the overridden run() method will be executed on a separate thread.
 */
public class ClientHandler
{

    protected static ArrayList<ClientHandler> clientDataAL = new ArrayList<>();  // Static ArrayList of ClientHandler objects to keep track of all the instances created
    protected InetAddress clientInetAddress;
    protected int port;
    protected String clientUsername;
    protected String worldX;
    protected String worldY;
    protected double velocityX;
    protected double velocityY;

    public ClientHandler()
    {

    }

    public ClientHandler(String clientUsername, String worldX, String worldY)
    {
        setClientUsername(clientUsername);
        setWorldX(worldX);
        setWorldY(worldY);
    }

    public InetAddress getClientInetAddress()
    {
        return clientInetAddress;
    }

    public void setClientInetAddress(InetAddress clientInetAddress)
    {
        this.clientInetAddress = clientInetAddress;
    }

    public String getClientUsername()
    {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername)
    {
        this.clientUsername = clientUsername;
    }

    public String getWorldX()
    {
        return worldX;
    }

    public void setWorldX(String worldX)
    {
        this.worldX = worldX;
    }

    public String getWorldY()
    {
        return worldY;
    }

    public void setWorldY(String worldY)
    {
        this.worldY = worldY;
    }

    public double getVelocityX()
    {
        return velocityX;
    }

    public void setVelocityX(double velocityX)
    {
        this.velocityX = velocityX;
    }

    public double getVelocityY()
    {
        return velocityY;
    }

    public void setVelocityY(double velocityY)
    {
        this.velocityY = velocityY;
    }
}
