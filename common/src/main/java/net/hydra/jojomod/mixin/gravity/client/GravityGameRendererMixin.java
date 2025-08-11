package net.hydra.jojomod.mixin.gravity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.util.RotationAnimation;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.client.Minecraft;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

@Mixin(GameRenderer.class)
public abstract class GravityGameRendererMixin {
    @Shadow
    @Final
    private Camera mainCamera;

    @Inject(
            method = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void roundabout$inject_renderWorld(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo ci) {
        if (this.mainCamera.getEntity() != null) {
            Entity focusedEntity = this.mainCamera.getEntity();
            Direction gravityDirection = GravityAPI.getGravityDirection(focusedEntity);
            RotationAnimation animation = GravityAPI.getRotationAnimation(focusedEntity);
            if (animation == null) {
                return;
            }
            long timeMs = focusedEntity.level().getGameTime() * 50 + (long) (tickDelta * 50);
            Quaternionf currentGravityRotation = animation.getCurrentGravityRotation(gravityDirection, timeMs);

            if (animation.isInAnimation()) {
                // make sure that frustum culling updates when running rotation animation
                Minecraft.getInstance().levelRenderer.needsUpdate();
            }

            matrix.mulPose(currentGravityRotation);
        }
    }
}