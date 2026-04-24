package pohci.classicvillagers.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import pohci.classicvillagers.ClassicVillagers;
import pohci.classicvillagers.client.command.ClientVillagerCommands;
import pohci.classicvillagers.config.ModConfig;

public class ClassicVillagersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModConfig.load();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> ClientVillagerCommands.register(dispatcher));
		ClassicVillagers.LOGGER.info("feature loaded: baby villager classic hitbox (client)");
	}
}
