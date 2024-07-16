package fuck.you.minecraft;

import io.netty.buffer.ByteBuf;

public class ByteBufHelper {


    public void writeString(ByteBuf buf, String string) {
        byte[] bytes = string.getBytes();
        writeVarInt(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    public String readString(ByteBuf buf) {
        int length = readVarInt(buf);
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    public void writeVarInt(ByteBuf buf, int value) {
        while ((value & -128) != 0) {
            buf.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        buf.writeByte(value);
    }

    public int readVarInt(ByteBuf buf) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (currentByte & 127) << position;

            if ((currentByte & 128) == 0) {
                break;
            }

            position += 7;

            if (position >= 32) {
                throw new RuntimeException("VarInt is too big");
            }
        }

        return value;
    }
}
