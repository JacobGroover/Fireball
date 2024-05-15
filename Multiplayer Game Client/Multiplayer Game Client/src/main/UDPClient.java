package main;

import entities.OtherPlayer;
import entities.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient extends Client
{

    protected DatagramSocket socket;
    GamePanel gp;

    public UDPClient(GamePanel gp)
    {
        try
        {
            socket = new DatagramSocket();
            this.gp = gp;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    protected void sendDP()
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

                    if (gp.joinedGame)
                    {
                        currentTime = System.nanoTime();
                        delta += (currentTime - lastTime) / drawInterval;
                        lastTime = currentTime;
                        if (delta >= 1)
                        {
                            try
                            {
                                // Send data to server
                                byte[] byteArray = ("moving:" + clientUsername + "moving:" + String.format("%.16f", Player.sendVelocityX) + "moving:" + String.format("%.16f", Player.sendVelocityY) +
                                        "moving:" + String.format("%.16f", Player.worldX) + "moving:" + String.format("%.16f", Player.worldY)).getBytes();

                                InetAddress address = InetAddress.getByName(Client.serverAddress);
                                DatagramPacket dgPacket = new DatagramPacket(byteArray, byteArray.length, address, Client.serverUdpPort1);

                                socket.send(dgPacket);

                            } catch (IOException ioe)
                            {
                                ioe.printStackTrace();
                                isWorking = false;
                            }

                            delta = 0;

                        }
                    }
                }
                socket.close();

            }
        });
        t1.start();
    }

    protected void receiveDP()
    {
        Thread t2 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                boolean isWorking = true;
                while (isWorking)
                {
                    if (gp.joinedGame)
                    {

                        String messageReceived = "";
                        try
                        {
                            // receive data from server
                            byte[] byteArray2 = new byte[1024];
                            DatagramPacket dgPacket2 = new DatagramPacket(byteArray2, byteArray2.length);

                            socket.receive(dgPacket2);

                            messageReceived = new String(dgPacket2.getData());
                        } catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                            isWorking = false;
                        }


                        String[] usernameVelocityXY = messageReceived.split("moving:");

                        for (int i = 1; i < usernameVelocityXY.length; i += 5)
                        {
                            String clientUsername = usernameVelocityXY[i];
                            String velocityX = usernameVelocityXY[i + 1];
                            String velocityY = usernameVelocityXY[i + 2];
                            String worldX = usernameVelocityXY[i + 3];
                            String worldY = usernameVelocityXY[i + 4];

                            for (int j = 0; j < otherPlayers.size(); j++)
                            {

                                // update local client with data from server
                                if (clientUsername.equals(otherPlayers.get(j).clientUserName))
                                {
                                    otherPlayers.get(j).velocityX = Double.parseDouble(velocityX);
                                    otherPlayers.get(j).velocityY = Double.parseDouble(velocityY);
                                    otherPlayers.get(j).worldX = Double.parseDouble(worldX);
                                    otherPlayers.get(j).worldY = Double.parseDouble(worldY);
                                }
                            }

                        }
                    }

                }
                socket.close();

            }
        });
        t2.start();
    }

}
