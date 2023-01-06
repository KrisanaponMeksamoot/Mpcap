package net.kris.mpcap;

import java.util.Deque;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class PackageHistory extends DrawableHelper {
    List<Packet<PacketListener>> packets = Lists.newArrayList();
    private final List<ChatHudLine<Text>> messages = Lists.newArrayList();
    private final List<ChatHudLine<OrderedText>> visibleMessages = Lists.newArrayList();
    private final Deque<Packet<PacketListener>> packetQueue = Queues.newArrayDeque();
    int scrolledLines = 0;
    int selected = -1;
    int pageSize = 10;
    boolean autoScroll = true;
    private MinecraftClient client;
    public PackageHistory(MinecraftClient client) {
        this.client = client;
    }
    public void scroll(int amount) {
        scrolledLines += amount;
        if (scrolledLines<0) {
            scrolledLines = 0;
        } else if (scrolledLines>Math.max(visibleMessages.size()-pageSize,0)) {
            toButtom();
        }
    }
    public void toTop() {
        scrolledLines = 0;
    }
    public void toButtom() {
        scrolledLines = Math.max(visibleMessages.size()-pageSize,0);
    }
    public void render(MatrixStack matrices, int tickDelta) {
        int r;
        int q;
        int o;
        int n;
        int i = this.getVisibleLineCount();
        int j = this.visibleMessages.size();
        if (j <= 0) {
            return;
        }
        float f = (float)this.getChatScale();
        int k = MathHelper.ceil((float)this.getWidth() / f);
        matrices.push();
        matrices.translate(4.0, 8.0, 0.0);
        matrices.scale(f, f, 1.0f);
        double d = this.client.options.getChtOpacity().getValue() * (double)0.9f + (double)0.1f;
        double e = this.client.options.getTextBackgroundOpacity().getValue();
        double g = this.client.options.getChatLineSpacing().getValue();
        double h = 9.0 * (g + 1.0);
        double l = -8.0 * (g + 1.0) + 4.0 * g;
        int m = 0;
        for (n = 0; n + this.scrolledLines < this.visibleMessages.size() && n < i; ++n) {
            ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(n + this.scrolledLines);
            if (chatHudLine == null) continue;
            double p = 1.0;
            q = (int)(255.0 * p * d);
            r = (int)(255.0 * p * e);
            ++m;
            if (q <= 3) continue;
            double t = (double)(-n) * h;
            matrices.push();
            matrices.translate(0.0, 0.0, 50.0);
            ChatHud.fill(matrices, -4, (int)(t - h), 0 + k + 4, (int)t, r << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0, 0.0, 50.0);
            this.client.textRenderer.drawWithShadow(matrices, chatHudLine.getText(), 0.0f, (float)((int)(t + l)), 0xFFFFFF + (q << 24));
            RenderSystem.disableBlend();
            matrices.pop();
        }
        if (!this.packetQueue.isEmpty()) {
            n = (int)(128.0 * d);
            int u = (int)(255.0 * e);
            matrices.push();
            matrices.translate(0.0, 0.0, 50.0);
            ChatHud.fill(matrices, -2, 0, k + 4, 9, u << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0, 0.0, 50.0);
            this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", this.packetQueue.size()), 0.0f, 1.0f, 0xFFFFFF + (n << 24));
            matrices.pop();
            RenderSystem.disableBlend();
        }
        {
            n = this.client.textRenderer.fontHeight;
            int u = j * n;
            o = m * n;
            int v = this.scrolledLines * o / j;
            int w = o * o / u;
            if (u != o) {
                q = v > 0 ? 170 : 96;
                r = 0x3333AA;
                matrices.translate(-4.0, 0.0, 0.0);
                ChatHud.fill(matrices, 0, -v, 2, -v - w, r + (q << 24));
                ChatHud.fill(matrices, 2, -v, 1, -v - w, 0xCCCCCC + (q << 24));
            }
        }
        matrices.pop();
    }
    public void addPacket(Packet<PacketListener> packet, int packetNum, int timestamp) {
        Text message = Text.of(packet.toString());
        if (packetNum != 0) {
            this.removeMessage(packetNum);
        }
        int i = MathHelper.floor((double)this.getWidth() / this.getChatScale());
        List<OrderedText> list = ChatMessages.breakRenderedChatMessageLines(message, i, this.client.textRenderer);
        for (OrderedText orderedText : list) {
            if (this.autoScroll) {
                this.scroll(1);
            }
            this.visibleMessages.add(0, new ChatHudLine<OrderedText>(timestamp, orderedText, packetNum));
        }
        this.messages.add(0, new ChatHudLine<Text>(timestamp, message, packetNum));
    }
    
    private double getChatScale() {
        return 0;
    }
    private void removeMessage(int messageId) {
    }
    public int getVisibleLineCount() {
        return this.getHeight() / 9;
    }
    public int getWidth() {
        return ChatHud.getWidth(this.client.options.getChatWidth().getValue());
    }
    public int getHeight() {
        return ChatHud.getHeight(this.client.options.getChatHeightFocused().getValue() / (this.client.options.getChatLineSpacing().getValue() + 1.0));
    }
}
