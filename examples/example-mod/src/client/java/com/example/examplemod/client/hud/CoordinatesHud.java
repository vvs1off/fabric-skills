package com.example.examplemod.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.example.examplemod.client.config.ExampleConfig;

/**
 * Shows the player's block coordinates. The text is rebuilt once per tick and only when the
 * position changes, so the per-frame draw call allocates nothing.
 */
public final class CoordinatesHud {
	private static Component cachedText = Component.empty();
	private static BlockPos lastPos = null;

	private CoordinatesHud() {
	}

	/** Called from {@code ClientTickEvents.END_CLIENT_TICK}. */
	public static void tick(Minecraft client) {
		if (client.player == null) {
			lastPos = null;
			return;
		}

		BlockPos pos = client.player.blockPosition();

		if (!pos.equals(lastPos)) {
			lastPos = pos;
			cachedText = Component.translatable("hud.examplemod.coordinates", pos.getX(), pos.getY(), pos.getZ());
		}
	}

	/** Registered with {@code HudElementRegistry}; runs every frame. */
	public static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft client = Minecraft.getInstance();
		ExampleConfig config = ExampleConfig.get();

		// gui.hud.isHidden() is the F1 toggle (26.3; older versions: options.hideGui).
		if (!config.hudEnabled || client.player == null || client.gui.hud.isHidden()) {
			return;
		}

		graphics.text(client.font, cachedText, config.hudX, config.hudY, config.textColor, true);
	}
}
