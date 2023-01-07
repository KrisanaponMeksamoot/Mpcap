package net.kris.mpcap;

import java.util.Deque;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class PacketHistory extends DrawableHelper {
    private final List<PacketMessage> messages = Lists.newArrayList();
    private final List<ChatHudLine<OrderedText>> visibleMessages = Lists.newArrayList();
    private final Deque<PacketMessage> packetQueue = Queues.newArrayDeque();
    int scrolledLines = 0;
    int selected = -1;
    int pageSize = 10;
    boolean autoScroll = true;
    private MinecraftClient client;
    public PacketHistory(MinecraftClient client) {
        this.client = client;
    }
    public void scroll(int amount) {
        scrolledLines += amount;
        if (scrolledLines<0) {
            scrolledLines = 0;
        } else if (scrolledLines>Math.max(visibleMessages.size(),0)) {
            toButtom();
        }
    }
    public void toTop() {
        scrolledLines = 0;
    }
    public void toButtom() {
        scrolledLines = Math.max(visibleMessages.size(),0);
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
        boolean bl = this.isChatFocused();
        float scale = (float)this.getChatScale();
        int k = MathHelper.ceil((float)this.getWidth() / scale);
        matrices.push();
        matrices.translate(4.0, 8.0, 0.0);
        matrices.scale(scale, scale, 1.0f);
        double d = this.client.options.getChtOpacity().getValue() * (double)0.9f + (double)0.1f;
        double textBackgroundOpacity = this.client.options.getTextBackgroundOpacity().getValue();
        double lineSpace = this.client.options.getChatLineSpacing().getValue();
        double h = 9.0 * (lineSpace + 1.0);
        double l = -8.0 * (lineSpace + 1.0) + 4.0 * lineSpace;
        int m = 0;
        for (n = 0; n + this.scrolledLines < this.visibleMessages.size() && n < i; n++) {
            ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(n + this.scrolledLines);
            if (chatHudLine == null || (o = tickDelta - chatHudLine.getCreationTick()) >= 200 && !bl) continue;
            double p = 1.0;
            q = (int)(255.0 * p * d);
            r = (int)(255.0 * p * textBackgroundOpacity);
            ++m;
            if (q <= 3) continue;
            double t = (double)(-n) * h;
            matrices.push();
            matrices.translate(0.0, 0.0, 50.0);
            fill(matrices, -4, (int)(t - h), 0 + k + 4, (int)t, r << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0, 0.0, 50.0);
            this.client.textRenderer.drawWithShadow(matrices, chatHudLine.getText(), 0.0f, (float)((int)(t + l)), 0xFFFFFF + (q << 24));
            RenderSystem.disableBlend();
            matrices.pop();
        }
        if (!this.packetQueue.isEmpty()) {
            n = (int)(128.0 * d);
            int u = (int)(255.0 * textBackgroundOpacity);
            matrices.push();
            matrices.translate(0.0, 0.0, 50.0);
            fill(matrices, -2, 0, k + 4, 9, u << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0, 0.0, 50.0);
            this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", this.packetQueue.size()), 0.0f, 1.0f, 0xFFFFFF + (n << 24));
            matrices.pop();
            RenderSystem.disableBlend();
        }
        if (bl) {
            n = this.client.textRenderer.fontHeight;
            int u = j * n;
            o = m * n;
            int v = this.scrolledLines * o / j;
            int w = o * o / u;
            if (u != o) {
                q = v > 0 ? 170 : 96;
                r = 0x3333AA;
                matrices.translate(-4.0, 0.0, 0.0);
                fill(matrices, 0, -v, 2, -v - w, r + (q << 24));
                fill(matrices, 2, -v, 1, -v - w, 0xCCCCCC + (q << 24));
            }
        }
        matrices.pop();
    }
    public void addPacket(PacketMessage packetMessage) {
        int i = MathHelper.floor((double)this.getWidth() / this.getChatScale());
        List<OrderedText> list = ChatMessages.breakRenderedChatMessageLines(packetMessage.getText(), i, this.client.textRenderer);
        for (OrderedText orderedText : list) {
            if (this.autoScroll) {
                this.scroll(1);
            }
            this.visibleMessages.add(new ChatHudLine<OrderedText>(packetMessage.getNum(), orderedText, packetMessage.getNum()));
        }
        packetMessage.setNum(this.messages.size());
        this.messages.add(packetMessage);
    }
    
    public int getVisibleLineCount() {
        return this.getHeight() / 9;
    }

    @Nullable
    public PacketsViewScreen getChatScreen() {
        Screen screen = this.client.currentScreen;
        if (screen instanceof PacketsViewScreen) {
            PacketsViewScreen pvScreen = (PacketsViewScreen)screen;
            return pvScreen;
        }
        return null;
    }

    private boolean isChatFocused() {
        return this.getChatScreen() != null;
    }

    public int getWidth() {
        return ChatHud.getWidth(this.client.options.getChatWidth().getValue());
    }

    public double getChatScale() {
        return this.client.options.getChatScale().getValue();
    }

    public int getHeight() {
        return ChatHud.getHeight(this.client.options.getChatHeightFocused().getValue() / (this.client.options.getChatLineSpacing().getValue() + 1.0));
    }
}
