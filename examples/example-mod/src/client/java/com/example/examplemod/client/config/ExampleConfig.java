package com.example.examplemod.client.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.fabricmc.loader.api.FabricLoader;

import com.example.examplemod.ExampleMod;

/**
 * Dependency-free JSON config stored at {@code config/examplemod.json}.
 * Values are read from memory; the file is only touched on load and save.
 */
public final class ExampleConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(ExampleMod.MOD_ID + ".json");
	private static ExampleConfig instance = new ExampleConfig();

	public int configVersion = 1;
	public boolean hudEnabled = true;
	public int hudX = 4;
	public int hudY = 4;
	/** ARGB. Keep the alpha byte, or the text is invisible. */
	public int textColor = 0xFFFFFFFF;

	public static ExampleConfig get() {
		return instance;
	}

	public static void load() {
		if (Files.notExists(PATH)) {
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(PATH)) {
			ExampleConfig loaded = GSON.fromJson(reader, ExampleConfig.class);
			instance = loaded != null ? loaded.validated() : new ExampleConfig();
		} catch (IOException | JsonParseException e) {
			// Keep the user's file untouched so they can fix it; run with defaults meanwhile.
			ExampleMod.LOGGER.error("Invalid config {}, using defaults", PATH, e);
			instance = new ExampleConfig();
		}
	}

	public static void save() {
		try {
			Files.createDirectories(PATH.getParent());
			Path tmp = PATH.resolveSibling(PATH.getFileName() + ".tmp");

			try (Writer writer = Files.newBufferedWriter(tmp)) {
				GSON.toJson(instance, writer);
			}

			Files.move(tmp, PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			ExampleMod.LOGGER.error("Could not save config {}", PATH, e);
		}
	}

	private ExampleConfig validated() {
		hudX = Math.max(0, hudX);
		hudY = Math.max(0, hudY);

		if ((textColor >>> 24) == 0) {
			textColor |= 0xFF000000;
		}

		return this;
	}
}
