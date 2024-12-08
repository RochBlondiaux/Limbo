package me.rochblondiaux.limbo.network.protocol.packets.status.serverbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.server.LimboServer;

@NoArgsConstructor
@Getter
public class ServerboundStatusPingPacket implements ServerboundPacket {

    private long timestamp;

    @Override
    public void decode(ByteMessage message, Version version) {
        this.timestamp = message.readLong();
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {
        server.packetHandler().handle(connection, this);
    }

}
