package main;

import entities.OtherPlayer;

import java.util.ArrayList;

public abstract class Client
{

    protected static String serverAddress;
    protected static int serverTcpPort1 = 6682;
    protected static int serverUdpPort1 = 4445;
    protected static String clientUsername;
    protected static volatile boolean validEntry = false;
    public volatile static ArrayList<OtherPlayer> otherPlayers = new ArrayList<>();

}
