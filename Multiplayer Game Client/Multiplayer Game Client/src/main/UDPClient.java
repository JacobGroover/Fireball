package main;/*package main;

import entities.OtherPlayer;

import java.io.*;
import java.net.*;

public class UDPClient extends Client
{

private DatagramSocket datagramSocket;
private InetAddress inetAddress;
private int udpPort;

    public UDPClient(DatagramSocket datagramSocket, String clientUsername, String serverAddress, int udpPort)
    {
        this.datagramSocket = datagramSocket;
        this.clientUsername = clientUsername;
        this.udpPort = udpPort;
        try
        {
            this.inetAddress = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException uhe)
        {
            closeEverything(datagramSocket);
        }
    }

    public void sendPosition(GamePanel gp)
    {
        *//*new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            }
        });*//*

        int FPS = 60;
        double drawInterval = (double)1000000000/ FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        *//*long timer = 0;
        int drawCount = 0;*//*

        while (datagramSocket.isConnected())
        {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                //System.out.println("*" + Player.sendVelocity + username);
                double x = gp.player.worldX;
                double y = -gp.player.worldY;

                byte[] positionX = String.valueOf(x).getBytes();
                byte[] positionY = String.valueOf(y).getBytes();

                DatagramPacket datagramPacketSend1 = new DatagramPacket(positionX, positionX.length, this.inetAddress, this.udpPort);
                DatagramPacket datagramPacketSend2 = new DatagramPacket(positionY, positionY.length, this.inetAddress, this.udpPort);

                try
                {
                    datagramSocket.send(datagramPacketSend1);
                    datagramSocket.send(datagramPacketSend2);
                } catch (IOException ioe)
                {
                    closeEverything(datagramSocket);
                }

                delta--;
                //drawCount++;
            }
        }
    }

    // TRY: sending a TCP packet from server-side ClientHandler thread with clientUsername String, receive it in
    // this method and use that clientUsername to match new world coordinates to correct OtherPlayer object.
    public void listenForPosition()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                byte[] byteArray = new byte[1024];
                DatagramPacket datagramPacketReceive1 = new DatagramPacket(byteArray, byteArray.length);

                while (datagramSocket.isConnected())
                {
                    try
                    {
                        datagramSocket.receive(datagramPacketReceive1);
                    } catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }

                    String capture = new String(datagramPacketReceive1.getData());
                    double position = Double.parseDouble(capture);

                    for (OtherPlayer otherPlayer : otherPlayers)
                    {
                        if (position > 0)
                        {
                            otherPlayer.worldX = position;
                        } else
                        {
                            otherPlayer.worldY = -position;
                        }
                    }
                }
            }
        });

    }

    public void closeEverything(DatagramSocket datagramSocket)
    {
        datagramSocket.close(); // Closing the socket closes both the input and output streams
    }

}*/
