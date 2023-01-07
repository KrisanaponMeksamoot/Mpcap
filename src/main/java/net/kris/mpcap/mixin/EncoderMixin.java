package net.kris.mpcap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.kris.mpcap.Mpcap;
import net.kris.mpcap.PacketHistory;
import net.kris.mpcap.PacketMessage;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketEncoder;

@Mixin(PacketEncoder.class)
public class EncoderMixin {
    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;Lio/netty/buffer/ByteBuf;)V", at = @At("TAIL"))
    public void encode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo info) {
        PacketHistory ph = Mpcap.getInstance().packetHistory;
        ph.addPacket(new PacketMessage(0, packet));
    }
}
