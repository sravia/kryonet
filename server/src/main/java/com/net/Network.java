package com.net;

import com.Config;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.net.handlers.PacketMessage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

public class Network extends Listener {
    private final static Logger LOGGER = Logger.getLogger(Network.class);

    private Server server;

    public void start() throws IOException {
        LOGGER.info("Starting up!");

        server = new Server();
        server.getKryo().register(PacketMessage.class);
        server.bind(Config.TCP_PORT, Config.UDP_PORT);
        server.start();
        server.addListener(new Network());

        LOGGER.info("Started!");
    }

    public void connected(Connection c) {
        LOGGER.info("Received a connection from " + c.getRemoteAddressTCP().getHostName());
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = "The time is: " + new Date().toString();

        c.sendTCP(packetMessage);
        //c.sendUDP(packetMessage);
    }

    public void received(Connection c, Object p) {

    }

    public void disconnected(Connection c) {
        LOGGER.info("A client disconnected!");
    }
}
