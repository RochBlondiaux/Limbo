package me.rochblondiaux.limbo.network.protocol.packets.configuration.serverbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.server.LimboServer;

@AllArgsConstructor
@NoArgsConstructor
public class ServerboundPluginMessagePacket implements ServerboundPacket {

    private String channel;
    private String message;

    @Override
    public void decode(ByteMessage message, Version version) {
        this.channel = message.readString();
        this.message = message.readString();
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {

    }
}
