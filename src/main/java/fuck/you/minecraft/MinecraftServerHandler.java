package fuck.you.minecraft;

import fuck.you.minecraft.motd.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MinecraftServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBufHelper helper = new ByteBufHelper();

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
                if (protocolVersion >= 736) { // Minecraft 1.16 and later
                    motd = MOTD1_16.builder()
                            .description("Welcome to the server!\nDu noob")
                            .players(PlayerInfo.builder().max(100).online(5).build())
                            .favicon(Image64Converter.convertToBase64("minecraft_22400.png"))
//                            .favicon("data:image/png;base64,iVBORw0KGgloAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAABLRJREFUeJztm09MHFUcx79vZnb5t4Q/K5RWLdvEApFQkx6ImNgm7Qk1aTSpHkwaD0QvJKYlHnXlqIm9yLF681I8NDWRgwkJHjSYmlQoKX8MXagiLOzyb5eF2Zn38yAmOu8t+9g/syzMJ9nLb9783vd9d2ben5kHeHh4nGTYoUqHYbS1Bi8Z9cZVGOwC01mIgCbGqBZgfgDafs7D5VWH9n8cIBNgW+BYI6In3KIJc90anV+I/YhBWKoJ1YSG4e/obO7XA/qAVsnOMFas9uUHEYHv0p92wr49PRUdwiDMbOdkbUnoTrC9+pR/2AhoXYWR6Q5Wkk/sLJtvR/piMweVO9CA8980dlc0VYxoPq2xsPLcgad5fG91r3fu3fgvmcpkNCB0J9geOOv/qVwb/y+2ydeTT82eTFeCJj0rDH91i+9uuTceAHS/1lDd4h9GGH7ZcakBHZ3N/UaNfqG40tzDqNG6Ojqb+2XHRAPuQtcD+q2iq3IZPaAPIAzDGRcMaEsEL2uV7Fl3ZLmHVqmdaQsFLwtxIdBgXDmq/Xw+MAYYdcYVZ1y8JHT2kkrCUPULuFjXrSxgbO0HxNKrSmUNZuBayzvKuRdS83iw8XP2gj4mPNcEA5jOQiqVXqzrRrj9c5WiAID3dt5EbFPNAL9WgU/aP1PO/d3yt0oGMMbOOWPCLUBAk3LNZQYxPOOMCQb8M7E5nsjaJhkHMOmA4Xggtk02EJKPDo8DBN0ZkjX2+PWB+zAmtu1EGSDj+F7uingGlFpAqTnxBggPvM7vT5Neld2XoK8JZ6tDyhW93vwWnqtSK5/me/hqcQiMqf0/cXMNC6n5rOV4iuPRa3/9r83CXECVWHpVeWwPAB+03kJP46tKZbetLfw6OQ6dCd12wTnxt4BnQKkFlJqcnwGZIKJMRw6bSZqr0KtVBTfg/dYPce20uJrT5D+lnKNGD2Dk5XEhPpd4jJtTfXnpc1JwA+p9DXi+qjWvHBrTpDk20+t55ZXWVfCMZYZnQKkFlBrPgFILKDWeAaUWUGo8A0otoNTIRoKEIqwMzyfnsGMnlcpqTMOLte58n+GaAYMzH+Hh1gOlspVaJcYv/V5oCSASZ2SyW4AXvOajAoPtDEkMoKwfF5YvYtvE1+PEtt0R4z6ytomvxwlr7shxHwYIq7jCQ5CIngDozLWSR9u/4f7ysBCPp2PKOWyypTn+SC3mKgsAQJwizpjYC6RpAsAbuVYyEr2Hkei9XE/fl5DGx9M388qRIfGEMyTcAtamNZpxWa+MISJYm9aoMy4YMBuJjfFdvuSOLPfgu7Q0G4mNOeNiNzgIy0rYt11R5SJ2wv5CtpFCOheYmYp+aSX5ZPFluYOV4JPTU9Eh2TH5ZGgQ5s6yed02eeGXYV3GNvn6zop5PdPukYyzwUhfbMZc2evlaR4vnrziYpt83VzhvQftGjlwOjx3Iz6eWDRfsZJc6D6OOlaSTyafmj1zN1bENyz/Iev75437qdjq+cTXDTVVG9BYFzNQe1Q/piYC+C5fsjasTx8/XOnbGEhFs52T07Y5rd64qvlYF9PYOdm2OVakL83IsW2OwLYZYZVsisCiCWuTRmcj0bHDbJvz8PA42fwNX06TGRCC14MAAAAASUVORK5CYII=")
                            .enforcesSecureChat(true)
                            .previewsChat(true)
                            .version(Version.builder().name("1.16.5").protocol(754).build())
                            .build().toString();
                    System.out.println(motd);
                } else if (protocolVersion >= 5) { // Minecraft 1.7 to 1.15
                    motd = MOTD1_7.builder()
                            .description("Welcome to the server!")
                            .players(PlayerInfo.builder().max(100).online(5).sample(new SampleInfo[0]).build())
                            .favicon(Image64Converter.convertToBase64("icon1.png"))
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
