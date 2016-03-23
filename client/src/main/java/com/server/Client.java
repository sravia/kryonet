package com.server;

import com.server.game.Game;
import com.server.net.Network;

public class Client {

    private Network network;

    public Client() throws Exception {
        network = new Network();
        //network.start();

        new Game();
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

}
