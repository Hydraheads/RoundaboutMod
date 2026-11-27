package net.hydra.jojomod.access;

import net.minecraft.client.Camera;
import net.minecraft.resources.ResourceLocation;

public interface IShaderGameRenderer {
    void sba$loadEffect(ResourceLocation $$0);

    boolean sba$tsShaderStatus();

    float sba$getFrameCount();
    float sba$getFov(Camera activeRenderInfo, float partialTicks, boolean useFOVSetting);
}
