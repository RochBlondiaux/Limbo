package me.rochblondiaux.limbo.network.protocol.packets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ConnectionState;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.server.LimboServer;

@NoArgsConstructor
@Getter
@Log4j2
public class ServerboundHandshakePacket implements ServerboundPacket {

    private Version version;
    private String host;
    private int port;
    private ConnectionState nextState;

    @Override
    public void decode(ByteMessage message, Version version) {
        try {
            this.version = Version.of(message.readVarInt());
        } catch (IllegalArgumentException e) {
            this.version = Version.UNDEFINED;
        }

        this.host = message.readString();
        this.port = message.readUnsignedShort();
        this.nextState = ConnectionState.getById(message.readVarInt());
    }

    @Override
    public void handle(Limbo app, LimboServer server, ClientConnection connection) {
        log.info("Received handshake packet from {}", connection.address());
    }
}
