package net.kris.mpcap;

import java.util.HashSet;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import io.netty.channel.Channel;
import net.kris.mpcap.mixin.ClientConnectionAccessor;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;

public class ClientConnectionHandler {
    private ClientConnection connection;
    private Channel channel;
    private HashSet<Consumer<Packet<?>>> receiveHandlers = Sets.newHashSet();
    private HashSet<Consumer<Packet<?>>> sendHandlers = Sets.newHashSet();
    public ClientConnectionHandler(ClientConnection connection) {
        this.connection = connection;
        this.channel = ((ClientConnectionAccessor)connection).getChannel();
    }
    public ClientConnection getClientConnection() {
        return connection;
    }
    public Channel getChannel() {
        return channel;
    }
    public void addReceiveHandler(Consumer<Packet<?>> handler) {
        if (handler == null)
            throw new NullPointerException("receive handler must not be null");
        this.receiveHandlers.add(handler);
    }
    public void addSendHandler(Consumer<Packet<?>> handler) {
        if (handler == null)
            throw new NullPointerException("send handler must not be null");
        this.sendHandlers.add(handler);
    }
    public void removeReceiveHandler(Consumer<Packet<?>> handler) {
        this.receiveHandlers.remove(handler);
    }
    public void removeSendHandler(Consumer<Packet<?>> handler) {
        this.sendHandlers.remove(handler);
    }
    public void invokeSend(Packet<?> packet) {
        for (Consumer<Packet<?>> handler : sendHandlers)
            handler.accept(packet);
    }
    public void invokeReceive(Packet<?> packet) {
        for (Consumer<Packet<?>> handler : receiveHandlers)
            handler.accept(packet);
    }
}
