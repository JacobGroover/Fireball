package main;

import entities.OtherPlayer;

import java.util.ArrayList;

public abstract class Client
{

    protected static String serverAddress;
    protected static int serverTcpPort1;
    protected static int serverUdpPort1;
    protected static String clientUsername;
    public volatile static ArrayList<OtherPlayer> otherPlayers = new ArrayList<>();

}
