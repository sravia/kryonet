package com.server;

import com.server.game.engineTester.MainGameLoop;
import com.server.net.Network;

public class Client {

    private Network network;

    public Client() throws Exception {
        network = new Network();
        //network.start();
        new MainGameLoop().run();
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

}
