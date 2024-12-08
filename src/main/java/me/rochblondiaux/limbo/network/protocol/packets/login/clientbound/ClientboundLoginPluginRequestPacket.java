package me.rochblondiaux.limbo.network.protocol.packets.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundLoginPluginRequestPacket implements ClientboundPacket {

    private int messageId;
    private String channel;
    private ByteBuf data;

    @Override
    public void encode(ByteMessage message, Version version) {
        message.writeVarInt(this.messageId);
        message.writeString(this.channel);
        message.writeBytes(this.data);
    }
}
