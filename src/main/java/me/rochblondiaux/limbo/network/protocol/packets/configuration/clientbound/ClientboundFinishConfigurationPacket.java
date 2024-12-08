package me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
public class ClientboundFinishConfigurationPacket implements ClientboundPacket {

    @Override
    public void encode(ByteMessage message, Version version) {

    }
}
