package me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.MetadataWriter;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.world.DimensionRegistry;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements ClientboundPacket {

    private DimensionRegistry registry;
    private MetadataWriter writer;

    @Override
    public void encode(ByteMessage message, Version version) {
        if (this.writer != null) {
            if (version.moreOrEqual(Version.V1_20_5)) {
                writer.writeData(message, version);
                return;
            }
        }

        message.writeNamelessCompoundTag(this.registry.codec_1_20());
    }

}
