package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.RShader;
import net.hydra.jojomod.client.shader.RShaderProgram;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class ZGameRenderer {
    @Inject(method="reloadShaders", at=@At("HEAD"))
    private void reloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        Roundabout.LOGGER.info("Reloading shaders...");
        try {
            RShader fragment = new RShader(provider, new ResourceLocation("roundabout", "shaders/fog.fsh"), 2);
            RShader vertex = new RShader(provider, new ResourceLocation("roundabout", "shaders/fog.vsh"), 1);

            new RShaderProgram(vertex, fragment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
