package fuck.you.minecraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("fuck.you.minecraft.PlayHandler received message: " + msg);
        // Handle play state packets here
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
