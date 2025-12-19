package net.hydra.jojomod.access;

import net.minecraft.client.Camera;
import net.minecraft.resources.ResourceLocation;

public interface IShaderGameRenderer {
    void roundabout$loadEffect(ResourceLocation $$0);

    boolean roundabout$tsShaderStatus();

    float roundabout$getFrameCount();
    float roundabout$getFov(Camera activeRenderInfo, float partialTicks, boolean useFOVSetting);
}
