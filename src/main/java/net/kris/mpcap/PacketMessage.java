package net.kris.mpcap;


import java.time.format.DateTimeFormatter;
import java.util.Date;

import net.minecraft.network.Packet;
import net.minecraft.text.Text;

public class PacketMessage {
    private long when;
    private Packet<?> packet;
    private int num;
    public PacketMessage(Packet<?> packet) {
        this.when = System.currentTimeMillis();
        this.packet = packet;
    }
    public PacketMessage(int when, Packet<?> packet) {
        this.when = when;
        this.packet = packet;
    }
    public long getWhen() {
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
    @Override
    public String toString() {
        return String.format("[%s] %s",DateTimeFormatter.ISO_LOCAL_TIME.format(new Date(when).toInstant()),packet.toString());
    }
}
