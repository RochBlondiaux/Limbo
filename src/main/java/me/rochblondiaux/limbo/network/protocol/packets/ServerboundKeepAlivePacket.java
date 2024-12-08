package me.rochblondiaux.limbo.network.protocol.packets;

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
public class ServerboundKeepAlivePacket implements ServerboundPacket {

    private long id;

    @Override
    public void decode(ByteMessage message, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            this.id = message.readLong();
        } else if (version.moreOrEqual(Version.V1_8)) {
            this.id = message.readVarInt();
        } else {
            this.id = message.readInt();
        }
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {

    }
}
