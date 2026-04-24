package pohci.classicvillagers.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.npc.villager.Villager;

public final class ClientHitboxRefresh {
	private ClientHitboxRefresh() {
	}

	public static int refreshVisibleVillagers() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return 0;
		}

		int refreshed = 0;
		for (var entity : minecraft.level.entitiesForRendering()) {
			if (entity instanceof Villager villager && villager.isBaby()) {
				villager.refreshDimensions();
				refreshed++;
			}
		}
		return refreshed;
	}
}
