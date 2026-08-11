package net.hydra.jojomod.mixin.whitesnake;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.RotationAnimation;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 900)
public abstract class WhitesnakeControlGravityCameraMixin {
    @Shadow
    @Final
    private Camera mainCamera;

    @Inject(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 3, shift = At.Shift.AFTER), require = 0)
    private void roundaboutWhitesnake$orientFabricControlCamera(float tickDelta, long limitTime,
                                                                PoseStack poseStack, CallbackInfo ci) {
        if (!"Forge".equals(ModPacketHandler.PLATFORM_ACCESS.getPlatformName())) {
            roundaboutWhitesnake$applyControlGravity(tickDelta, poseStack);
        }
    }

    @Inject(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 4, shift = At.Shift.AFTER), require = 0)
    private void roundaboutWhitesnake$orientForgeControlCamera(float tickDelta, long limitTime,
                                                               PoseStack poseStack, CallbackInfo ci) {
        if ("Forge".equals(ModPacketHandler.PLATFORM_ACCESS.getPlatformName())) {
            roundaboutWhitesnake$applyControlGravity(tickDelta, poseStack);
        }
    }

    private void roundaboutWhitesnake$applyControlGravity(float tickDelta, PoseStack poseStack) {
        Entity focused = mainCamera.getEntity();
        Entity controlled = ((ICamera) mainCamera).roundabout$getPovSwitch();
        if (focused == null || focused.is(controlled)
                || !(controlled instanceof WhitesnakeEntity whitesnake)
                || !whitesnake.isControlModeActive()) return;

        Direction controlledGravity = GravityAPI.getGravityDirection(controlled);
        RotationAnimation controlledAnimation = GravityAPI.getRotationAnimation(controlled);
        if (controlledAnimation == null) return;

        long timeMs = controlled.level().getGameTime() * 50L + (long) ((tickDelta % 1.0F) * 50.0F);
        Quaternionf correction = new Quaternionf();
        RotationAnimation focusedAnimation = GravityAPI.getRotationAnimation(focused);
        if (focusedAnimation != null) {
            Direction focusedGravity = GravityAPI.getGravityDirection(focused);
            correction.set(focusedAnimation.getCurrentGravityRotation(focusedGravity, timeMs)).conjugate();
        }
        correction.mul(controlledAnimation.getCurrentGravityRotation(controlledGravity, timeMs));
        poseStack.mulPose(correction);

        if (controlledAnimation.isInAnimation()) Minecraft.getInstance().levelRenderer.needsUpdate();
    }
}
