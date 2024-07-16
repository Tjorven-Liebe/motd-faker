package fuck.you.minecraft;

import de.tjorven.commandhandler.CommandHandler;
import de.tjorven.commandhandler.CommandRegistry;
import fuck.you.minecraft.command.MotdCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MinecraftServer {

    private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static int port = 25565;

    public static void main(String[] args) {
        if (args.length > 1) {
            if (args[0].equals("--port"))
                port = Integer.parseInt(args[1]);
        }

        EXECUTOR.execute(() -> {
            try {
                new MinecraftServer().run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CommandRegistry.registerCommand("motd", new MotdCommand());
        CommandHandler.init();
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer());

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
