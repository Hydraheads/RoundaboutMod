package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Camera.class, priority = 2000)
public abstract class WhitesnakeControlCameraMixin {
    @ModifyVariable(method = "setRotation(FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float roundaboutWhitesnake$cameraYaw(float original) {
        if (!WhitesnakeControlClient.hasCameraLook()) return original;
        return Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT
                ? WhitesnakeControlClient.getCameraYaw() + 180.0F
                : WhitesnakeControlClient.getCameraYaw();
    }

    @ModifyVariable(method = "setRotation(FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float roundaboutWhitesnake$cameraPitch(float original) {
        if (!WhitesnakeControlClient.hasCameraLook()) return original;
        return Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT
                ? -WhitesnakeControlClient.getCameraPitch()
                : WhitesnakeControlClient.getCameraPitch();
    }
}
