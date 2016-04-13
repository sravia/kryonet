package com;

import com.game.engineTester.MainGameLoop;
import com.net.Network;

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
