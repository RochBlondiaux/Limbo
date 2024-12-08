package me.rochblondiaux.limbo.network.connection;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.network.connection.pipeline.PacketDecoder;
import me.rochblondiaux.limbo.network.connection.pipeline.PacketEncoder;
import me.rochblondiaux.limbo.network.connection.pipeline.VarIntFrameDecoder;
import me.rochblondiaux.limbo.network.connection.pipeline.VarIntLengthEncoder;
import me.rochblondiaux.limbo.server.LimboServer;

@RequiredArgsConstructor
@Log4j2
public class ClientChannelInitializer extends ChannelInitializer<Channel> {

    private final Limbo app;
    private final LimboServer server;

    @Override
    protected void initChannel(Channel channel) {
        log.debug("Initializing channel...");
        ChannelPipeline pipeline = channel.pipeline();

        // Create packet handlers
        PacketDecoder packetDecoder = new PacketDecoder();
        PacketEncoder packetEncoder = new PacketEncoder();

        // Create connection
        ClientConnection connection = new ClientConnection(this.app, this.server, channel, packetDecoder, packetEncoder);

        // Setup pipeline
        pipeline.addLast("timeout", new ReadTimeoutHandler(
                this.app.configuration().readTimeout(),
                TimeUnit.MILLISECONDS
        ));
        pipeline.addLast("frame_decoder", new VarIntFrameDecoder());
        pipeline.addLast("frame_encoder", new VarIntLengthEncoder());
        pipeline.addLast("decoder", packetDecoder);
        pipeline.addLast("encoder", packetEncoder);
        pipeline.addLast("handler", connection);
        log.debug("Channel initialized.");
    }

}
