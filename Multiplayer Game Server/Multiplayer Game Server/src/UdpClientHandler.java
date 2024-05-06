import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientHandler extends ClientHandler
{
    //protected static ArrayList<ClientHandler> clientDataAL = new ArrayList<>();
    protected DatagramSocket socket;

    public UdpClientHandler() throws IOException
    {
        socket = new DatagramSocket(4445);
    }

    public void sendDP()
    {
        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                int FPS = 60;
                double drawInterval = (double) 1000000000 / FPS;
                double delta = 0;
                long lastTime = System.nanoTime();
                long currentTime;

                boolean isWorking = true;
                while (isWorking)
                {
                    currentTime = System.nanoTime();
                    delta += (currentTime - lastTime) / drawInterval;
                    lastTime = currentTime;

                    if (delta >= 1)
                    {
                        String tempByteString = "";
                        for (ClientHandler client : clientDataAL)
                        {
                            if (client.getJoinedGame())
                            {
                                tempByteString += (String.format("moving:%s" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f",
                                        client.getClientUsername(), client.getVelocityX(), client.getVelocityY(), client.getWorldX(), client.getWorldY()));
                            }
                        }

                        try
                        {
                            for (ClientHandler client : clientDataAL)
                            {
                                if (client.getJoinedGame() && client.isFirstDPReceived())
                                {
                                    byte [] byteArray = tempByteString.getBytes();

                                    // Send data back to the client at address and port
                                    InetAddress address = client.getClientInetAddress();
                                    int port = client.getPort();
                                    DatagramPacket responsePacket = new DatagramPacket(byteArray, byteArray.length, address, port);
                                    socket.send(responsePacket);
                                }
                            }

                        }
                        catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                            isWorking = false;
                        }
                        delta = 0;
                    }
                }
                socket.close();

            }
        });
        t1.start();
    }

    public void receiveDP()
    {
        Thread t2 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                boolean isWorking = true;
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

                            String[] usernameWorldXY = dgPacketString.split("moving:");
                            String clientUsername = usernameWorldXY[1];
                            double velocityX = Double.parseDouble(usernameWorldXY[2]);
                            double velocityY = Double.parseDouble(usernameWorldXY[3]);
                            double worldX = Double.parseDouble(usernameWorldXY[4]);
                            double worldY = Double.parseDouble(usernameWorldXY[5]);

                            for (ClientHandler client : clientDataAL)
                            {

                                if (client.getClientUsername().equals(clientUsername))
                                {
                                    client.setVelocityX(velocityX);
                                    client.setVelocityY(velocityY);
                                    client.setWorldX(worldX);
                                    client.setWorldY(worldY);

                                    client.setFirstDPReceived(true);
                                    client.setClientInetAddress(dgPacket.getAddress());
                                    client.setPort(dgPacket.getPort());

                                }
                            }

                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                        isWorking = false;
                    }
                }
                socket.close();

            }
        });
        t2.start();
    }

}
