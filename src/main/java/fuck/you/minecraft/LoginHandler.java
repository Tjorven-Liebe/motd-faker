package fuck.you.minecraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class LoginHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBufHelper helper = new ByteBufHelper();

        int packetId = helper.readVarInt(msg);

        if (packetId == 0x00) { // Login Start
            String playerName = helper.readString(msg);

            // Send Login Success
            ByteBuf response = ctx.alloc().buffer();
            helper.writeVarInt(response, 0x02); // Packet ID for Login Success
            helper.writeString(response, UUID.randomUUID().toString()); // Player UUID
            helper.writeString(response, playerName); // Player Name

            ctx.writeAndFlush(response);

            // Transition to play state
            ctx.pipeline().addLast(new PlayHandler());
            ctx.pipeline().remove(this);
        }
    }
}