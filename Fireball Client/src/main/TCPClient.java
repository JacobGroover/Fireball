package main;

import entities.OtherPlayer;
import entities.Player;

import java.io.*;
import java.net.Socket;

public class TCPClient extends Client
{
    public Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public TCPClient(Socket socket)
    {
        this.socket = socket;
        try
        {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ioe)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendInfo(GamePanel gp)
    {
//        Thread t1 = Thread.startVirtualThread(new Runnable()
        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int FPS = 2;
                double drawInterval = (double) 1000000000 / FPS;
                double delta = 0;
                long lastTime = System.nanoTime();
                long currentTime;

                boolean changedGameState = gp.joinedGame;
                while (socket.isConnected())
                {

                    currentTime = System.nanoTime();
                    delta += (currentTime - lastTime) / drawInterval;
                    lastTime = currentTime;

                    if (changedGameState != gp.joinedGame)
                    {
                        if (gp.gameState != gp.GAME_OVER_STATE)
                        {
                            try
                            {
                                bufferedWriter.write("*" + clientUsername + "*" + gp.joinedGame);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                changedGameState = gp.joinedGame;
                            } catch (IOException ioe)
                            {
                                closeEverything(socket, bufferedReader, bufferedWriter);
                            }
                        }
                        else
                        {
                            try
                            {
                                bufferedWriter.write("*" + clientUsername + "*" + gp.joinedGame + "*" + Player.respawned);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                changedGameState = gp.joinedGame;
                            } catch (IOException ioe)
                            {
                                closeEverything(socket, bufferedReader, bufferedWriter);
                            }
                        }
                    }

                     /*
                     Later, when more abilities are added to the game, players will transfer their loadout over TCP before joining a game.
                     This will match left click to the appropriate ability before the lobby is joined
                     */
                    // TCP code for left click attack is the number 1
                    if (delta >= 1 && gp.mouseHandler.playPressed1 && !gp.mouseHandler.playPressed1Cooldown && !gp.player.dying)
                    {
                        try
                        {
                            bufferedWriter.write("-" + clientUsername + "-" + 1 + "-" + gp.mouseHandler.mouseX + "-" + gp.mouseHandler.mouseY);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            delta = 0;
                        } catch (IOException ioe)
                        {
                            closeEverything(socket, bufferedReader, bufferedWriter);
                        }
                    }

                }
            }
        });
        t1.start();

    }

    public void listenForMessage(GamePanel gp)
    {
//        Thread t2 = Thread.startVirtualThread(new Runnable()
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
                                    if (tokens.length > 3)
                                    {
                                        gp.joinedGame = true;
                                    }
                                    else
                                    {
                                        otherPlayers.clear();
                                    }
                                }
                                else
                                {
                                    if (tokens.length < 4)
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
                                    else
                                    {
                                        for (int i = 0; i < otherPlayers.size(); i++)
                                        {
                                            if (otherPlayers.get(i).clientUserName.equals(tokens[1]))
                                            {
                                                otherPlayers.get(i).respawned = true;
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        else if (messageReceived != null && messageReceived.startsWith("-"))
                        {
                            String[] tokens = messageReceived.split("-");

                            if (Integer.parseInt(tokens[2]) == 1)
                            {
                                for (int i = 0; i < otherPlayers.size(); i++)
                                {
                                    if (otherPlayers.get(i).clientUserName.equals(tokens[1]))
                                    {
                                        otherPlayers.get(i).skill1 = true;
                                        otherPlayers.get(i).mouseX = Integer.parseInt(tokens[3]);
                                        otherPlayers.get(i).mouseY = Integer.parseInt(tokens[4]);
                                    }
                                }
                            }
                        }
                        else
                        {
//                            System.out.println("ELSE REACHED " + messageReceived);
                        }


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

    public boolean verifyLogin()
    {
        boolean validEntry = false;
        if (socket.isConnected())
        {

            String message = "";
            try
            {
                bufferedWriter.write("+" + clientUsername);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                while (message.isEmpty())
                {
                    message = bufferedReader.readLine().trim();
                }

            } catch (IOException ioe)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

            if (message.startsWith("+") && Boolean.parseBoolean(message.substring(1)))
            {
                validEntry = true;
            }

        }
        else
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
        return validEntry;
    }

}
