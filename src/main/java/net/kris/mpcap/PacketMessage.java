package net.kris.mpcap;

import net.minecraft.network.Packet;
import net.minecraft.text.Text;

public class PacketMessage {
    private int when;
    private Packet<?> packet;
    private int num;
    public PacketMessage(int when, Packet<?> packet) {
        this.when = when;
        this.packet = packet;
    }
    public int getWhen() {
        return this.when;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return this.num;
    }
    public Packet<?> getPacket() {
        return this.packet;
    }
    public Text getText() {
        return Text.of(toString());
    }
}
