package me.rochblondiaux.limbo.network.protocol.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PacketSnapshot implements ClientboundPacket {

    private final ClientboundPacket packet;
    private final Map<Version, byte[]> versionMessages = new HashMap<>();
    private final Map<Version, Version> mappings = new HashMap<>();

    public void encode() {
        Map<Integer, Version> hashes = new HashMap<>();

        for (Version version : Version.values()) {
            if (version.equals(Version.UNDEFINED)) continue;

            ByteMessage encodedMessage = ByteMessage.create();
            packet.encode(encodedMessage, version);

            int hash = encodedMessage.hashCode();
            Version hashed = hashes.get(hash);

            if (hashed != null) {
                mappings.put(version, hashed);
            } else {
                hashes.put(hash, version);
                mappings.put(version, version);
                versionMessages.put(version, encodedMessage.toByteArray());
            }

            encodedMessage.release();
        }
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        Version mapped = mappings.get(version);
        byte[] message = versionMessages.get(mapped);

        if (message != null)
            msg.writeBytes(message);
        else
            throw new IllegalArgumentException("No mappings for version " + version);
    }

    public static PacketSnapshot of(ClientboundPacket packet) {
        PacketSnapshot snapshot = new PacketSnapshot(packet);
        snapshot.encode();
        return snapshot;
    }

    @Override
    public String toString() {
        return packet.getClass().getSimpleName();
    }
}
