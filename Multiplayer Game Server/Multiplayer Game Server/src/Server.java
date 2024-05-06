import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// TO DO:



/**
 * This class: Listens for clients who wish to connect and spawns new threads to handle each new client connection.
 */
public class Server
{

    // Creates a socket object to communicate with incoming clients. Represents a connection between the Server or ClientHandler and the client.
    // Each socket has an output stream to send data through the connection, and an input stream to read data
    private final ServerSocket serverSocketTCP;
    private final static int tcpPort = 6682;


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
        Server server = new Server(serverSocketTCP);

        // UdpBranch
        UdpClientHandler udpClientHandler = new UdpClientHandler();

        udpClientHandler.receiveDP();
        udpClientHandler.sendDP();

        server.startServer();
    }

}