import javax.swing.*;
import java.awt.*;
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
    protected static JFrame serverWindow = new JFrame();
    protected static int clientCount = 0;
    protected static JLabel clientLabel = new JLabel("Clients Connected: " + clientCount);



    public Server(ServerSocket serverSocketTCP)
    {
        this.serverSocketTCP = serverSocketTCP;
    }

    public void startServer()
    {

        try
        {
            while (!serverSocketTCP.isClosed())
            {
                Socket serverSocketTCP = this.serverSocketTCP.accept(); // Suspends code here until a client connects. When a client connects, values are stored to a socket object, which can be used to communicate with that client
                clientCount++;
                clientLabel.setText("Clients Connected: " + clientCount);
                TcpHandler tcpHandler = new TcpHandler(serverSocketTCP);  // Custom class; see ClientHandler for details

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

        serverWindow.setSize(500, 300);
        serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverWindow.setResizable(false);
        serverWindow.setTitle("Fireball Server");
//        serverWindow.setLayout(new BoxLayout(serverWindow.getContentPane(), BoxLayout.Y_AXIS));
        serverWindow.setLocationRelativeTo(null);

//        JLabel clientCount = new JLabel("Clients Connected: " + ClientHandler.clientDataAL.size());
        clientLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        clientLabel.setMaximumSize(new Dimension(600, 50));
        clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clientLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        serverWindow.add(clientLabel);
        serverWindow.setVisible(true);

        ServerSocket serverSocketTCP = new ServerSocket(tcpPort);
        Server server = new Server(serverSocketTCP);

        // UdpBranch
        UdpClientHandler udpClientHandler = new UdpClientHandler();

        udpClientHandler.receiveDP();
        udpClientHandler.sendDP();

        server.startServer();
    }

}