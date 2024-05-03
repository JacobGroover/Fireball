import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UdpClientHandler extends ClientHandler implements Runnable //extends Thread
{
    //protected static ArrayList<ClientHandler> clientDataAL = new ArrayList<>();
    protected DatagramSocket socket;
    protected boolean isWorking;

    public UdpClientHandler() throws IOException
    {
        socket = new DatagramSocket(4445);
        isWorking = true;
    }

    @Override
    public void run()
    {

        while (isWorking)
        {

            try
            {
                byte[] byteArray = new byte[1024];

                // receive packet from a client
                DatagramPacket dgPacket = new DatagramPacket(byteArray, byteArray.length);
                socket.receive(dgPacket);

                // GATHER DATA FROM PACKET:
                String dgPacketString = new String(dgPacket.getData());

                if (dgPacketString.startsWith("entry:"))
                {
                    String[] usernameWorldXY = dgPacketString.split("entry:");
                    String clientUsername = usernameWorldXY[1];
                    String worldX = usernameWorldXY[2];
                    String worldY = usernameWorldXY[3];
                    //clientDataAL.add(new ClientHandler(clientUsername, worldX, worldY));
                        /*System.out.println(clientUsername);
                        System.out.println(worldX);
                        System.out.println(worldY);*/

                    // "entry:" String packet send instructions
                        /*for (ClientHandler client : clientDataAL)
                        {
                            byteArray = ("entry:" + client.getClientUsername() + "entry:" + client.getWorldX() + "entry:" + client.getWorldY()).getBytes();
                            // Send data back to the client at address and port
                            InetAddress address = dgPacket.getAddress();
                            int port = dgPacket.getPort();
                            dgPacket = new DatagramPacket(byteArray, byteArray.length, address, port);
                            socket.send(dgPacket);
                        }*/

                }
                else
                {
                    String[] usernameWorldXY = dgPacketString.split("moving:");
                    String clientUsername = usernameWorldXY[1];
                    double velocityX = Double.parseDouble(usernameWorldXY[2]);
                    double velocityY = Double.parseDouble(usernameWorldXY[3]);
                    double worldX = Double.parseDouble(usernameWorldXY[4]);
                    double worldY = Double.parseDouble(usernameWorldXY[5]);
                    boolean joinedGame = Boolean.parseBoolean(usernameWorldXY[6].substring(0, 4));
                    //System.out.println(usernameWorldXY[1] + " " + joinedGame);
                        /*System.out.println(clientUsername);
                        System.out.println(velocityX);
                        System.out.println(velocityY);*/

                    String tempByteString = "";
                    for (ClientHandler client : clientDataAL)
                    {

                        //System.out.println(client.getClientUsername());
                        if (client.getClientUsername().equals(clientUsername))
                        {
                            client.setVelocityX(velocityX);
                            client.setVelocityY(velocityY);
                            client.setWorldX(worldX);
                            client.setWorldY(worldY);
                            client.setJoinedGame(joinedGame);
                            //System.out.println(client.getJoinedGame());
                            //System.out.println(client.getClientUsername() + " " + client.getVelocityX() + " " + client.getVelocityY());
                            
                        }
                        tempByteString += (String.format("moving:%s" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f" + "moving:%b",   //  + "moving:%b"
                                client.getClientUsername(), client.getVelocityX(), client.getVelocityY(), client.getWorldX(), client.getWorldY(), client.getJoinedGame())); // , client.getJoinedGame()
                        //System.out.println(tempByteString);
                    }
                    //System.out.println(tempByteString);
                    byteArray = tempByteString.getBytes();

                    //System.out.println("moving:" + client.getClientUsername() + "moving:" + client.getVelocityX() + "moving:" + client.getVelocityY());

                    //byteArray = ("moving:" + client.getClientUsername() + "moving:" + client.getVelocityX() + "moving:" + client.getVelocityY()).getBytes();
                    // Send data back to the client at address and port
                    InetAddress address = dgPacket.getAddress();
                    int port = dgPacket.getPort();
                    //dgPacket = new DatagramPacket(byteArray, byteArray.length, address, port);
                    DatagramPacket responsePacket = new DatagramPacket(byteArray, byteArray.length, address, port);
                    //System.out.println(new String(dgPacket.getData()));
                    //socket.send(dgPacket);
                    socket.send(responsePacket);
                    //System.out.println(client.getClientUsername() + " " + client.getVelocityX() + " " + client.getVelocityY());
                }

                // store client ip address into ArrayList
                // store client port into ArrayList
                // store client username into ArrayList
                // store client worldX and worldY position into ArrayList
                // store client speed (and any other future character attributes) into ArrayList

            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                isWorking = false;
            }
        }
        socket.close();
    }

    /*
    1. Create abstract parent class for all ClientHandlers
        ** May be unnecessary, try completely decoupling TCP and UDP

    2. Create TcpClientHandler class (extends ClientHandler) to establish TCP connection between server and clients
        a) TcpClientHandler will receive and sync player connections and world position between clients
    3. Create a UdpClientHandler class for each UDP listening port
        a) Need a listening port for client movement velocity packets
        b) might need another to periodically sync player world position?
        c) Will probably need another later for player abilities so they can move while using abilities without hanging up the movement Thread
     */

}
