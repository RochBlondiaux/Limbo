package me.rochblondiaux.limbo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.connection.ConnectionManager;
import me.rochblondiaux.limbo.network.PacketHandler;
import me.rochblondiaux.limbo.network.connection.ClientChannelInitializer;
import me.rochblondiaux.limbo.network.protocol.model.Version;

@Log4j2
@Getter
public class LimboServer {

    private final Limbo app;
    private final Version version;
    private final ConnectionManager connections;

    private PacketHandler packetHandler;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public LimboServer(Limbo app) {
        this.app = app;
        this.version = Version.V1_21_3; // TODO: Add version configuration
        this.connections = new ConnectionManager();
    }

    public void start() {
        log.info("Starting server on {}:{}... (Version: {})", this.app.configuration().host(), this.app.configuration().port(), this.version.prettyName());

        // Disable resource leak detection
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        // Packet handler
        this.packetHandler = new PacketHandler(this.app, this);

        // Bootstrap
        this.startBootstrap();

        // Garbage collector
        System.gc();

        log.info("Server started");
    }

    private void startBootstrap() {
        Class<? extends ServerChannel> channelClass;

        // TODO: Add configuration for boss and worker group sizes
        int bossGroupSize = 1;
        int workerGroupSize = 4;

        if (Epoll.isAvailable()) {
            this.bossGroup = new EpollEventLoopGroup(bossGroupSize);
            this.workerGroup = new EpollEventLoopGroup(workerGroupSize);
            channelClass = EpollServerSocketChannel.class;
            log.info("Using Epoll transport type");
        } else {
            this.bossGroup = new NioEventLoopGroup(bossGroupSize);
            this.workerGroup = new NioEventLoopGroup(workerGroupSize);
            channelClass = NioServerSocketChannel.class;
            log.info("Using Java NIO transport type");
        }

        new ServerBootstrap()
                .group(this.bossGroup, this.workerGroup)
                .channel(channelClass)
                .childHandler(new ClientChannelInitializer(this.app, this))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(this.app.configuration().host(), this.app.configuration().port())
                .bind();
    }

    public void stop() {
        log.info("Stopping server...");

        if (this.bossGroup != null)
            this.bossGroup.shutdownGracefully();

        if (this.workerGroup != null)
            this.workerGroup.shutdownGracefully();

        log.info("Server stopped");
    }
}
