package pohci.classicvillagers.config;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.npc.villager.Villager;

public final class ServerHitboxRefresh {
	private ServerHitboxRefresh() {
	}

	public static int refreshAllVillagers(MinecraftServer server) {
		int refreshed = 0;
		for (var level : server.getAllLevels()) {
			for (var entity : level.getAllEntities()) {
				if (entity instanceof Villager villager && villager.isBaby()) {
					villager.refreshDimensions();
					refreshed++;
				}
			}
		}
		return refreshed;
	}
}
