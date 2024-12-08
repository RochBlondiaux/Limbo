package me.rochblondiaux.limbo.network.protocol.packets.status.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundStatusPongPacket implements ClientboundPacket {

    private long timestamp;

    @Override
    public void encode(ByteMessage message, Version version) {
        message.writeLong(this.timestamp);
    }
}
