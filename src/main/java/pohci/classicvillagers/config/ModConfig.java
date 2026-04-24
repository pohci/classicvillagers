package pohci.classicvillagers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import pohci.classicvillagers.ClassicVillagers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String FILE_NAME = "classicvillagers.json";
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
	private static final Path LEGACY_SERVER = FabricLoader.getInstance().getConfigDir().resolve("classicvillagers-server.json");
	private static final Path LEGACY_CLIENT = FabricLoader.getInstance().getConfigDir().resolve("classicvillagers-client.json");
	private static final Path[] LEGACY_PATHS = {LEGACY_SERVER, LEGACY_CLIENT};

	private ModConfig() {
	}

	public static void load() {
		apply(readOrCreateDefault());
	}

	public static boolean reloadFromFile() {
		if (!Files.exists(CONFIG_PATH)) {
			load();
			return true;
		}
		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			ModConfigData data = GSON.fromJson(reader, ModConfigData.class);
			if (data == null) {
				return false;
			}
			apply(data);
			return true;
		} catch (IOException | JsonParseException exception) {
			ClassicVillagers.LOGGER.error("failed to reload config from disk", exception);
			return false;
		}
	}

	public static void setBabyVillagerHitboxEnabled(boolean enabled) {
		FeatureConfig.setBabyVillagerHitboxEnabled(enabled);
		writeCurrent();
	}

	public static void writeCurrent() {
		ModConfigData data = new ModConfigData();
		data.babyVillagerHitboxEnabled = FeatureConfig.isBabyVillagerHitboxEnabled();
		write(data);
	}

	private static ModConfigData readOrCreateDefault() {
		if (!Files.exists(CONFIG_PATH)) {
			Boolean fromLegacy = tryReadLegacy();
			ModConfigData data = new ModConfigData();
			if (fromLegacy != null) {
				data.babyVillagerHitboxEnabled = fromLegacy;
			}
			write(data);
			return data;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			ModConfigData data = GSON.fromJson(reader, ModConfigData.class);
			return data == null ? new ModConfigData() : data;
		} catch (IOException | JsonParseException exception) {
			ClassicVillagers.LOGGER.error("failed to read config, using defaults", exception);
			ModConfigData defaultData = new ModConfigData();
			write(defaultData);
			return defaultData;
		}
	}

	private static Boolean tryReadLegacy() {
		for (Path legacy : LEGACY_PATHS) {
			if (!Files.exists(legacy)) {
				continue;
			}
			try (Reader reader = Files.newBufferedReader(legacy)) {
				ModConfigData data = GSON.fromJson(reader, ModConfigData.class);
				if (data == null) {
					continue;
				}
				ClassicVillagers.LOGGER.info("migrated config from {} to {}", legacy.getFileName(), FILE_NAME);
				return data.babyVillagerHitboxEnabled;
			} catch (IOException | JsonParseException exception) {
				ClassicVillagers.LOGGER.warn("failed to read legacy config {}", legacy, exception);
			}
		}
		return null;
	}

	private static void write(ModConfigData data) {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(data, writer);
			}
		} catch (IOException exception) {
			ClassicVillagers.LOGGER.error("failed to write config", exception);
		}
	}

	private static void apply(ModConfigData data) {
		FeatureConfig.setBabyVillagerHitboxEnabled(data.babyVillagerHitboxEnabled);
	}

	private static final class ModConfigData {
		@SerializedName("babyVillagerHitboxEnabled")
		private boolean babyVillagerHitboxEnabled = true;
	}
}
