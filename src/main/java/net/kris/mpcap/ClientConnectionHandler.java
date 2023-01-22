package net.kris.mpcap;

import java.util.HashMap;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import io.netty.channel.Channel;
import net.kris.mpcap.mixin.ClientConnectionAccessor;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;

public class ClientConnectionHandler {
    private static final HashMap<ClientConnection,ClientConnectionHandler> handlers = Maps.newHashMap();
    private ClientConnection connection;
    private Channel channel;
    private Consumer<Packet<?>> receiveHandler;
    private Consumer<Packet<?>> sendHandler;
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
    public static void invokeSend(ClientConnection connection, Packet<?> packet) {
        handlers.get(connection).sendHandler.accept(packet);
    }
    public static void invokeReceive(ClientConnection connection, Packet<?> packet) {
        handlers.get(connection).receiveHandler.accept(packet);
    }
}
