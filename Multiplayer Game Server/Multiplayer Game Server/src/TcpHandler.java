import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class: // Each object of this class will be responsible for communicating with a client from Server class.
 * Implements Runnable interface, indicating that each instance of this class will be executed on a separate thread;
 * Specifically, code in the overridden run() method will be executed on a separate thread.
 */
public class TcpHandler implements Runnable
{

    public static ArrayList<TcpHandler> tcpHandlers = new ArrayList<>();  // Static ArrayList of ClientHandler objects to keep track of all the instances created
    private Socket TCPSocket;  // Socket object passed through constructor from Server class. Used to establish a connection between client and server.
    //private DatagramSocket datagramSocket;  // UDP
    private BufferedReader bufferedReader;  // Reads messages sent from the client
    private BufferedWriter bufferedWriter;  // Sends messages to the client (either from other clients or from the server)
    //private DatagramPacket datagramPacketReceive;   // UDP
    //private DatagramPacket datagramPacketSend;  // UDP
    InetAddress clientInetAddress;
    //private int tcpPort;   // UDP
    //private int udpPort;    // UDP
    private String clientUsername;

    public TcpHandler(Socket serverSocketTCP, int tcpPort)
    {

        try
        {
            this.TCPSocket = serverSocketTCP;
            // digital media classroom 10.255.11.27
            // intermediate programming classroom 10.255.4.72
            // Home IP 192.168.2.102
            // GGC Database class IP 10.255.36.219
            this.clientInetAddress = InetAddress.getByName("192.168.2.102");  // UDP
            //this.clientInetAddress = InetAddress.getByName("10.255.5.7");   // GGC IP
            //this.tcpPort = tcpPort;   // UDP
            //this.udpPort = udpPort; // UDP
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocketTCP.getOutputStream()));   // OutputStreamWriter is for character streams, OutputStream is for bytes. Therefore, wrap the byte stream in a character stream. Buffering the stream increases efficiency.
            this.bufferedReader = new BufferedReader(new InputStreamReader(serverSocketTCP.getInputStream()));   // BufferedWriter stream is for sending, BufferedReader for receiving.

            //this.datagramSocket = new DatagramSocket(this.udpPort); // UDP receives data
            //byte[] byteArrayReceive = new byte[1024];  // UDP
            //datagramPacketReceive = new DatagramPacket(byteArrayReceive, byteArrayReceive.length);  // UDP sends data
            //datagramSocket.receive(datagramPacketReceive); // UDP

            this.clientUsername = bufferedReader.readLine();
            tcpHandlers.add(this);

            for (TcpHandler tcpHandler : tcpHandlers)
            {
                tcpHandler.broadcastMessage(tcpHandler.clientUsername);
            }

            /*for (ClientHandler clientHandler : clientHandlers)
            {
                try
                {
                    System.out.println(clientHandler.clientUserName);

                    if (!clientHandler.clientUserName.equals(this.clientUserName))
                    {
                        clientHandler.bufferedWriter.write(this.clientUserName);    // this.clientUserName
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.
                    }
                } catch (IOException ioe)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }*/

            broadcastMessage("Server: " + clientUsername + " has entered the chat!");
        } catch (IOException ioe)
        {
            closeEverything(serverSocketTCP, bufferedReader, bufferedWriter);
        }

    }

    @Override
    public void run()
    {
        String messageFromClient;

        while (TCPSocket.isConnected())
        {

            try
            {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

                /*positionFromClient = bufferedReader.readLine();
                broadcastPosition(positionFromClient);*/
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                break;
            }
            //broadcastPosition();  // UDP
        }
    }

    public void broadcastMessage(String messageToSend)
    {
        for (int i = 0; i < tcpHandlers.size(); i++)
        {
            try
            {
                if (!tcpHandlers.get(i).clientUsername.equals(this.clientUsername))
                {
                    tcpHandlers.get(i).bufferedWriter.write(messageToSend);
                    tcpHandlers.get(i).bufferedWriter.newLine();
                    tcpHandlers.get(i).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                }
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }
        }
        /*for (ClientHandler clientHandler : clientHandlers)
        {
            try
            {
                if (!clientHandler.clientUsername.equals(this.clientUsername))
                {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                }
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }
        }*/
    }

    // UDP method
    /*public void broadcastPosition()
    {
        String positionFromClient;
        String positionToClient;
        positionFromClient = new String(datagramPacketReceive.getData());

        for (ClientHandler clientHandler : clientHandlers)
        {
            try
            {
                if (!clientHandler.clientUsername.equals(this.clientUsername))
                {
                    positionToClient = "*" + this.clientUsername + positionFromClient;
                    byte[] byteArraySend = (positionToClient).getBytes();
                    datagramPacketSend = new DatagramPacket(byteArraySend, byteArraySend.length, clientInetAddress, this.udpPort);
                    datagramSocket.send(datagramPacketSend);
                }
            } catch (IOException ioe)
            {
                closeEverythingUDP(datagramSocket);
            }
        }

    }*/

    public void removeClientHandler()
    {
        tcpHandlers.remove(this);
        broadcastMessage("Server: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        removeClientHandler();
        try
        {
            if (bufferedReader != null)
            {
                bufferedReader.close(); // Only need to close the outer buffered wrappers to close all the wrapped streams
            }
            if (bufferedWriter != null)
            {
                bufferedWriter.close(); // Only need to close the outer buffered wrappers to close all the wrapped streams
            }
            if (socket != null)
            {
                socket.close(); // Closing the socket closes both the input and output streams
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    // UDP Method
    /*public void closeEverythingUDP(DatagramSocket datagramSocket)
    {
        removeClientHandler();
        if (datagramSocket != null)
        {
            datagramSocket.close();
        }
    }*/

}
