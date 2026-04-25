package pohci.classicvillagers.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pohci.classicvillagers.config.FeatureConfig;

@Mixin(AgeableMobRenderer.class)
@SuppressWarnings("deprecation")
public class BabyVillagerRenderScaleMixin {
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At("HEAD")
	)
	private void classicVillagers$babyVillagerModelScale(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
		if (!FeatureConfig.isBabyVillagerOldModelEnabled()) {
			return;
		}
		if (!(state instanceof VillagerRenderState) || !state.isBaby) {
			return;
		}

		state.isBaby = false;
		state.scale = 0.5f;
	}
}
