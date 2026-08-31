package net.hydra.jojomod.mixin.whitesnake.hallucination;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.HallucinationRenderOffset;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class HallucinationShadowMixin {
    private static final String RENDER_SHADOW = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;"
            + "renderShadow(Lcom/mojang/blaze3d/vertex/PoseStack;"
            + "Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;"
            + "FFLnet/minecraft/world/level/LevelReader;F)V";

    @Inject(method = "render", at = @At(value = "INVOKE", target = RENDER_SHADOW))
    private <E extends Entity> void roundaboutWhitesnake$offsetShadow(E rendered,
                                                                       double x, double y, double z,
                                                                       float yaw, float partialTick,
                                                                       PoseStack poseStack,
                                                                       MultiBufferSource buffers,
                                                                       int light, CallbackInfo ci) {
        Vec3 offset = HallucinationRenderOffset.forEntity(rendered);
        poseStack.translate(offset.x, offset.y, offset.z);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = RENDER_SHADOW, shift = At.Shift.AFTER))
    private <E extends Entity> void roundaboutWhitesnake$restoreShadowPose(E rendered,
                                                                           double x, double y, double z,
                                                                           float yaw, float partialTick,
                                                                           PoseStack poseStack,
                                                                           MultiBufferSource buffers,
                                                                           int light, CallbackInfo ci) {
        Vec3 offset = HallucinationRenderOffset.forEntity(rendered);
        poseStack.translate(-offset.x, -offset.y, -offset.z);
    }
}
