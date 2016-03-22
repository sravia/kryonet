package com.server;

import com.server.net.Network;

public class Server {

    private Network network;

    public Server() throws Exception {
        network = new Network();
        network.start();
    }

    public static void main(String[] args) throws Exception {
        new Server();
    }
}
