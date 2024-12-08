package me.rochblondiaux.limbo.network.protocol.packets.login.serverbound;

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
public class ServerboundLoginPluginResponsePacket implements ServerboundPacket {

    private int messageId;
    private boolean successful;
    private ByteMessage data;

    @Override
    public void decode(ByteMessage message, Version version) {
        this.messageId = message.readVarInt();
        this.successful = message.readBoolean();

        if (message.readableBytes() > 0) {
            int i = message.readableBytes();
            this.data = new ByteMessage(message.readBytes(i));
        }
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {
        server.packetHandler().handle(connection, this);
    }
}
