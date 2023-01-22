package net.kris.mpcap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class Mpcap implements ModInitializer, ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("mpcap");

	private static Mpcap instance;
	private static boolean disabledUI = false;

	protected KeyBinding pvsKey;

	public PacketHistory packetHistory;

	public Mpcap() {
		instance = this;
	}

	public static Mpcap getInstance() {
		return instance;
	}

	@Override
	public void onInitialize() {
	}

	@Override
	public void onInitializeClient() {
		if (!disabledUI)
			initUI();
		LOGGER.info("Mpcap initialized!");
	}

	private void initUI() {
		this.pvsKey = new KeyBinding("key.openPacketsView",GLFW.GLFW_KEY_F12,"key.categories.misc");
		KeyBindingHelper.registerKeyBinding(pvsKey);

		ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
		ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
	}

	private void onClientStarted(MinecraftClient client) {
		this.packetHistory = new PacketHistory(client);
	}

	private void onClientTick(MinecraftClient client) {
		while (pvsKey.wasPressed()) {
			client.setScreen(new PacketsViewScreen(this));
		}
	}
}
