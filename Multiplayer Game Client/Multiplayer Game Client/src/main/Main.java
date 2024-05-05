package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main
{

    public static String username;
    public static volatile boolean validEntry;
    public static void main(String[] args)
    {

            JFrame login = new JFrame();
            login.setSize(640, 400);
            login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            login.setResizable(false);
            login.setTitle("Login");

            login.setLayout(new BoxLayout(login.getContentPane(), BoxLayout.Y_AXIS));

            JLabel label1 = new JLabel("PLEASE CREATE A USERNAME TO LOGIN:");
            label1.setFont(new Font("Arial", Font.PLAIN, 20));
            label1.setBorder(new EmptyBorder(40, 40, 40, 40));
            label1.setAlignmentX(Component.CENTER_ALIGNMENT);
            label1.setAlignmentY(Component.TOP_ALIGNMENT);

            JTextField textField1 = new JTextField(15);
            textField1.setFont(new Font("Arial", Font.PLAIN, 40));
            //textField1.setBorder(new EmptyBorder(40, 40, 40, 40));
            textField1.setMaximumSize(new Dimension(600, 50));
            textField1.setAlignmentX(Component.CENTER_ALIGNMENT);
            textField1.setAlignmentY(Component.CENTER_ALIGNMENT);

            JButton button1 = new JButton("LOGIN");
            button1.setFont(new Font("Arial", Font.BOLD, 80));
            button1.setAlignmentX(Component.CENTER_ALIGNMENT);
            button1.setAlignmentY(Component.BOTTOM_ALIGNMENT);

            button1.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    username = textField1.getText();
                    if (username.length() < 2 || username.length() > 15 || username.contains("+") || username.contains("-") ||
                            username.contains(":") || username.contains("*") || username.substring(0, 1).matches("\\d"))
                    {
                        validEntry = false;
                        JOptionPane.showMessageDialog(null, "Name must be between 2 and 15 characters, cannot start with a number, and cannot contain '+', '-', ':' or '*'",
                                "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        validEntry = true;
                        login.dispose();
                    }
                }
            });

            login.add(label1);
            login.add(textField1);
            login.add(button1);

            //login.pack();
            login.setLocationRelativeTo(null);
            login.setVisible(true);

        while (!validEntry)
        {
            Thread.onSpinWait();
        }

        // Oghma-Infinium Home IP 192.168.2.102
        Client.serverAddress = "192.168.2.102";
        Client.serverTcpPort1 = 6682;
        Client.serverUdpPort1 = 4445;

        /*Scanner scanner = new Scanner(System.in);
        boolean validEntry;
        String username;
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
        } while (!validEntry);*/

        // PORTED FROM PREVIOUS MAIN CLASS
        JFrame window = new JFrame();   // create a window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set window to exit when closed
        //window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            Socket socket = new Socket(Client.serverAddress, Client.serverTcpPort1);
            TCPClient tcpClient = new TCPClient(socket, username);
            UDPClient udpClient = new UDPClient(gamePanel);

            //UDPClient udpClient = new UDPClient(datagramSocket, username, serverAddress, serverUdpPort);

            // Call listenForMessage() and sendMessage() methods on this client instance; both run on separate threads and are blocked, so they both get called and run continuously while connected.
            tcpClient.listenForMessage(gamePanel);    // COMMENT
            //tcpClient.sendJoinedGame(gamePanel);
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
