package net.kris.mpcap;

import net.minecraft.text.OrderedText;

public class PacketHudLine {
    OrderedText orderedText;
    int num;
    public PacketHudLine(OrderedText orderedText,int num) {
        this.orderedText = orderedText;
        this.num = num;
    }
}
