package me.rochblondiaux.limbo.network.protocol;

import java.util.ArrayList;
import java.util.List;

import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.LimboConstants;
import me.rochblondiaux.limbo.configuration.ServerConfiguration;
import me.rochblondiaux.limbo.network.protocol.model.MetadataWriter;
import me.rochblondiaux.limbo.network.protocol.model.PacketSnapshot;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundPluginMessagePacket;
import me.rochblondiaux.limbo.network.protocol.packets.configuration.clientbound.ClientboundRegistryDataPacket;
import me.rochblondiaux.limbo.network.protocol.packets.play.clientbound.ClientboundJoinGamePacket;
import me.rochblondiaux.limbo.network.protocol.packets.play.clientbound.ClientboundPlayerAbilitiesPacket;
import me.rochblondiaux.limbo.server.LimboServer;
import me.rochblondiaux.limbo.world.Dimension;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;

public class PacketSnapshots {

    public static PacketSnapshot PACKET_PLUGIN_MESSAGE;
    public static PacketSnapshot PACKET_REGISTRY_DATA;
    public static List<PacketSnapshot> PACKETS_REGISTRY_DATA;
    public static PacketSnapshot PLAYER_ABILITIES;
    public static PacketSnapshot PACKET_JOIN_GAME;

    public static void init(Limbo app, LimboServer server) {
        ServerConfiguration configuration = app.configuration();

        // Join game
        PACKET_JOIN_GAME = PacketSnapshot.of(new ClientboundJoinGamePacket(
                0,
                false,
                2,
                -1,
                new String[]{"minecraft:overworld"},
                app.dimensions(),
                "minecraft:overworld",
                0,
                configuration.maxPlayers(),
                2,
                false,
                true,
                false,
                false,
                false,
                false
        ));

        // Brand name
        if (configuration.brand() != null)
            PACKET_PLUGIN_MESSAGE = PacketSnapshot.of(new ClientboundPluginMessagePacket(
                    LimboConstants.BRAND_CHANNEL,
                    configuration.brand()
            ));

        // Player abilities
        PLAYER_ABILITIES = PacketSnapshot.of(new ClientboundPlayerAbilitiesPacket(
                0x02,
                0.0F,
                0.1F
        ));

        // Registry data
        PACKET_REGISTRY_DATA = PacketSnapshot.of(new ClientboundRegistryDataPacket(
                app.dimensions(),
                null
        ));

        // Packets registry data
        Dimension dimension1_21 = app.dimensions().dimension_1_21();
        List<PacketSnapshot> packetRegistries = new ArrayList<>();
        CompoundBinaryTag dimensionTag = dimension1_21.data();
        for (String registryType : dimensionTag.keySet()) {
            CompoundBinaryTag compoundRegistryType = dimensionTag.getCompound(registryType);
            ListBinaryTag values = compoundRegistryType.getList("value");
            MetadataWriter writer = (message, version) -> {
                message.writeString(registryType);

                message.writeVarInt(values.size());
                for (BinaryTag entry : values) {
                    CompoundBinaryTag entryTag = (CompoundBinaryTag) entry;

                    String name = entryTag.getString("name");
                    CompoundBinaryTag element = entryTag.getCompound("element");

                    message.writeString(name);
                    message.writeBoolean(true);
                    message.writeNamelessCompoundTag(element);
                }
            };

            packetRegistries.add(PacketSnapshot.of(new ClientboundRegistryDataPacket(
                    app.dimensions(),
                    writer
            )));
        }
        PACKETS_REGISTRY_DATA = packetRegistries;
    }
}
