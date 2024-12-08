package me.rochblondiaux.limbo.network.connection.pipeline;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.network.protocol.model.*;
import me.rochblondiaux.limbo.network.protocol.registry.PacketRegistry;

@Getter
@Log4j2
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private PacketRegistry registry;
    private Version version;

    public PacketDecoder() {
        updateVersion(Version.min());
        updateState(ConnectionState.HANDSHAKE);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> output) {
        if (!context.channel().isActive() || this.registry == null)
            return;

        // Wrap the buffer into a ByteMessage
        ByteMessage msg = new ByteMessage(buffer);

        // Retrieve the packet
        int packetId = msg.readVarInt();
        Packet packet = this.registry.getPacket(packetId);
        if (packet == null) {
            log.warn("Unknown incoming packet: 0x{}", Integer.toHexString(packetId));
            return;
        }

        // Make sure packet is a ServerboundPacket
        if (!(packet instanceof ServerboundPacket serverboundPacket)) {
            log.warn("Unexpected packet type: {}[0x{}]", packet.getClass().getSimpleName(), Integer.toHexString(packetId));
            return;
        }

        log.debug("Received packet: {}[0x{}] ({} bytes}", packet.getClass().getSimpleName(), Integer.toHexString(packetId), msg.readableBytes());

        // Decode the packet
        try {
            serverboundPacket.decode(msg, this.version);
        } catch (Exception e) {
            log.error("Error while decoding packet: {}[0x{}]", packet.getClass().getSimpleName(), Integer.toHexString(packetId), e);
        }

        context.fireChannelRead(serverboundPacket);
    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(ConnectionState state) {
        this.registry = state.serverBound().getRegistry(version);
    }
}
