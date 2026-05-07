package net.hydra.jojomod.mixin.metallica;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckInBodyLayer.class)
public abstract class MetallicaStuckLayerMixin<T extends LivingEntity, M extends net.minecraft.client.model.EntityModel<T>> {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void roundabout$render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (MainUtil.isUsingMetallica(livingEntity)) {
            float opacity = PowersMetallica.getMetallicaInvisibilityAlpha(
                    livingEntity,
                    Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(livingEntity.position()),
                    partialTick
            );

            if (opacity < 0.99F) {
                ci.cancel();
            }
        }
    }
}