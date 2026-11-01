package com.example.examplemod.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.client.config.ExampleConfig;
import com.example.examplemod.client.hud.CoordinatesHud;

public class ExampleModClient implements ClientModInitializer {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ExampleMod.id("main"));

	private static KeyMapping toggleHudKey;

	@Override
	public void onInitializeClient() {
		ExampleConfig.load();

		toggleHudKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.examplemod.toggle_hud",
				InputConstants.Type.KEYBOARD,
				InputConstants.KEY_H,
				CATEGORY
		));

		HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, ExampleMod.id("coordinates"), CoordinatesHud::extract);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleHudKey.consumeClick()) {
				toggleHud(client);
			}

			CoordinatesHud.tick(client);
		});

		ExampleMod.LOGGER.info("Example Mod initialized");
	}

	private static void toggleHud(Minecraft client) {
		ExampleConfig config = ExampleConfig.get();
		config.hudEnabled = !config.hudEnabled;
		ExampleConfig.save();

		if (client.player != null) {
			String key = config.hudEnabled ? "message.examplemod.hud_enabled" : "message.examplemod.hud_disabled";
			client.player.sendOverlayMessage(Component.translatable(key));
		}
	}
}
