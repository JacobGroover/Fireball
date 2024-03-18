package main;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        boolean validEntry;
        String username;
        // digital media classroom 10.255.11.27
        // intermediate programming classroom 10.255.4.72
        // Oghma-Infinium Home IP 192.168.2.102
        // GGC Database class IP 10.255.36.219
        Client.serverAddress = "192.168.2.102";
        //String serverAddress = "10.255.5.7"; // GGC IP Address
        Client.serverTcpPort1 = 6682;
        Client.serverUdpPort1 = 4445;
        //int serverUdpPort = 6690; // UDP
        do
        {
            validEntry = true;
            System.out.println("Enter your username for the group chat: ");
            username = scanner.nextLine();
            if (username.length() <= 2 || username.contains("+") || username.contains("-") ||
                    username.contains(":") || username.contains("*") || username.substring(0, 1).matches("\\d"))
            {
                System.out.println("Name must be longer than 2 characters, cannot start with a number, and cannot contain '+', '-', ':' or '*'");
                validEntry = false;
            }
        } while (!validEntry);

        // PORTED FROM PREVIOUS MAIN CLASS
        JFrame window = new JFrame();   // create a window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set window to exit when closed
        window.setResizable(false); // prevents resizing of the window
        window.setTitle(username);  // set name of the window
        // Add GamePanel object to main method for instantiating GUI on game launch
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();  // Causes the GUI window to be sized to fit the preferred size and layouts of its subcomponents (GamePanel class in this case)

        window.setLocationRelativeTo(null); // sets window not to be relative to any [coordinate] on the screen, therefore null defaults it to center of screen
        window.setVisible(true);    // makes window visible

        gamePanel.setupGame();
        gamePanel.startGameThread();
        // END OF PORT FROM PREVIOUS MAIN CLASS

        try
        {

            //DatagramSocket datagramSocket = new DatagramSocket();



            Socket socket = new Socket(Client.serverAddress, Client.serverTcpPort1);
            TCPClient tcpClient = new TCPClient(socket, username);
            UDPClient udpClient = new UDPClient(gamePanel);

            //UDPClient udpClient = new UDPClient(datagramSocket, username, serverAddress, serverUdpPort);

            // Call listenForMessage() and sendMessage() methods on this client instance; both run on separate threads and are blocked, so they both get called and run continuously while connected.
            tcpClient.listenForMessage(gamePanel);    // COMMENT
            udpClient.run();    // COMMENT
            //tcpClient.sendPosition();


            //tcpClient.listenForPosition();
            //udpClient.sendPosition(gamePanel);
            //udpClient.listenForPosition();
            //tcpClient.sendMessage();

        } catch (IOException ioe)
        {
            System.err.println("Host could not be found!");
            //throw new RuntimeException(ioe);
        }

    }

}
