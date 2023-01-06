package net.kris.mpcap;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class PacketsViewScreen extends Screen {
    private Mpcap mpcap;

    protected PacketsViewScreen(Mpcap mpcap) {
        super(Text.translatable("packet_view_screen.title"));
        this.mpcap = mpcap;
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
            this.mpcap.packageHistory.scroll(1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.mpcap.packageHistory.scroll(-1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            this.mpcap.packageHistory.toTop();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            this.mpcap.packageHistory.toButtom();
            return true;
        }
        return false;
    }
}
