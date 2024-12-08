package me.rochblondiaux.limbo.network.protocol.packets.play.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@AllArgsConstructor
@NoArgsConstructor
public class ClientboundPlayerAbilitiesPacket implements ClientboundPacket {

    private int flags = 0x02;
    private float flyingSpeed = 0.0F;
    private float fieldOfView = 0.1F;

    @Override
    public void encode(ByteMessage message, Version version) {
        message.writeByte(this.flags);
        message.writeFloat(this.flyingSpeed);
        message.writeFloat(this.fieldOfView);
    }
}
