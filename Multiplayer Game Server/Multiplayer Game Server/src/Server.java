import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// TO DO:

// 3. Transmit gp.player.worldX and gp.player.worldY with each "moving:" delimited UDP packet so that players joining later have client positions synced correctly and de-syncs are handled (made Player.worldX and Player.worldY static, accounted for collisions accordingly)

/**
 * This class: Listens for clients who wish to connect and spawns new threads to handle each new client connection.
 */
public class Server
{

    // Creates a socket object to communicate with incoming clients. Represents a connection between the Server or ClientHandler and the client.
    // Each socket has an output stream to send data through the connection, and an input stream to read data
    private final ServerSocket serverSocketTCP;
    //private final ServerSocket serverSocketUDP;   // UDP
    private final static int tcpPort = 6682;
    //private final static int udpPort = 9999;  // UDP


    public Server(ServerSocket serverSocketTCP)
    {
        this.serverSocketTCP = serverSocketTCP;
        //this.serverSocketUDP = serverSocketUDP;   // UDP
    }

    public void startServer()
    {

        try
        {
            while (!serverSocketTCP.isClosed())
            {
                Socket serverSocketTCP = this.serverSocketTCP.accept(); // Suspends code here until a client connects. When a client connects, values are stored to a socket object, which can be used to communicate with that client
                //Socket serverSocketUDP = this.serverSocketUDP.accept();   // UDP
                System.out.println("A new client has connected!");  // TEST CODE
                TcpHandler tcpHandler = new TcpHandler(serverSocketTCP, tcpPort);  // Custom class; see ClientHandler for details

                Thread tcpThread = new Thread(tcpHandler);   // Spawn a new Thread to handle the client connection, pass ClientHandler object to it
                tcpThread.start();

            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
            closeServerSocket();
        }

    }

    /** Method: closeServerSocket
     *
     * This method handles IOExceptions caused by illegal serverSocket connections by closing the connection.
     */
    public void closeServerSocket()
    {

        try
        {
            if (serverSocketTCP != null)
            {
                serverSocketTCP.close();
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocketTCP = new ServerSocket(tcpPort);
        //ServerSocket serverSocketUDP = new ServerSocket(udpPort); // UDP
        Server server = new Server(serverSocketTCP);

        // UdpBranch
        UdpClientHandler udpClientHandler = new UdpClientHandler();
        Thread udpThread = new Thread(udpClientHandler);
        udpThread.start();

        server.startServer();
    }

}