package me.rochblondiaux.limbo.network.protocol.packets;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundKeepAlivePacket implements ClientboundPacket {

    private long id;

    @Override
    public void encode(ByteMessage message, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            message.writeLong(id);
        } else if (version.moreOrEqual(Version.V1_8)) {
            message.writeVarInt((int) id);
        } else {
            message.writeInt((int) id);
        }
    }
}
