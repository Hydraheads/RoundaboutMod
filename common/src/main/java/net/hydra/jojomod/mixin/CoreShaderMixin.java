package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.callback.ResourceProviderEvent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CoreShaderMixin {
    /* nowhere else provides a ResourceProvider, register a callback from ResourceProviderEvent */
    @Inject(method="reloadShaders", at=@At("HEAD"))
    private void RoundaboutReloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        ResourceProviderEvent.invoke(provider);
    }
}
