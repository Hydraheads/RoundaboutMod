package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.shader.FogDataHolder;
import net.hydra.jojomod.client.shader.TSCoreShader;
import net.hydra.jojomod.client.shader.TSPostShader;
import net.hydra.jojomod.client.shader.callback.ResourceProviderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class ZGameRenderer {
    @Shadow @Final private Minecraft minecraft;

    /* nowhere else provides a ResourceProvider, register a callback from ResourceProviderEvent */
    @Inject(method="reloadShaders", at=@At("HEAD"))
    private void RoundaboutReloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        ResourceProviderEvent.invoke(provider);
    }

    @Inject(method="shutdownShaders", at=@At("HEAD"))
    private void shutdownShaders(CallbackInfo ci)
    {
        TSCoreShader.clear();
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",
            shift = At.Shift.AFTER),
            method = "render")
    private void renderShaders(float tickDelta, long $$1, boolean $$2, CallbackInfo ci) {
        // fog is not implemented yet as the depthbuffer issue needs to be fixed
//        if (FogDataHolder.fogDensity > 0.25 && FogDataHolder.shouldRenderFog) {
//            if (TSPostShader.FOG_SHADER != null && TSPostShader.FOG_SHADER_PASSES != null) {
//                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogDensity", FogDataHolder.fogDensity);
//                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogNear", FogDataHolder.fogNear);
//                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogFar", FogDataHolder.fogFar);
//                TSPostShader.setVec3Uniform(TSPostShader.FOG_SHADER_PASSES, "FogColor", FogDataHolder.fogColor);
//
//                TSPostShader.renderShader(TSPostShader.FOG_SHADER.get(), tickDelta);
//            }
//        }

        //TSCoreShader.renderShaderFullscreen(TSCoreShader.getByIndex(0).get());
    }
}