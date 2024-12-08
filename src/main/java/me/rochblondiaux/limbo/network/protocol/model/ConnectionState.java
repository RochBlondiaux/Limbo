package me.rochblondiaux.limbo.network.protocol.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static me.rochblondiaux.limbo.network.protocol.model.Version.*;
import static me.rochblondiaux.limbo.network.protocol.model.Version.max;
import me.rochblondiaux.limbo.network.protocol.packets.ClientboundDisconnectPacket;
import me.rochblondiaux.limbo.network.protocol.packets.ClientboundKeepAlivePacket;
import me.rochblondiaux.limbo.network.protocol.packets.ServerboundHandshakePacket;
import me.rochblondiaux.limbo.network.protocol.packets.ServerboundKeepAlivePacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundFinishConfigurationPacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundPluginMessagePacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundRegistryDataPacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.serverbound.ServerboundFinishConfigurationPacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.serverbound.ServerboundPluginMessagePacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.clientbound.ClientboundLoginPluginRequestPacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.clientbound.ClientboundLoginSuccessPacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginAcknowledgedPacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginPluginResponsePacket;
import me.rochblondiaux.limbo.network.protocol.packets.login.serverbound.ServerboundLoginStartPacket;
import me.rochblondiaux.limbo.network.protocol.packets.play.clientbound.ClientboundDeclareCommandsPacket;
import me.rochblondiaux.limbo.network.protocol.packets.play.clientbound.ClientboundJoinGamePacket;
import me.rochblondiaux.limbo.network.protocol.packets.play.clientbound.ClientboundPlayerAbilitiesPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusPongPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.clientbound.ClientboundStatusResponsePacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusPingPacket;
import me.rochblondiaux.limbo.network.protocol.packets.status.serverbound.ServerboundStatusRequestPacket;
import me.rochblondiaux.limbo.network.protocol.registry.Mapping;
import me.rochblondiaux.limbo.network.protocol.registry.ProtocolMappings;

@RequiredArgsConstructor
@Getter
public enum ConnectionState {
    HANDSHAKE(0) {
        {
            registerServerBoundPacket(ServerboundHandshakePacket::new, map(0x00));
        }
    },
    STATUS(1) {
        {
            // Client -> Server
            registerServerBoundPacket(ServerboundStatusRequestPacket::new, map(0x00));
            registerServerBoundPacket(ServerboundStatusPingPacket::new, map(0x01));

            // Server -> Client
            registerClientBoundPacket(ClientboundStatusResponsePacket::new, map(0x00));
            registerClientBoundPacket(ClientboundStatusPongPacket::new, map(0x01));
        }
    },
    LOGIN(2) {
        {
            // Client -> Server
            registerServerBoundPacket(ServerboundLoginStartPacket::new, map(0x00));
            registerServerBoundPacket(ServerboundLoginPluginResponsePacket::new, map(0x02));
            registerServerBoundPacket(ServerboundLoginAcknowledgedPacket::new, ConnectionState.map(0x03, V1_20_2, max()));

            // Server -> Client
            registerClientBoundPacket(ClientboundDisconnectPacket::new, map(0x00));
            registerClientBoundPacket(ClientboundLoginSuccessPacket::new, map(0x02));
            registerClientBoundPacket(ClientboundLoginPluginRequestPacket::new, map(0x04));
        }
    },
    CONFIGURATION(3) {
        {
            // Client -> Server
            registerServerBoundPacket(ServerboundPluginMessagePacket::new, map(0x00, V1_20_2, V1_20_3), map(0x01, V1_20_5, max()));
            registerServerBoundPacket(ServerboundFinishConfigurationPacket::new, map(0x01, V1_20_2, V1_20_3), map(0x02, V1_20_5, max()));
            registerServerBoundPacket(ServerboundKeepAlivePacket::new, map(0x03, V1_20_2, V1_20_3), map(0x04, V1_20_5, max()));

            // Server -> Client
            registerClientBoundPacket(ClientboundPluginMessagePacket::new, map(0x00, V1_20_2, V1_20_3), map(0x01, V1_20_5, max()));
            registerClientBoundPacket(ClientboundDisconnectPacket::new, map(0x01, V1_20_2, V1_20_3), map(0x02, V1_20_5, max()));
            registerClientBoundPacket(ClientboundFinishConfigurationPacket::new, map(0x02, V1_20_2, V1_20_3), map(0x03, V1_20_5, max()));
            registerClientBoundPacket(ClientboundKeepAlivePacket::new, map(0x03, V1_20_2, V1_20_3), map(0x04, V1_20_5, max()));
            registerClientBoundPacket(ClientboundRegistryDataPacket::new, map(0x05, V1_20_2, V1_20_3), map(0x07, V1_20_5, max()));
        }
    },
    PLAY(4) {
        {
            // Client -> Server

            // Server -> Client
            registerClientBoundPacket(ClientboundKeepAlivePacket::new,
                    map(0x00, V1_7_2, V1_8),
                    map(0x0B, V1_9, V1_11_1),
                    map(0x0C, V1_12, V1_12),
                    map(0x0B, V1_12_1, V1_12_2),
                    map(0x0E, V1_13, V1_13_2),
                    map(0x0F, V1_14, V1_15_2),
                    map(0x10, V1_16, V1_16_4),
                    map(0x0F, V1_17, V1_18_2),
                    map(0x11, V1_19, V1_19),
                    map(0x12, V1_19_1, V1_19_1),
                    map(0x11, V1_19_3, V1_19_3),
                    map(0x12, V1_19_4, V1_20),
                    map(0x14, V1_20_2, V1_20_2),
                    map(0x15, V1_20_3, V1_20_3),
                    map(0x18, V1_20_5, max()));
            registerClientBoundPacket(ClientboundDeclareCommandsPacket::new,
                    map(0x11, V1_13, V1_14_4),
                    map(0x12, V1_15, V1_15_2),
                    map(0x11, V1_16, V1_16_1),
                    map(0x10, V1_16_2, V1_16_4),
                    map(0x12, V1_17, V1_18_2),
                    map(0x0F, V1_19, V1_19_1),
                    map(0x0E, V1_19_3, V1_19_3),
                    map(0x10, V1_19_4, V1_20),
                    map(0x11, V1_20_2, max())
            );
            registerClientBoundPacket(ClientboundJoinGamePacket::new,
                    map(0x01, V1_7_2, V1_8),
                    map(0x23, V1_9, V1_12_2),
                    map(0x25, V1_13, V1_14_4),
                    map(0x26, V1_15, V1_15_2),
                    map(0x25, V1_16, V1_16_1),
                    map(0x24, V1_16_2, V1_16_4),
                    map(0x26, V1_17, V1_18_2),
                    map(0x23, V1_19, V1_19),
                    map(0x25, V1_19_1, V1_19_1),
                    map(0x24, V1_19_3, V1_19_3),
                    map(0x28, V1_19_4, V1_20),
                    map(0x29, V1_20_2, V1_20_3),
                    map(0x2B, V1_20_5, max())
            );
            registerClientBoundPacket(ClientboundPluginMessagePacket::new,
                    map(0x19, V1_13, V1_13_2),
                    map(0x18, V1_14, V1_14_4),
                    map(0x19, V1_15, V1_15_2),
                    map(0x18, V1_16, V1_16_1),
                    map(0x17, V1_16_2, V1_16_4),
                    map(0x18, V1_17, V1_18_2),
                    map(0x15, V1_19, V1_19),
                    map(0x16, V1_19_1, V1_19_1),
                    map(0x15, V1_19_3, V1_19_3),
                    map(0x17, V1_19_4, V1_20),
                    map(0x18, V1_20_2, V1_20_3),
                    map(0x19, V1_20_5, max())
            );
            registerClientBoundPacket(ClientboundPlayerAbilitiesPacket::new,
                    map(0x39, V1_7_2, V1_8),
                    map(0x2B, V1_9, V1_12),
                    map(0x2C, V1_12_1, V1_12_2),
                    map(0x2E, V1_13, V1_13_2),
                    map(0x31, V1_14, V1_14_4),
                    map(0x32, V1_15, V1_15_2),
                    map(0x31, V1_16, V1_16_1),
                    map(0x30, V1_16_2, V1_16_4),
                    map(0x32, V1_17, V1_18_2),
                    map(0x2F, V1_19, V1_19),
                    map(0x31, V1_19_1, V1_19_1),
                    map(0x30, V1_19_3, V1_19_3),
                    map(0x34, V1_19_4, V1_20),
                    map(0x36, V1_20_2, V1_20_3),
                    map(0x38, V1_20_5, max())
            );
        }
    };

    private final int id;
    private final ProtocolMappings serverBound = new ProtocolMappings();
    private final ProtocolMappings clientBound = new ProtocolMappings();

    // ID -> State
    private static final Map<Integer, ConnectionState> STATE_BY_ID = new HashMap<>();

    static {
        for (ConnectionState registry : values()) {
            STATE_BY_ID.put(registry.id, registry);
        }
    }

    public static ConnectionState getById(int stateId) {
        return STATE_BY_ID.get(stateId);
    }

    /**
     * Map packet id to version range
     *
     * @param packetId Packet id
     * @param from     Minimal version (include)
     * @param to       Last version (include)
     * @return Created mapping
     */
    private static Mapping map(int packetId, Version from, Version to) {
        return new Mapping(packetId, from, to);
    }

    private static Mapping map(int packetId, Version version) {
        return new Mapping(packetId, version, version);
    }

    private static Mapping map(int packetId) {
        return new Mapping(packetId, min(), max());
    }

    void registerServerBoundPacket(Supplier<? extends ServerboundPacket> packet, Mapping... mappings) {
        serverBound.register(packet, mappings);
    }

    void registerClientBoundPacket(Supplier<? extends ClientboundPacket> packet, Mapping... mappings) {
        clientBound.register(packet, mappings);
    }
}
