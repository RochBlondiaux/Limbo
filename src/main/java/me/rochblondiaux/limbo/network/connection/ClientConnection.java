package me.rochblondiaux.limbo.network.connection;

import java.net.SocketAddress;

import org.jetbrains.annotations.NotNull;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.GameProfile;
import me.rochblondiaux.limbo.network.connection.pipeline.PacketDecoder;
import me.rochblondiaux.limbo.network.connection.pipeline.PacketEncoder;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.ConnectionState;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import me.rochblondiaux.limbo.server.LimboServer;

@Getter
@Setter
@Log4j2
public class ClientConnection extends ChannelInboundHandlerAdapter {

    private final Limbo app;
    private final LimboServer server;
    private final Channel channel;
    private final SocketAddress address;
    private final PacketDecoder decoder;
    private final PacketEncoder encoder;

    private ConnectionState state;
    private Version version;
    @Delegate
    private GameProfile profile;

    public ClientConnection(Limbo app, LimboServer server, Channel channel, PacketDecoder decoder, PacketEncoder encoder) {
        this.app = app;
        this.server = server;
        this.channel = channel;
        this.address = channel.remoteAddress();
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext context) throws Exception {
        if (state.equals(ConnectionState.PLAY) || state.equals(ConnectionState.CONFIGURATION))
            this.server.connections().unregister(this);
        super.channelInactive(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        if (channel.isActive())
            log.error("Exception caught: ", cause);
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext context, @NotNull Object msg) {
        this.receivePacket(msg);
    }

    public void receivePacket(Object packet) {
        if (packet instanceof ServerboundPacket serverboundPacket)
            serverboundPacket.handle(this.app, this.server, this);
    }

    public void sendPacket(ClientboundPacket packet) {
        this.ensureConnected();
        this.channel.writeAndFlush(packet, this.channel.voidPromise());
    }

    public void sendPacketAndDisconnect(ClientboundPacket packet) {
        this.ensureConnected();
        this.channel.writeAndFlush(packet).addListener(ChannelFutureListener.CLOSE);
    }

    public void writePacket(ClientboundPacket packet) {
        this.ensureConnected();
        this.channel.write(packet, this.channel.voidPromise());
    }

    public void updateState(ConnectionState state) {
        this.state = state;
        decoder.updateState(state);
        encoder.updateState(state);
    }

    public void updateEncoderVersion(Version version) {
        this.version = version;
        encoder.updateVersion(version);
    }

    public void updateVersion(Version version) {
        this.version = version;
        decoder.updateVersion(version);
        encoder.updateVersion(version);
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void ensureConnected() throws IllegalStateException {
        if (!this.isConnected())
            throw new IllegalStateException("Connection is not active");
    }

    public void disconnect() {
        this.ensureConnected();
        this.channel.close();
    }
}
