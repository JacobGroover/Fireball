package main;

import entities.OtherPlayer;
import entities.Player;

import java.io.*;
import java.net.Socket;

public class TCPClient extends Client
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public TCPClient(Socket socket, String clientUsername)
    {
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Client.clientUsername = clientUsername;

            bufferedWriter.write(clientUsername);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException ioe)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendPosition()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    bufferedWriter.write(clientUsername);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();


                    int FPS = 60;
                    double drawInterval = (double)1000000000/ FPS;
                    double delta = 0;
                    long lastTime = System.nanoTime();
                    long currentTime;
                    //long timer = 0;
                    //int drawCount = 0;

                    while (socket.isConnected())
                    {

                        currentTime = System.nanoTime();

                        delta += (currentTime - lastTime) / drawInterval;

                        lastTime = currentTime;

                        if (delta >= 1) {
                            //System.out.println("*" + Player.sendVelocity + username);

                            // TCP MOVEMENT
                            bufferedWriter.write("*" + Player.sendVelocity + clientUsername);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            // UDP MOVEMENT
                    /*DatagramSocket datagramSocket = new DatagramSocket();
                    byte[] b = ("*" + Player.sendVelocity + clientUsername).getBytes();
                    InetAddress ia = InetAddress.getByName("192.168.2.102");
                    DatagramPacket datagramPacketSend = new DatagramPacket(b, b.length, ia, 1234);
                    datagramSocket.send(datagramPacketSend);*/


                            delta--;
                            //drawCount++;
                        }
                    }
                } catch (IOException ioe)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        });

    }

    public void listenForMessage(GamePanel gp)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String messageReceived;

                while (socket.isConnected())
                {
                    try
                    {
                        messageReceived = bufferedReader.readLine();
                        //System.out.println(messageReceived);
                        if (messageReceived != null && !messageReceived.startsWith("*") && !messageReceived.contains(":"))
                        {
                            boolean isDuplicate = false;
                            for (OtherPlayer otherPlayer : otherPlayers)
                            {
                                if (otherPlayer.clientUserName.equals(messageReceived))
                                {
                                    isDuplicate = true;
                                    break;
                                }
                            }

                            if (!(messageReceived.equals(clientUsername)) && !isDuplicate)    // AND if otherPlayers ArrayList does not include a copy of messageReceived
                            {
                                //System.out.println(messageReceived);
                                otherPlayers.add(new OtherPlayer(gp, messageReceived));
                                System.out.println("adding " + messageReceived);
                            }

                            // TEST CODE
                            /*for (OtherPlayer otherPlayer : otherPlayers)
                            {
                                System.out.println(otherPlayer.clientUserName);
                            }*/

                        }
                        else if (messageReceived != null && messageReceived.contains(":") && !messageReceived.startsWith("*"))
                        {
                            if (messageReceived.endsWith(" has left the chat!"))
                            {
                                for (int i = 0; i < otherPlayers.size(); i++)
                                {
                                    if (messageReceived.substring(8).startsWith(otherPlayers.get(i).clientUserName))
                                    {
                                        otherPlayers.remove(i);
                                        break;
                                    }
                                    //i--;
                                }
                            }
                            System.out.println(messageReceived);
                        }
                        else
                        {
                            System.out.println(messageReceived);
                        }

                        // commented out for UDP
                        /*else
                        {
                            for (OtherPlayer otherPlayer : otherPlayers)
                            {
                                if (messageReceived != null && messageReceived.substring(5).equals(otherPlayer.clientUserName))
                                {
                                    //System.out.println(messageReceived.substring(5) + " moves");
                                    //System.out.println(otherPlayer.velocityX + " " + otherPlayer.velocityY);
                                    otherPlayer.velocityX = Double.parseDouble(messageReceived.substring(1, 3));
                                    otherPlayer.velocityY = Double.parseDouble(messageReceived.substring(3, 5));
                                }
                            }
                        }*/

                        byte[] b1 = new byte[1024];

                    } catch (IOException ioe)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    /*public void listenForPosition()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String messageFromGroupChat;

                while (socket.isConnected())
                {
                    try
                    {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    } catch (IOException ioe)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }*/

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
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

}
