import java.io.*;
import java.net.Socket;

/**
 * This class: // Each object of this class will be responsible for communicating with a client from Server class.
 * Implements Runnable interface, indicating that each instance of this class will be executed on a separate thread;
 * Specifically, code in the overridden run() method will be executed on a separate thread.
 */
public class TcpHandler extends ClientHandler implements Runnable
{
    private Socket TCPSocket;  // Socket object passed through constructor from Server class. Used to establish a connection between client and server.
    private BufferedReader bufferedReader;  // Reads messages sent from the client
    private BufferedWriter bufferedWriter;  // Sends messages to the client (either from other clients or from the server)

    public TcpHandler(Socket serverSocketTCP)
    {
        try
        {
            this.TCPSocket = serverSocketTCP;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocketTCP.getOutputStream()));   // OutputStreamWriter is for character streams, OutputStream is for bytes. Therefore, wrap the byte stream in a character stream. Buffering the stream increases efficiency.
            this.bufferedReader = new BufferedReader(new InputStreamReader(serverSocketTCP.getInputStream()));   // BufferedWriter stream is for sending, BufferedReader for receiving.

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
//                System.out.println(messageFromClient + " " + this.TCPSocket.getInetAddress() + ":" + this.TCPSocket.getPort());   // FOR TESTING
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                break;
            }

            if (messageFromClient.startsWith("*"))
            {
                String[] tokens = messageFromClient.split("\\*");
                if (tokens.length < 4)
                {
                    this.setJoinedGame(Boolean.parseBoolean(tokens[2].substring(0, 4)));
                    broadcastJoinGame(tokens[1], Boolean.parseBoolean(tokens[2].substring(0, 4)));
                }
                else
                {
                    broadcastJoinGame(tokens[1], Boolean.parseBoolean(tokens[2].substring(0, 4)), Boolean.parseBoolean(tokens[3].substring(0, 4)));
                }
            }
            else if (messageFromClient.startsWith("-"))
            {
                String[] tokens = messageFromClient.split("-");
                this.setPlayPressed1(Integer.parseInt(tokens[2]));  // add if/else clause later to check which button was pressed (when more abilities are added)
                this.setMouseX(Integer.parseInt(tokens[3]));
                this.setMouseY(Integer.parseInt(tokens[4]));
                broadcastButtonPress(tokens[1], this.getPlayPressed1(), this.getMouseX(), this.getMouseY());
            }
            else if (messageFromClient.startsWith("+"))
            {
                validateLoginCredentials(messageFromClient);
            }
        }
    }

    private void validateLoginCredentials(String messageFromClient)
    {
        try
        {
            this.clientUsername = messageFromClient.substring(1);
            boolean duplicate = false;
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (clientDataAL.get(i).clientUsername.equalsIgnoreCase(messageFromClient.substring(1)))
                {
                    duplicate = true;
                    // SEND TCP PACKET INDICATING VALID ENTRY TO CLIENT
                    this.bufferedWriter.write("+" + false);
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.
                    break;
                }
            }
            if (!duplicate)
            {
                clientDataAL.add(this);
                // SEND TCP PACKET INDICATING VALID ENTRY TO CLIENT
                this.bufferedWriter.write("+" + true);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.
            }

        } catch (IOException ioe)
        {
            closeEverything(TCPSocket, bufferedReader, bufferedWriter);
        }
    }

    public void constructorBroadcastMessage(String messageToSend)
    {
        for (int i = 0; i < clientDataAL.size(); i++)
        {
            try
            {
                ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write(messageToSend);
                ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }

        }

    }

    public void broadcastMessage(String messageToSend)
    {
        for (int i = 0; i < clientDataAL.size(); i++)
        {
            try
            {
                if (!clientDataAL.get(i).clientUsername.equals(this.clientUsername))
                {
                    ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write(messageToSend);
                    ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                    ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.
                }
            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }

        }

    }

    public void broadcastJoinGame(String clientUsername, boolean joinedGame)
    {

        if (joinedGame)
        {
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (!clientDataAL.get(i).getClientUsername().equals(clientUsername) && clientDataAL.get(i).getJoinedGame())
                {
                    try
                    {
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write("*" + clientUsername + "*" + joinedGame);
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                        this.bufferedWriter.write("*" + clientDataAL.get(i).getClientUsername() + "*" + clientDataAL.get(i).getJoinedGame());
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                    } catch (IOException ioe)
                    {
                        closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (!clientDataAL.get(i).getClientUsername().equals(clientUsername) && clientDataAL.get(i).getJoinedGame())
                {
                    try
                    {
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write("*" + clientUsername + "*" + joinedGame);
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                    } catch (IOException ioe)
                    {
                        closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
            try
            {
                this.bufferedWriter.write("*" + clientUsername + "*" + joinedGame);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }

        }

    }

    public void broadcastJoinGame(String clientUsername, boolean joinedGame, boolean respawned)
    {

        if (joinedGame)
        {
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (!clientDataAL.get(i).getClientUsername().equals(clientUsername) && clientDataAL.get(i).getJoinedGame())
                {
                    try
                    {
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write("*" + clientUsername + "*" + joinedGame);
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                        this.bufferedWriter.write("*" + clientDataAL.get(i).getClientUsername() + "*" + clientDataAL.get(i).getJoinedGame());
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                    } catch (IOException ioe)
                    {
                        closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (!clientDataAL.get(i).getClientUsername().equals(clientUsername) && clientDataAL.get(i).getJoinedGame())
                {
                    try
                    {
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write("*" + clientUsername + "*" + joinedGame + "*" + respawned);
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                    } catch (IOException ioe)
                    {
                        closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
            try
            {
                this.bufferedWriter.write("*" + clientUsername + "*" + joinedGame + "*" + respawned);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

            } catch (IOException ioe)
            {
                closeEverything(TCPSocket, bufferedReader, bufferedWriter);
            }

        }

    }

    public void broadcastButtonPress(String clientUsername, int buttonPressed, int mouseX, int mouseY)
    {

        if (joinedGame)
        {
            for (int i = 0; i < clientDataAL.size(); i++)
            {
                if (!clientDataAL.get(i).getClientUsername().equals(clientUsername) && clientDataAL.get(i).getJoinedGame())
                {
                    try
                    {
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.write("-" + clientUsername + "-" + buttonPressed + "-" + mouseX + "-" + mouseY);
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.newLine();
                        ((TcpHandler) clientDataAL.get(i)).bufferedWriter.flush();   // Buffer won't be sent to Output Stream unless it is full, so flush() is necessary to force the buffer to send the message and empty.

                    } catch (IOException ioe)
                    {
                        closeEverything(TCPSocket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }

    }

    public void removeClientHandler()
    {
        clientDataAL.remove(this);
        Server.clientCount--;
        Server.clientLabel.setText("Clients Connected: " + Server.clientCount);
//        broadcastMessage("Server: " + clientUsername + " has left the chat!");
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

}
