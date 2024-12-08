package me.rochblondiaux.limbo.network.connection.pipeline;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class VarIntFrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) {
            in.clear();
            return;
        }

        VarIntByteDecoder reader = new VarIntByteDecoder();
        int varIntEnd = in.forEachByte(reader);

        if (varIntEnd == -1) return;

        if (reader.result() == VarIntByteDecoder.DecodeResult.SUCCESS) {
            int readVarInt = reader.readVarInt();
            int bytesRead = reader.bytesRead();
            if (readVarInt < 0) {
                log.error("[VarIntFrameDecoder] Bad data length");
            } else if (readVarInt == 0) {
                in.readerIndex(varIntEnd + 1);
            } else {
                int minimumRead = bytesRead + readVarInt;

                if (in.isReadable(minimumRead)) {
                    out.add(in.retainedSlice(varIntEnd + 1, readVarInt));
                    in.skipBytes(minimumRead);
                }
            }
        } else if (reader.result() == VarIntByteDecoder.DecodeResult.TOO_BIG) {
            log.error("[VarIntFrameDecoder] Too big data");
        }
    }
}