package pohci.classicvillagers.config;

public final class FeatureConfig {
	private static volatile boolean babyVillagerHitboxEnabled = true;
	private static volatile boolean babyVillagerOldModelEnabled = false;

	private FeatureConfig() {
	}

	public static boolean isBabyVillagerHitboxEnabled() {
		return babyVillagerHitboxEnabled;
	}

	public static void setBabyVillagerHitboxEnabled(boolean enabled) {
		babyVillagerHitboxEnabled = enabled;
	}

	public static boolean isBabyVillagerOldModelEnabled() {
		return babyVillagerOldModelEnabled;
	}

	public static void setBabyVillagerOldModelEnabled(boolean enabled) {
		babyVillagerOldModelEnabled = enabled;
	}
}
