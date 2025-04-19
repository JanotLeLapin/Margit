package dev.rocco.kig.paper.impl.cheetah;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.server.*;

import java.io.IOException;

public class SinkPlayerConnection extends PlayerConnection {
    public SinkPlayerConnection(MinecraftServer minecraftserver, SinkEntityPlayer entityplayer) {
        super(minecraftserver, null, entityplayer);
    }

    @Override
    public void sendPacket(Packet packet) {
        // Don't send packet to non existing connection, instead serialize it in place and invoke the callback.
        ByteBuf direct = Unpooled.directBuffer(), withLength = null;
        Integer id = EnumProtocol.PLAY.a(EnumProtocolDirection.CLIENTBOUND, packet);
        if (id == null) return;
        try {
            PacketDataSerializer serializer = new PacketDataSerializer(direct);
            serializer.b(id); // write packet ID
            packet.b(serializer);
            // Prepend packet length
            withLength = Unpooled.directBuffer();
            withLength.markReaderIndex();
            int packetSize = direct.readableBytes();
            int packetSizeVarInt = PacketDataSerializer.a(packetSize);
            if (packetSizeVarInt > 3) {
                throw new IllegalArgumentException("unable to fit " + packetSize + " into " + 3);
            } else {
                PacketDataSerializer newSerializer = new PacketDataSerializer(withLength);
                newSerializer.ensureWritable(packetSizeVarInt + packetSize);
                newSerializer.b(packetSize);
                newSerializer.writeBytes(direct, direct.readerIndex(), packetSize);
            }
            withLength.resetReaderIndex();
            ((SinkEntityPlayer) player).getPacketConsumer().accept(withLength.nioBuffer());
        } catch (Exception ex) {
            MinecraftServer.LOGGER.error("Error sending packet to sink entity", ex);
        } finally {
            ReferenceCountUtil.release(direct);
            if (withLength != null) ReferenceCountUtil.release(withLength);
        }
    }
}
