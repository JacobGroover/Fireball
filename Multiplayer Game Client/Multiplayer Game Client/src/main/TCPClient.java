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

    public void sendJoinedGame(GamePanel gp)
    {
        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    boolean changedGameState = gp.joinedGame;
                    while (socket.isConnected())
                    {

                        if (changedGameState != gp.joinedGame)
                        {
                            bufferedWriter.write("*" + clientUsername + "*" + gp.joinedGame);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            changedGameState = gp.joinedGame;
                        }

                    }
                } catch (IOException ioe)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        });
        t1.start();

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

                            // TCP MOVEMENT
                            bufferedWriter.write("*" + Player.sendVelocity + clientUsername);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();


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
        Thread t2 = new Thread(new Runnable()
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

                        if (messageReceived != null && messageReceived.startsWith("*"))
                        {

                            String[] tokens = messageReceived.split("\\*");

                            if (Boolean.parseBoolean(tokens[2].substring(0, 4)))
                            {
                                boolean duplicate = false;
                                for (int i = 0; i < otherPlayers.size(); i++)
                                {
                                    if (otherPlayers.get(i).clientUserName.equals(tokens[1]))
                                    {
                                        duplicate = true;
                                    }
                                }
                                if (!duplicate)
                                {
                                    otherPlayers.add(new OtherPlayer(gp, tokens[1]));
                                }
                            }
                            else
                            {
                                if (tokens[1].equals(Client.clientUsername))
                                {
                                    otherPlayers.clear();
                                }
                                else
                                {
                                    for (int i = 0; i < otherPlayers.size(); i++)
                                    {
                                        if (otherPlayers.get(i).clientUserName.equals(tokens[1]))
                                        {
                                            otherPlayers.remove(i);
                                            i--;
                                        }
                                    }
                                }
                            }

                        }
                        else
                        {
//                            System.out.println("ELSE REACHED " + messageReceived);
                        }

                        byte[] b1 = new byte[1024];

                    } catch (IOException ioe)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        });
        t2.start();

    }

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
