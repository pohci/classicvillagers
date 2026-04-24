package pohci.classicvillagers.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.villager.Villager;
import pohci.classicvillagers.config.FeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class BabyVillagerHitboxMixin {
	@Inject(method = "getDefaultDimensions", at = @At("RETURN"), cancellable = true)
	private void getDefaultDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
		Villager villager = (Villager) (Object) this;
		if (!villager.isBaby()) {
			return;
		}
		if (!FeatureConfig.isBabyVillagerHitboxEnabled()) {
			return;
		}

		cir.setReturnValue(EntityDimensions.scalable(0.3F, 0.98F));
	}
}
