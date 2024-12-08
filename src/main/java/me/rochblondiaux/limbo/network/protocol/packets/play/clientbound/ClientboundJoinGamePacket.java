package me.rochblondiaux.limbo.network.protocol.packets.play.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.world.DimensionRegistry;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundJoinGamePacket implements ClientboundPacket {

    private int entityId;
    private boolean isHardcore = false;
    private int gameMode = 2;
    private int previousGameMode = -1;
    private String[] worldNames;
    private DimensionRegistry dimensionRegistry;
    private String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance = 2;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean isDebug;
    private boolean isFlat;
    private boolean limitedCrafting;
    private boolean secureProfile;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeInt(entityId);

        if (version.fromTo(Version.V1_7_2, Version.V1_7_6)) {
            msg.writeByte(gameMode == 3 ? 1 : gameMode);
            msg.writeByte(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
        }

        if (version.fromTo(Version.V1_8, Version.V1_9)) {
            msg.writeByte(gameMode);
            msg.writeByte(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_9_1, Version.V1_13_2)) {
            msg.writeByte(gameMode);
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_14, Version.V1_14_4)) {
            msg.writeByte(gameMode);
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_15, Version.V1_15_2)) {
            msg.writeByte(gameMode);
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeLong(hashedSeed);
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
        }

        if (version.fromTo(Version.V1_16, Version.V1_16_1)) {
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.oldCodec());
            msg.writeString(dimensionRegistry.defaultDimension_1_16().name());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
        }

        if (version.fromTo(Version.V1_16_2, Version.V1_17_1)) {
            msg.writeBoolean(isHardcore);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.codec_1_16());
            msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_16().data());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
        }

        if (version.fromTo(Version.V1_18, Version.V1_18_2)) {
            msg.writeBoolean(isHardcore);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeStringsArray(worldNames);
            if (version.moreOrEqual(Version.V1_18_2)) {
                msg.writeCompoundTag(dimensionRegistry.codec_1_18_2());
                msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_18_2().data());
            } else {
                msg.writeCompoundTag(dimensionRegistry.codec_1_16());
                msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_16().data());
            }
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
        }

        if (version.fromTo(Version.V1_19, Version.V1_19_4)) {
            msg.writeBoolean(isHardcore);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeStringsArray(worldNames);
            if (version.moreOrEqual(Version.V1_19_1)) {
                if (version.moreOrEqual(Version.V1_19_4)) {
                    msg.writeCompoundTag(dimensionRegistry.codec_1_19_4());
                } else {
                    msg.writeCompoundTag(dimensionRegistry.codec_1_19_1());
                }
            } else {
                msg.writeCompoundTag(dimensionRegistry.codec_1_19());
            }
            msg.writeString(worldName); // World type
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
            msg.writeBoolean(false);
        }

        if (version.equals(Version.V1_20)) {
            msg.writeBoolean(isHardcore);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.codec_1_20());
            msg.writeString(worldName); // World type
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
        }

        if (version.fromTo(Version.V1_20_2, Version.V1_20_3)) {
            msg.writeBoolean(isHardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeString(worldName);
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
        }

        if (version.moreOrEqual(Version.V1_20_5)) {
            msg.writeBoolean(isHardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeVarInt(dimensionRegistry.dimension_1_20_5().id());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode);
            msg.writeByte(previousGameMode);
            msg.writeBoolean(isDebug);
            msg.writeBoolean(isFlat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
            msg.writeBoolean(secureProfile);
        }
    }
}
