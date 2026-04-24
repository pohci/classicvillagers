package pohci.classicvillagers.util;

import net.minecraft.network.chat.Component;

public final class CommandMessages {
	private CommandMessages() {
	}

	public static Component hitboxSet(boolean babyVillagerHitboxEnabled) {
		if (babyVillagerHitboxEnabled) {
			return Component.literal("Villagers hitbox set to Skinny");
		}
		return Component.literal("Villagers hitbox set to Fat");
	}
}
