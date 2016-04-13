package com.net;

import com.Config;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.net.handlers.PacketMessage;
import org.apache.log4j.Logger;

public class Network extends Listener {
    private final static Logger LOGGER = Logger.getLogger(Network.class);
    private Client client;
    private boolean messageReceived = false;

    public void start() throws Exception {
        LOGGER.info("Starting!");
        client = new Client();

        client.getKryo().register(PacketMessage.class);
        client.start();
        client.connect(5000, Config.HOST, Config.TCP_PORT, Config.UDP_PORT);

        client.addListener(new Network());
        LOGGER.info("Started! Waiting for connection...");

        while (!messageReceived) {
            Thread.sleep(1000);
        }

        LOGGER.info("Exiting!");
        System.exit(0);
    }

    public void received(Connection c, Object p) {
        if (p instanceof PacketMessage) {
            PacketMessage packet = (PacketMessage) p;
            LOGGER.info("received a message from the host: " + packet.message);
            messageReceived = true;
        }
    }

}
