package me.rochblondiaux.limbo.network.protocol.registry;

import java.util.*;
import java.util.function.Supplier;

import me.rochblondiaux.limbo.network.protocol.model.Version;

public class ProtocolMappings {

    private final Map<Version, PacketRegistry> registry = new HashMap<>();

    public PacketRegistry getRegistry(Version version) {
        return registry.getOrDefault(version, registry.get(Version.min()));
    }

    public void register(Supplier<?> packet, Mapping... mappings) {
        for (Mapping mapping : mappings) {
            for (Version ver : getRange(mapping)) {
                PacketRegistry reg = registry.computeIfAbsent(ver, PacketRegistry::new);
                reg.register(mapping.packetId(), packet);
            }
        }
    }

    private Collection<Version> getRange(Mapping mapping) {
        Version from = mapping.from();
        Version curr = mapping.to();
        if (curr == from)
            return Collections.singletonList(from);

        List<Version> versions = new LinkedList<>();
        while (curr != from) {
            versions.add(curr);
            curr = curr.prev();
        }
        versions.add(from);
        return versions;
    }

}

