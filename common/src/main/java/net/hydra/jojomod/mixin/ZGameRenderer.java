package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.RCoreShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

@Mixin(GameRenderer.class)
public class ZGameRenderer {
    @Shadow @Final private Map<String, ShaderInstance> shaders;
    @Shadow @Final private Minecraft minecraft;
    @Unique @Nullable
    private RCoreShader roundabout$meltDodgeShader;

    @Inject(method = "reloadShaders", at=@At("HEAD"))
    private void roundabout$reloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        try
        {
            roundabout$meltDodgeShader = new RCoreShader(provider, "meltdodge");

            if (roundabout$meltDodgeShader.getProgram() != null)
            {
                roundabout$meltDodgeShader.getProgram().setSampler("DiffuseSampler", this.minecraft.getMainRenderTarget().getColorTextureId());
            }
            else {
                throw new IOException("Shader was null!");
            }
        }
        catch (IOException e)
        {
            Roundabout.LOGGER.warn("roundabout$meltDodgeShader failed to load!\n{}", e.toString());
            return;
        }
        RCoreShader.roundabout$meltDodgeProgram = roundabout$meltDodgeShader.getProgram();
        Roundabout.LOGGER.info("Reloaded shaders!");
    }
}
