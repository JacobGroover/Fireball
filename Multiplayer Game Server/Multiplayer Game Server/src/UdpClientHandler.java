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
                        for (int i = 0; i < clientDataAL.size(); i++)
                        {
                            if (clientDataAL.get(i).getJoinedGame())
                            {
                                tempByteString += (String.format("moving:%s" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f" + "moving:%.16f",
                                        clientDataAL.get(i).getClientUsername(), clientDataAL.get(i).getVelocityX(), clientDataAL.get(i).getVelocityY(), clientDataAL.get(i).getWorldX(), clientDataAL.get(i).getWorldY()));
                            }
                        }

                        try
                        {
                            for (int i = 0; i < clientDataAL.size(); i++)
                            {
                                if (clientDataAL.get(i).getJoinedGame() && clientDataAL.get(i).isFirstDPReceived())
                                {
                                    byte [] byteArray = tempByteString.getBytes();

                                    // Send data back to the client at address and port
                                    InetAddress address = clientDataAL.get(i).getClientInetAddress();
                                    int port = clientDataAL.get(i).getPort();
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

                            for (int i = 0; i < clientDataAL.size(); i++)
                            {

                                if (clientDataAL.get(i).getClientUsername().equals(clientUsername))
                                {
                                    clientDataAL.get(i).setVelocityX(velocityX);
                                    clientDataAL.get(i).setVelocityY(velocityY);
                                    clientDataAL.get(i).setWorldX(worldX);
                                    clientDataAL.get(i).setWorldY(worldY);

                                    clientDataAL.get(i).setFirstDPReceived(true);
                                    clientDataAL.get(i).setClientInetAddress(dgPacket.getAddress());
                                    clientDataAL.get(i).setPort(dgPacket.getPort());

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
