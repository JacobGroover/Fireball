package main;

import javax.swing.*;
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

        login.setLayout(new BoxLayout(login.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label1 = new JLabel("PLEASE CREATE A USERNAME TO LOGIN:");
        label1.setFont(new Font("Arial", Font.PLAIN, 20));
        label1.setBorder(new EmptyBorder(40, 40, 40, 40));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setAlignmentY(Component.TOP_ALIGNMENT);
        login.add(label1);

        JTextField textField1 = new JTextField(15);
        textField1.setFont(new Font("Arial", Font.PLAIN, 40));
        textField1.setMaximumSize(new Dimension(600, 50));
        textField1.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField1.setAlignmentY(Component.CENTER_ALIGNMENT);
        login.add(textField1);

        JButton button1 = new JButton("ONLINE SESSION");
        button1.setFont(new Font("Arial", Font.BOLD, 40));
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        //button1.setPreferredSize(new Dimension(500, 50));
        login.add(button1);

        JButton button2 = new JButton("LOGIN");
        button2.setFont(new Font("Arial", Font.BOLD, 80));
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        login.add(button2);

        //login.pack();
        login.setLocationRelativeTo(null);
        login.setVisible(true);

        button1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clickCount++;
                if (clickCount == 1)
                {
                    button1.setText("LOCAL SESSION");
                }
                if (clickCount == 2)
                {
                    button1.setText("ONLINE SESSION");
                    clickCount = 0;
                }
            }
        });

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
                    loginAttempt = true;
                    if (button1.getText().equals("ONLINE SESSION"))
                    {
                        onlineSession = true;
                    }
                    else
                    {
                        onlineSession = false;
                    }
                }
            }
        });

        Socket socket;
        try
        {
            socket = new Socket(Client.serverAddress, Client.serverTcpPort1);
        } catch (IOException e)
        {
            System.err.println("Host could not be found!");
            throw new RuntimeException(e);
        }

        TCPClient tcpClient;
        do
        {
            while (!loginAttempt)
            {
                Thread.onSpinWait();
            }
            loginAttempt = false;

            if (onlineSession)
            {
                Client.serverAddress = "99.147.220.159";
            }
            else
            {
                Client.serverAddress = "192.168.2.102";
            }

            tcpClient = new TCPClient(socket);

            validEntry = tcpClient.verifyLogin();
            if (!validEntry)
            {
                JOptionPane.showMessageDialog(null, "That username is already taken!",
                        "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                Thread.onSpinWait();

            }

        } while (!validEntry);
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
        UDPClient udpClient = new UDPClient(gamePanel);

        // Call listenForMessage() and sendJoinedGame() methods on this client instance; both run on separate threads and are blocked, so they both get called and run continuously while connected.
        tcpClient.listenForMessage(gamePanel);
        tcpClient.sendInfo(gamePanel);

        // Run UDP threads to handle movement and position packets
        udpClient.sendDP();
        udpClient.receiveDP();

    }
}
