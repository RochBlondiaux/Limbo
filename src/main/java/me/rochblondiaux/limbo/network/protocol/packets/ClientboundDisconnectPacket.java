package me.rochblondiaux.limbo.network.protocol.packets;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundDisconnectPacket implements ClientboundPacket {

    private String reason;

    @Override
    public void encode(ByteMessage message, Version version) {
        message.writeString(reason);
    }
}
