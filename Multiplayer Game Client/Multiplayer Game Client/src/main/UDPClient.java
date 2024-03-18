package main;

import entities.OtherPlayer;
import entities.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient extends Client implements Runnable
{

    protected DatagramSocket socket;

    public UDPClient(GamePanel gp)
    {
        try
        {
            socket = new DatagramSocket();

            // Send data to server
            // "entry:" String packet send instructions
            byte[] byteArray = ("entry:" + clientUsername + "entry:" + gp.player.worldX + "entry:" + gp.player.worldY).getBytes(); // new byte[1024];
            InetAddress address = InetAddress.getByName(Client.serverAddress);
            DatagramPacket dgPacket = new DatagramPacket(byteArray, byteArray.length, address, Client.serverUdpPort1);
            socket.send(dgPacket);

            // TEST CODE
                    /*for (OtherPlayer otherPlayer : otherPlayers)
                    {
                        System.out.println(otherPlayer.clientUserName);
                    }*/

            // update local client with data from server
            /*for (OtherPlayer otherPlayer : otherPlayers)
            {
                // receive data from server
                dgPacket = new DatagramPacket(byteArray, byteArray.length);
                socket.receive(dgPacket);
                String messageReceived = new String(dgPacket.getData());

                if (messageReceived.startsWith("entry:"))
                {
                    String[] usernameWorldXY = messageReceived.split("entry:");
                    String clientUsername = usernameWorldXY[0];
                    String worldX = usernameWorldXY[1];
                    String worldY = usernameWorldXY[2];
                    //System.out.println(messageReceived.substring(5));
                    if (messageReceived != null && clientUsername.equals(otherPlayer.clientUserName))
                    {
                        //System.out.println(messageReceived.substring(5) + " moves");
                        //System.out.println(otherPlayer.velocityX + " " + otherPlayer.velocityY);
                        otherPlayer.worldX = Double.parseDouble(worldX);
                        otherPlayer.worldY = Double.parseDouble(worldY);
                    }
                }

            }*/
            //socket.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

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

                try
                {
                    //DatagramSocket socket = new DatagramSocket();

                    // Send data to server
                    byte[] byteArray = ("moving:" + clientUsername + "moving:" + String.format("%.16f", Player.sendVelocityX) + "moving:" + String.format("%.16f", Player.sendVelocityY)).getBytes(); // new byte[1024];
                    InetAddress address = InetAddress.getByName(Client.serverAddress);
                    DatagramPacket dgPacket = new DatagramPacket(byteArray, byteArray.length, address, Client.serverUdpPort1);
                    socket.send(dgPacket);

                    //System.out.println(new String(dgPacket.getData()));

                    /*// receive data from server
                    byte[] byteArray2 = new byte[1024];
                    DatagramPacket dgPacket2 = new DatagramPacket(byteArray2, byteArray2.length);
                    socket.receive(dgPacket2);
                    String messageReceived = new String(dgPacket2.getData());
                    System.out.println(new String(dgPacket2.getData()));*/

                    //System.out.println("THIS" + messageReceived);

                    // TEST CODE
                    /*for (OtherPlayer otherPlayer : otherPlayers)
                    {
                        System.out.println(otherPlayer.clientUserName);
                    }*/

                    // "entry:" identifier packet receive instructions
                    /*if (messageReceived.startsWith("entry:"))
                    {
                        String[] usernameWorldXY = messageReceived.split("entry:");
                        String clientUsername = usernameWorldXY[1];
                        String worldX = usernameWorldXY[2];
                        String worldY = usernameWorldXY[3];
                        for (OtherPlayer otherPlayer : otherPlayers)
                        {
                            //System.out.println(messageReceived.substring(5));
                            if (clientUsername.equals(otherPlayer.clientUserName))
                            {
                                //System.out.println(messageReceived.substring(5) + " moves");
                                //System.out.println(otherPlayer.velocityX + " " + otherPlayer.velocityY);
                                otherPlayer.worldX = Double.parseDouble(worldX);
                                otherPlayer.worldX = Double.parseDouble(worldY);
                            }
                        }
                    }*/

                        // receive data from server
                        byte[] byteArray2 = new byte[1024];
                        DatagramPacket dgPacket2 = new DatagramPacket(byteArray2, byteArray2.length);
                        socket.receive(dgPacket2);
                        String messageReceived = new String(dgPacket2.getData());
                        System.out.println(new String(dgPacket2.getData()));


                        String[] usernameVelocityXY = messageReceived.split("moving:");

                        for (int i = 1; i < usernameVelocityXY.length; i += 3)
                        {
                            String clientUsername = usernameVelocityXY[i];
                            String velocityX = usernameVelocityXY[i + 1];
                            String velocityY = usernameVelocityXY[i + 2];

                            for (OtherPlayer otherPlayer : otherPlayers)
                            {


                                // update local client with data from server


                                //System.out.println(otherPlayer.clientUserName);
                                //System.out.println(clientUsername + " " + velocityX + " " + velocityY);
                                //System.out.println(otherPlayers.get(0));
                                if (clientUsername.equals(otherPlayer.clientUserName))
                                {
                                    otherPlayer.velocityX = Double.parseDouble(velocityX);
                                    otherPlayer.velocityY = Double.parseDouble(velocityY);
                                }
                            }
                        }


                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                    isWorking = false;
                }


                delta--;
            }
        }
        socket.close();

    }
}
