package fuck.you.minecraft;

import de.tjorven.commandhandler.CommandHandler;
import fuck.you.minecraft.motd.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MinecraftServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    ByteBufHelper helper = new ByteBufHelper();
    public static String imageToUse = "icon1.png";

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.readableBytes() < 1) {
            return;
        }

        int packetLength = helper.readVarInt(msg);

        if (msg.readableBytes() < packetLength) {
            return;
        }

        int packetId = helper.readVarInt(msg);
        System.out.println("Received packet with ID: " + packetId);

        if (packetId == 0x00) { // Handshake packet
            int protocolVersion = helper.readVarInt(msg);
            String address = helper.readString(msg);
            int port = msg.readUnsignedShort();
            int nextState = helper.readVarInt(msg);
            System.out.println("Received Handshake: " + protocolVersion + ", " + address + ", " + port + ", " + nextState);
            if (nextState == 1) { // Status
                ByteBuf response = ctx.alloc().buffer();
                helper.writeVarInt(response, 0x00); // Packet ID for Status Response

                String motd;
                System.out.println(imageToUse);
                if (protocolVersion >= 736) { // Minecraft 1.16 and later
                    motd = MOTD1_16.builder()
                            .description("Welcome to the server!\nDu noob")
                            .players(PlayerInfo.builder().max(100).online(5).build())
                            .favicon(Image64Converter.convertToBase64(imageToUse))
                            .enforcesSecureChat(true)
                            .previewsChat(true)
                            .version(Version.builder().name("1.16.5").protocol(754).build())
                            .build().toString();
                    System.out.println(motd);
                } else if (protocolVersion >= 5) { // Minecraft 1.7 to 1.15
                    motd = MOTD1_7.builder()
                            .description("Welcome to the server!")
                            .players(PlayerInfo.builder().max(100).online(5).sample(new SampleInfo[0]).build())
                            .favicon(Image64Converter.convertToBase64(imageToUse))
                            .version(Version.builder().name("1.15.2").protocol(578).build())
                            .build().toString();
                } else {
                    motd = MOTD_CLASSIC.builder()
                            .description("Hello, world!")
                            .players(PlayerInfo.builder().max(100).online(50).build())
                            .version(Version.builder().name("1.16.5").protocol(754).build())
                            .build().toString();
                }
                helper.writeString(response, motd);
                // Create the final response buffer
                ByteBuf finalResponse = ctx.alloc().buffer();
                helper.writeVarInt(finalResponse, response.readableBytes()); // Write the length of the response
                finalResponse.writeBytes(response); // Write the response itself

                ctx.writeAndFlush(finalResponse);
            } else if (nextState == 2) {
                ctx.pipeline().addLast(new LoginHandler());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
