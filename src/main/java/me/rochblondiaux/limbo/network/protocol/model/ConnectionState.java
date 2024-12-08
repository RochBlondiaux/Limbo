package me.rochblondiaux.limbo.network.protocol.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.packets.ServerboundHandshakePacket;
import me.rochblondiaux.limbo.network.protocol.registry.Mapping;
import me.rochblondiaux.limbo.network.protocol.registry.ProtocolMappings;

@RequiredArgsConstructor
@Getter
public enum ConnectionState {
    HANDSHAKE(0) {
        {
            registerServerBoundPacket(ServerboundHandshakePacket::new, map(0x00, Version.min(), Version.max()));
        }
    },
    STATUS(1),
    LOGIN(2),
    CONFIGURATION(3),
    PLAY(4);

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

    void registerServerBoundPacket(Supplier<?> packet, Mapping mapping) {
        serverBound.register(packet, mapping);
    }

    void registerClientBoundPacket(Supplier<?> packet, Mapping mapping) {
        clientBound.register(packet, mapping);
    }
}
