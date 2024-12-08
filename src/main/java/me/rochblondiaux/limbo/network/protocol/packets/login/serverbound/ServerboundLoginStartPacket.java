package me.rochblondiaux.limbo.network.protocol.packets.login.serverbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.server.LimboServer;

@Getter
@NoArgsConstructor
public class ServerboundLoginStartPacket implements ServerboundPacket {

    private String username;

    @Override
    public void decode(ByteMessage message, Version version) {
        this.username = message.readString();
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {
        server.packetHandler().handle(connection, this);
    }
}
