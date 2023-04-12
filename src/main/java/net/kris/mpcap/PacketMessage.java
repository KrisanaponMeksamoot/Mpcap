package net.kris.mpcap;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import net.kris.mpcap.util.PacketViewHelper;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

public class PacketMessage {
    private long when;
    private Packet<?> packet;
    private int num;
    public PacketMessage(Packet<?> packet) {
        this(System.currentTimeMillis(),packet);
    }
    public PacketMessage(long when, Packet<?> packet) {
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
        return String.format("%d [%s] %s",this.num,DateTimeFormatter.ISO_LOCAL_TIME.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(when),ZoneId.systemDefault())),PacketViewHelper.stringView(packet));
    }
}
