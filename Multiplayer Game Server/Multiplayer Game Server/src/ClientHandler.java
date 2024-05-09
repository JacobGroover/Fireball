import java.net.InetAddress;
import java.util.ArrayList;

/**
 * This class: // Each object of this class will be responsible for communicating with a client from Server class.
 * Implements Runnable interface, indicating that each instance of this class will be executed on a separate thread;
 * Specifically, code in the overridden run() method will be executed on a separate thread.
 */
public class ClientHandler
{

    protected static ArrayList<ClientHandler> clientDataAL = new ArrayList<>();  // Static ArrayList of ClientHandler objects to keep track of all the instances created
    protected boolean firstDPReceived;
    protected InetAddress clientInetAddress;
    protected int port;
    protected String clientUsername;
    protected double worldX;
    protected double worldY;
    protected double velocityX;
    protected double velocityY;
    protected boolean joinedGame;

    protected int playPressed1;

    protected int mouseX;
    protected int mouseY;

    public ClientHandler()
    {
    }

    public ClientHandler(String clientUsername, double worldX, double worldY)
    {
        setClientUsername(clientUsername);
        setWorldX(worldX);
        setWorldY(worldY);
    }

    public boolean isFirstDPReceived()
    {
        return firstDPReceived;
    }

    public void setFirstDPReceived(boolean firstDPReceived)
    {
        this.firstDPReceived = firstDPReceived;
    }

    public InetAddress getClientInetAddress()
    {
        return clientInetAddress;
    }

    public void setClientInetAddress(InetAddress clientInetAddress)
    {
        this.clientInetAddress = clientInetAddress;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getClientUsername()
    {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername)
    {
        this.clientUsername = clientUsername;
    }

    public double getWorldX()
    {
        return worldX;
    }

    public void setWorldX(double worldX)
    {
        this.worldX = worldX;
    }

    public double getWorldY()
    {
        return worldY;
    }

    public void setWorldY(double worldY)
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
    public boolean getJoinedGame()
    {
        return joinedGame;
    }

    public void setJoinedGame(boolean joinedGame)
    {
        this.joinedGame = joinedGame;
    }

    public int getPlayPressed1()
    {
        return playPressed1;
    }

    public void setPlayPressed1(int playPressed1)
    {
        this.playPressed1 = playPressed1;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public void setMouseX(int mouseX)
    {
        this.mouseX = mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public void setMouseY(int mouseY)
    {
        this.mouseY = mouseY;
    }
}
