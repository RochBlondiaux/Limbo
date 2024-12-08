package me.rochblondiaux.limbo.network.connection.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.network.protocol.model.*;
import me.rochblondiaux.limbo.network.protocol.registry.PacketRegistry;

@Getter
@Log4j2
public class PacketEncoder extends MessageToByteEncoder<ClientboundPacket> {

    private PacketRegistry registry;
    private Version version;

    public PacketEncoder() {
        updateVersion(Version.max());
        updateState(ConnectionState.HANDSHAKE);
    }

    @Override
    protected void encode(ChannelHandlerContext context, ClientboundPacket packet, ByteBuf output) {
        if (this.registry == null)
            return;

        // Wrap the output buffer
        ByteMessage message = new ByteMessage(output);

        // Retrieve the packet ID
        int packetId = this.registry.getPacketId(packet instanceof PacketSnapshot snapshot ? snapshot.packet().getClass() : packet.getClass());
        if (packetId == -1) {
            log.error("Unknown packet: {}[0x{}] ({} bytes)", packet.getClass().getSimpleName(), Integer.toHexString(packetId), output.readableBytes());
            return;
        }

        // Write the packet ID
        message.writeVarInt(packetId);

        // Write the packet
        try {
            packet.encode(message, this.version);

            log.debug("Sending {}[0x{}] packet with {} bytes", packet.getClass().getSimpleName(), Integer.toHexString(packetId), output.readableBytes());
        } catch (Exception e) {
            log.error("Error while encoding packet: {}[0x{}]", packet.getClass().getSimpleName(), Integer.toHexString(packetId), e);
        }

    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(ConnectionState state) {
        this.registry = state.clientBound().getRegistry(version);
    }
}
