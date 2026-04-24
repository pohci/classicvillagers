package pohci.classicvillagers.config;

public final class FeatureConfig {
	private static volatile boolean babyVillagerHitboxEnabled = true;

	private FeatureConfig() {
	}

	public static boolean isBabyVillagerHitboxEnabled() {
		return babyVillagerHitboxEnabled;
	}

	public static void setBabyVillagerHitboxEnabled(boolean enabled) {
		babyVillagerHitboxEnabled = enabled;
	}
}
