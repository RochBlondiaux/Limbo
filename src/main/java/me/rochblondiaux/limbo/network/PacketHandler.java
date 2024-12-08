package me.rochblondiaux.limbo.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.packets.ServerboundHandshakePacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusPongPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusResponsePacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusPingPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusRequestPacket;
import me.rochblondiaux.limbo.server.LimboServer;

@RequiredArgsConstructor
@Log4j2
public class PacketHandler {

    private final Limbo app;
    private final LimboServer server;

    public void handle(ClientConnection connection, ServerboundHandshakePacket packet) {
        // Update connection
        connection.updateVersion(packet.version());
        connection.updateState(packet.nextState());

        log.info("Pinged from {} [{}]", connection.address(), packet.version());

        // TODO: Handle info forwarding
    }

    public void handle(ClientConnection connection, ServerboundStatusRequestPacket unused) {
        log.debug("Received status request from {}", connection.address());
        connection.sendPacket(new ClientboundStatusResponsePacket(app));
    }

    public void handle(ClientConnection connection, ServerboundStatusPingPacket packet) {
        log.debug("Received status ping from {} (end of status state)", connection.address());
        connection.sendPacketAndDisconnect(new ClientboundStatusPongPacket(packet.timestamp()));
    }


}
