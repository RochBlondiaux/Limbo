package me.rochblondiaux.limbo.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.ClientConnection;
import me.rochblondiaux.limbo.network.protocol.PacketSnapshots;
import me.rochblondiaux.limbo.network.protocol.model.ConnectionState;
import me.rochblondiaux.limbo.network.protocol.model.PacketSnapshot;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.network.protocol.packets.ClientboundDisconnectPacket;
import me.rochblondiaux.limbo.network.protocol.packets.ServerboundHandshakePacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundFinishConfigurationPacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.serverbound.ServerboundFinishConfigurationPacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginAcknowledgedPacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginPluginResponsePacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginStartPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusPongPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusResponsePacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusPingPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusRequestPacket;
import me.rochblondiaux.limbo.server.LimboServer;
import me.rochblondiaux.limbo.utils.UUIDUtils;

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

    public void handle(ClientConnection connection, ServerboundLoginStartPacket packet) {
        log.debug("Received login start from {}", connection.address());

        // Check there is room for the player
        if (app.configuration().maxPlayers() <= server.connections().count()) {
            connection.sendPacketAndDisconnect(new ClientboundDisconnectPacket("Server is full"));
            log.warn("Player {} tried to join but the server is full", packet.username());
            return;
        }

        // Check if we support the player's version
        if (!connection.version().isSupported()) {
            connection.sendPacketAndDisconnect(new ClientboundDisconnectPacket("Unsupported client version"));
            log.warn("Player {} tried to join with an unsupported version", packet.username());
            return;
        }

        // TODO: check if player is already connected

        // TODO: velocity / bungeecord support

        // Update connection profile
        connection.profile(new GameProfile(
                UUIDUtils.getOfflineModeUuid(packet.username()),
                packet.username()
        ));

        connection.fireLoginSuccess();
    }

    public void handle(ClientConnection connection, ServerboundLoginPluginResponsePacket packet) {
        // TODO: handle velocity modern
    }

    public void handle(ClientConnection connection, ServerboundLoginAcknowledgedPacket packet) {
        log.debug("Received login acknowledge from {}", connection.address());
        connection.updateState(ConnectionState.CONFIGURATION);

        // Brand
        if (PacketSnapshots.PACKET_PLUGIN_MESSAGE != null)
            connection.writePacket(PacketSnapshots.PACKET_PLUGIN_MESSAGE);

        // Registry data
        if (connection.version().moreOrEqual(Version.V1_20_5)) {
            for (PacketSnapshot packetSnapshot : PacketSnapshots.PACKETS_REGISTRY_DATA) {
                connection.writePacket(packetSnapshot);
            }
        } else {
            connection.writePacket(PacketSnapshots.PACKET_REGISTRY_DATA);
        }

        // Finish configuration
        connection.sendPacket(new ClientboundFinishConfigurationPacket());
    }

    public void handle(ClientConnection connection, ServerboundFinishConfigurationPacket packet) {
        log.debug("Received finish configuration from {}", connection.address());

        connection.spawnPlayer();
    }
}
