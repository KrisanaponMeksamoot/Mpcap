package net.kris.mpcap.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.kris.mpcap.Mpcap;
import net.kris.mpcap.PackageHistory;
import net.kris.mpcap.PacketMessage;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.Packet;

@Mixin(DecoderHandler.class)
public class DecoderMixin {
    @Inject(method = "decode(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V", at = @At("TAIL"))
    public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects, CallbackInfo info) {
        PackageHistory ph = Mpcap.getInstance().packageHistory;
        ph.addPacket(new PacketMessage(0, (Packet<?>) objects.get(objects.size()-1)));
    }
}
