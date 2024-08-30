package main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class Main
{

    public static int clickCount;
    public static boolean onlineSession = true;
    public static volatile boolean loginAttempt;
    public static volatile boolean validEntry;
    public static void main(String[] args)
    {

        JFrame login = new JFrame();
        login.setSize(640, 400);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setResizable(false);
        login.setTitle("Login");

        login.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel label1 = new JLabel("PLEASE CREATE A USERNAME TO LOGIN:");
        label1.setFont(new Font("Arial", Font.PLAIN, 20));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setAlignmentY(Component.TOP_ALIGNMENT);
        login.add(label1);

        JTextField textField1 = new JTextField(15);
        textField1.setFont(new Font("Arial", Font.PLAIN, 40));
        textField1.setMaximumSize(new Dimension(600, 50));
        textField1.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField1.setAlignmentY(Component.CENTER_ALIGNMENT);
        login.add(textField1);

        JLabel label2 = new JLabel("SERVER IP ADDRESS:");
        label2.setFont(new Font("Arial", Font.PLAIN, 20));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setAlignmentY(Component.TOP_ALIGNMENT);
        login.add(label2);

        JTextField textField2 = new JTextField(15);
        textField2.setFont(new Font("Arial", Font.PLAIN, 40));
        textField2.setMaximumSize(new Dimension(600, 50));
        textField2.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField2.setAlignmentY(Component.CENTER_ALIGNMENT);
        login.add(textField2);

        JButton button2 = new JButton("LOGIN");
        button2.setFont(new Font("Arial", Font.BOLD, 80));
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        login.add(button2);

        login.setLocationRelativeTo(null);
        login.setVisible(true);

        button2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Client.clientUsername = textField1.getText();
                if (Client.clientUsername.length() < 2 || Client.clientUsername.length() > 15 || Client.clientUsername.contains("+") || Client.clientUsername.contains("-") ||
                        Client.clientUsername.contains(":") || Client.clientUsername.contains("*") || Client.clientUsername.substring(0, 1).matches("\\d"))
                {
                    loginAttempt = false;
                    JOptionPane.showMessageDialog(null, "Name must be between 2 and 15 characters, cannot start with a number, and cannot contain '+', '-', ':' or '*'",
                            "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    Client.serverAddress = textField2.getText();
                    if (Client.serverAddress.isBlank())
                    {
                        JOptionPane.showMessageDialog(null, "Please enter a server IP address in the form xxx.xxx.xxx.xxx or enter any other text to indicate an offline session.",
                                "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        loginAttempt = true;
                    }
                }
            }
        });

        while (!loginAttempt)
        {
            Thread.onSpinWait();
        }
        loginAttempt = false;

        Socket socket = null;
        TCPClient tcpClient = null;
        try
        {
            socket = new Socket(Client.serverAddress, Client.serverTcpPort1);
            do
            {

                tcpClient = new TCPClient(socket);

                validEntry = tcpClient.verifyLogin();
                if (!validEntry)
                {
                    JOptionPane.showMessageDialog(null, "That username is already taken!",
                            "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                    while (!loginAttempt)
                    {
                        Thread.onSpinWait();
                    }
                    loginAttempt = false;

                }

            } while (!validEntry);
            
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "HOST COULD NOT BE FOUND! STARTING GAME IN OFFLINE MODE.",
                    "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
//            System.err.println("Host could not be found!");
//            throw new RuntimeException(e);    // If this is uncommented, offline games will not work (since throwing the exception will end runtime)
        }
        login.dispose();

        JFrame window = new JFrame();   // create a window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set window to exit when closed
        window.setResizable(false); // prevents resizing of the window
        window.setTitle(Client.clientUsername);  // set name of the window

        // Add GamePanel object to main method for instantiating GUI on game launch
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();  // Causes the GUI window to be sized to fit the preferred size and layouts of its subcomponents (GamePanel class in this case)

        window.setLocationRelativeTo(null); // sets window not to be relative to any [coordinate] on the screen, therefore null defaults it to center of screen
        window.setVisible(true);    // makes window visible

        gamePanel.setupGame();
        gamePanel.startGameThread();

        if (socket != null)
        {
            UDPClient udpClient = new UDPClient(gamePanel);

            // Call listenForMessage() and sendInfo() methods on this client instance; both run on separate threads and are blocked, so they both get called and run continuously while connected.
            tcpClient.listenForMessage(gamePanel);
            tcpClient.sendInfo(gamePanel);

            // Run UDP threads to handle movement and position packets
            udpClient.sendDP();
            udpClient.receiveDP();
        }

    }
}
