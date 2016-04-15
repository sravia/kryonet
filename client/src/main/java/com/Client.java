package com;

import com.engine.Engine;
import com.net.Network;

public class Client {

    private Network network;

    public Client() throws Exception {
        network = new Network();
        //network.start();
        new Engine().run();
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

}
