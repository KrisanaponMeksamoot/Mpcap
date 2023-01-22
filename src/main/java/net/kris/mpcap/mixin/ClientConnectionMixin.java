package net.kris.mpcap.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.kris.mpcap.ClientConnectionHandler;
import net.kris.mpcap.ConnectEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.Packet;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Inject(method = "<init>(Lnet/minecraft/network/NetworkSide;)V",at = @At("TAIL"))
    public void init(NetworkSide side, CallbackInfo info) {
        ConnectEvent.invoke(new ClientConnectionHandler((ClientConnection)(Object)this));
    }
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V",at = @At("TAIL"))
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        ClientConnectionHandler.invokeReceive((ClientConnection)(Object)this,packet);
    }
    @Inject(method = "sendInternal(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;Lnet/minecraft/network/NetworkState;Lnet/minecraft/network/NetworkState;)V",at = @At(value = "TAIL"))
    private void sendInternal(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback, NetworkState packetState, NetworkState currentState, CallbackInfo info) {
        
    }
}
