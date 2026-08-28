package net.hydra.jojomod.mixin.silver_chariot;

import net.hydra.jojomod.client.SilverChariotClient;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Camera.class, priority = 2000)
public abstract class SilverChariotControlCameraMixin {
    @ModifyVariable(method = "setRotation(FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float roundaboutSilverChariot$cameraYaw(float original) {
        if (!SilverChariotClient.hasCameraLook()) return original;
        return Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT
                ? SilverChariotClient.getCameraYaw() + 180.0F
                : SilverChariotClient.getCameraYaw();
    }

    @ModifyVariable(method = "setRotation(FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float roundaboutSilverChariot$cameraPitch(float original) {
        if (!SilverChariotClient.hasCameraLook()) return original;
        return Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT
                ? -SilverChariotClient.getCameraPitch()
                : SilverChariotClient.getCameraPitch();
    }
}
