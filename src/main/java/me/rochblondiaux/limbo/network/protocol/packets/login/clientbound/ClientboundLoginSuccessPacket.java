package me.rochblondiaux.limbo.network.protocol.packets.login.clientbound;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@AllArgsConstructor
@NoArgsConstructor
public class ClientboundLoginSuccessPacket implements ClientboundPacket {

    private UUID uniqueId;
    private String username;

    @Override
    public void encode(ByteMessage message, Version version) {
        if (version.moreOrEqual(Version.V1_16))
            message.writeUuid(this.uniqueId);
        else if (version.moreOrEqual(Version.V1_7_6))
            message.writeString(this.uniqueId.toString());
        else
            message.writeString(this.uniqueId.toString().replace("-", ""));

        message.writeString(this.username);
        if (version.moreOrEqual(Version.V1_19))
            message.writeVarInt(0);
        if (version.moreOrEqual(Version.V1_20_5) && version.less(Version.V1_21_3))
            message.writeBoolean(true);
    }
}
