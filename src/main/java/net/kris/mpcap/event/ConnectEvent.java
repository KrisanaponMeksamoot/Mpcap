package net.kris.mpcap.event;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.kris.mpcap.ClientConnectionHandler;

public class ConnectEvent {
    private static LinkedList<Consumer<ClientConnectionHandler>> listeners = Lists.newLinkedList();
    public static void register(Consumer<ClientConnectionHandler> listener) {
        if (listener == null)
            throw new NullPointerException("listener must not be null");
        listeners.add(listener);
    }
    public static void invoke(ClientConnectionHandler connection) {
        for (Consumer<ClientConnectionHandler> listener : listeners) {
            listener.accept(connection);
        }
    }
}
