package com.server;

import com.server.net.Network;

public class Client {

    private Network network;

    public Client() throws Exception {
        network = new Network();
        network.start();
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

}
