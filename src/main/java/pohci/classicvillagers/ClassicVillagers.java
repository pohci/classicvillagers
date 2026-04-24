package pohci.classicvillagers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import pohci.classicvillagers.command.ClassicVillagersCommands;
import pohci.classicvillagers.config.ModConfig;
import pohci.classicvillagers.config.ServerHitboxRefresh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassicVillagers implements ModInitializer {
	public static final String MOD_ID = "classicvillagers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		try {
			ServerLifecycleEvents.SERVER_STARTING.register(server -> {
				ModConfig.load();
				int refreshed = ServerHitboxRefresh.refreshAllVillagers(server);
				LOGGER.info("server config loaded, refreshed {} baby villagers", refreshed);
			});
			ServerLifecycleEvents.SERVER_STOPPED.register(server -> ModConfig.writeCurrent());
			CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> ClassicVillagersCommands.register(dispatcher));
			String version = resolveVersion();
			LOGGER.info("starting {} v{}", MOD_ID, version);
			LOGGER.info("feature loaded: baby villager classic hitbox (server)");
			LOGGER.info("startup complete");
		} catch (Exception exception) {
			LOGGER.error("startup issue detected, continuing with safe defaults", exception);
		}
	}

	private static String resolveVersion() {
		ModContainer modContainer = FabricLoader.getInstance().getModContainer(MOD_ID).orElse(null);
		if (modContainer == null) {
			return "unknown";
		}

		return modContainer.getMetadata().getVersion().getFriendlyString();
	}
}
