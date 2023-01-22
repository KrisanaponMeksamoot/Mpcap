package net.kris.mpcap;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

public class ConnectEvent {
    private static LinkedList<Consumer<ClientConnectionHandler>> listeners = Lists.newLinkedList();
    public static void register(Consumer<ClientConnectionHandler> listener) {
        listeners.add(listener);
    }
    public static void invoke(ClientConnectionHandler connection) {
        for (Consumer<ClientConnectionHandler> listener : listeners) {
            listener.accept(connection);
        }
    }
}
