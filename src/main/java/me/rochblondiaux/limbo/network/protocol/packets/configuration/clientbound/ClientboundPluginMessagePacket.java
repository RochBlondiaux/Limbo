package me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@AllArgsConstructor
@NoArgsConstructor
public class ClientboundPluginMessagePacket implements ClientboundPacket {

    private String channel;
    private String message;

    @Override
    public void encode(ByteMessage message, Version version) {
        message.writeString(this.channel);
        message.writeString(this.message);
    }
}
