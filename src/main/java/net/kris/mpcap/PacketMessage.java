package net.kris.mpcap;

import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.Text;

public class PacketMessage {
    private int when;
    private int num;
    private Packet<PacketListener> packet;
    public PacketMessage(int when, Packet<PacketListener> packet, int num) {
        this.when = when;
        this.packet = packet;
        this.num = num;
    }
    public int getWhen() {
        return this.when;
    }
    public int getNum() {
        return this.num;
    }
    public Packet<PacketListener> getPacket() {
        return this.packet;
    }
    public Text getText() {
        return Text.of(toString());
    }
}
