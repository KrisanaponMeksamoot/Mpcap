package net.kris.mpcap;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class PacketsViewScreen extends Screen {
    public static final Text CLEAR = Text.translatable("gui.mpcap.clear");

    private Mpcap mpcap;

    public PacketsViewScreen(Mpcap mpcap) {
        super(Text.translatable("packets_view_screen.title"));
        this.mpcap = mpcap;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(2, 2, 200, 20, CLEAR, button -> {
            this.mpcap.packetHistory.clear();
        }));
    }
    
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.setScreen(null);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.mpcap.packetHistory.scroll(1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.mpcap.packetHistory.scroll(-1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            this.mpcap.packetHistory.toTop();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            this.mpcap.packetHistory.toButtom();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.mpcap.packetHistory.scroll((int)amount);
        return true;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // fill(matrices, 2, 2, this.width - 2, 14, this.client.options.getTextBackgroundColor(Integer.MIN_VALUE));
        //renderPacketsMessage(matrices);
        mpcap.packetHistory.render(matrices, (int)delta);
        matrices.push();
        matrices.translate(0.0, 0.0, 100.0);
        super.render(matrices, mouseX, mouseY, delta);
        matrices.pop();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void renderPacketsMessage(MatrixStack matrices) {
        int i = (int)(255.0 * (this.client.options.getChtOpacity().getValue() * (double)0.9f + (double)0.1f));
        int j = (int)(255.0 * this.client.options.getTextBackgroundOpacity().getValue());
        int k = this.getPreviewWidth();
        List<OrderedText> list = this.getPreviewText();
        int l = this.getPreviewHeight(list);
        RenderSystem.enableBlend();
        matrices.push();
        matrices.translate(this.getPreviewLeft(), this.getPreviewTop(l), 0.0);
        fill(matrices, 0, 0, k, l, j << 24);
        matrices.translate(2.0, 2.0, 0.0);
        for (int m = 0; m < list.size(); ++m) {
            OrderedText orderedText = list.get(m);
            this.client.textRenderer.drawWithShadow(matrices, orderedText, 0.0f, (float)(m * this.textRenderer.fontHeight), i << 24 | 0xFFFFFF);
        }
        matrices.pop();
        RenderSystem.disableBlend();
    }

    private int getPreviewWidth() {
        return this.client.currentScreen.width - 4;
    }

    private int getPreviewHeight(List<OrderedText> lines) {
        return Math.max(lines.size(), 1) * this.textRenderer.fontHeight + 4;
    }

    private int getPreviewBottom() {
        return this.client.currentScreen.height - 15;
    }

    private int getPreviewTop(int previewHeight) {
        return this.getPreviewBottom() - previewHeight;
    }

    private int getPreviewLeft() {
        return 2;
    }

    private List<OrderedText> getPreviewText() {
        return List.of(Text.of("preview text").asOrderedText());
    }
}
