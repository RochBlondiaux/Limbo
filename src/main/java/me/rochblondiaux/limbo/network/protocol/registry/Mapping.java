package me.rochblondiaux.limbo.network.protocol.registry;

import me.rochblondiaux.limbo.network.protocol.model.Version;

public record Mapping(int packetId, Version from, Version to) {
}
