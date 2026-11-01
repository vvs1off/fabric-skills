package com.example.examplemod;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared constants. This mod is client-only, so there is no common {@code main} entrypoint;
 * everything that touches the client lives in {@code src/client}.
 */
public final class ExampleMod {
	public static final String MOD_ID = "examplemod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private ExampleMod() {
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
