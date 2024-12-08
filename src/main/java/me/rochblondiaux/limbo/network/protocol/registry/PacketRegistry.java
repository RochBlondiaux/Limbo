package me.rochblondiaux.limbo.network.protocol.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.Getter;
import me.rochblondiaux.limbo.network.protocol.model.Packet;
import me.rochblondiaux.limbo.network.protocol.model.Version;

public class PacketRegistry {

    @Getter
    private final Version version;
    private final Map<Integer, Supplier<?>> packetsById = new HashMap<>();
    private final Map<Class<?>, Integer> packetIdByClass = new HashMap<>();

    public PacketRegistry(Version version) {
        this.version = version;
    }

    public Packet getPacket(int packetId) {
        Supplier<?> supplier = packetsById.get(packetId);
        return supplier == null ? null : (Packet) supplier.get();
    }

    public int getPacketId(Class<?> packetClass) {
        return packetIdByClass.getOrDefault(packetClass, -1);
    }

    public void register(int packetId, Supplier<?> supplier) {
        packetsById.put(packetId, supplier);
        packetIdByClass.put(supplier.get().getClass(), packetId);
    }
}