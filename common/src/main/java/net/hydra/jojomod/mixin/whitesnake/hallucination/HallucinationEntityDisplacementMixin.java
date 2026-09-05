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
public abstract class HallucinationEntityDisplacementMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private <E extends Entity> void roundaboutWhitesnake$applyDisplacement(E rendered,
                                                                           double x, double y, double z,
                                                                           float yaw, float partialTick,
                                                                           PoseStack poseStack,
                                                                           MultiBufferSource buffers,
                                                                           int light, CallbackInfo ci) {
        Vec3 offset = HallucinationRenderOffset.forEntity(rendered);
        poseStack.translate(offset.x, offset.y, offset.z);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private <E extends Entity> void roundaboutWhitesnake$stopDisplacement(E rendered,
                                                                          double x, double y, double z,
                                                                          float yaw, float partialTick,
                                                                          PoseStack poseStack,
                                                                          MultiBufferSource buffers,
                                                                          int light, CallbackInfo ci) {
        Vec3 offset = HallucinationRenderOffset.forEntity(rendered);
        poseStack.translate(-offset.x, -offset.y, -offset.z);
    }
}
