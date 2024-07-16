package fuck.you.minecraft;

import fuck.you.minecraft.codec.PacketDecoder;
import fuck.you.minecraft.codec.PacketEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketDecoder(), new PacketEncoder(), new MinecraftServerHandler());
    }
}
