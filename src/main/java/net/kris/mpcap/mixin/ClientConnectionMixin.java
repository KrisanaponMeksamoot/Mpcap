package net.kris.mpcap.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import net.kris.mpcap.ClientConnectionHandler;
import net.kris.mpcap.event.ConnectEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    private ClientConnectionHandler capHandler;

    @Inject(method = "<init>(Lnet/minecraft/network/NetworkSide;)V",at = @At("TAIL"))
    public void init(NetworkSide side, CallbackInfo info) {
        ConnectEvent.invoke(this.capHandler = new ClientConnectionHandler((ClientConnection)(Object)this));
    }
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",at = @At("TAIL"))
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        this.capHandler.invokeReceive(packet);
    }
    @Inject(method = "sendInternal(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Lnet/minecraft/network/NetworkState;Lnet/minecraft/network/NetworkState;)V",at = @At(value = "TAIL"))
    private void sendInternal(Packet<?> packet, @Nullable PacketCallbacks callbacks, NetworkState packetState, NetworkState currentState, CallbackInfo info) {
        this.capHandler.invokeSend(packet);
    }
}
