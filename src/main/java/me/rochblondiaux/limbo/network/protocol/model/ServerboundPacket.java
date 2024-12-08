package me.rochblondiaux.limbo.network.protocol.model;

import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.server.LimboServer;

public interface ServerboundPacket extends Packet {

    void decode(ByteMessage message, Version version);

    void handle(Limbo app, LimboServer server, ClientConnection connection);

}
