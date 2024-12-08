package me.rochblondiaux.limbo.network.protocol.packets.play.clientbound;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundDeclareCommandsPacket implements ClientboundPacket {

    private Set<String> commands;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(commands.size() * 2 + 1); // +1 because declaring root node

        // Declare root node
        msg.writeByte(0);
        msg.writeVarInt(commands.size());

        for (int i = 1; i <= commands.size() * 2; i++) {
            msg.writeVarInt(i++);
        }

        // Declare other commands
        int i = 1;
        for (String cmd : commands) {
            msg.writeByte(1 | 0x04);
            msg.writeVarInt(1);
            msg.writeVarInt(i + 1);
            msg.writeString(cmd);
            i++;

            msg.writeByte(2 | 0x04 | 0x10);
            msg.writeVarInt(1);
            msg.writeVarInt(i);
            msg.writeString("arg");
            msg.writeString("brigadier:string");
            msg.writeVarInt(0);
            msg.writeString("minecraft:ask_server");
            i++;
        }

        msg.writeVarInt(0);
    }
}
