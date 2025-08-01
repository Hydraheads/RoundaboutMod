package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGameRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.zetalasis.client.shader.RCoreShader;
import net.zetalasis.client.shader.RPostShaderRegistry;
import net.zetalasis.client.shader.callback.RenderCallbackRegistry;
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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Mixin(GameRenderer.class)
public abstract class ZGameRenderer {
    @Shadow @Final private Map<String, ShaderInstance> shaders;
    @Shadow @Final private Minecraft minecraft;

    @Shadow public abstract Minecraft getMinecraft();

    @Inject(method = "renderLevel", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.BEFORE))
    private void roundabout$beforeClearDepthBuffer(float tickDelta, long $$1, PoseStack $$2, CallbackInfo ci)
    {
        // TODO: copy the old depth buffer to a texture
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void roundabout$renderShaders(float tickDelta, long $$1, boolean renderLevel, CallbackInfo ci)
    {
        RenderCallbackRegistry.roundabout$GAME_RENDERER_FINISH(tickDelta);

        if (RPostShaderRegistry.DESATURATE != null) {
            if (((IGameRenderer)Minecraft.getInstance().gameRenderer).roundabout$tsShaderStatus())
            {
                RPostShaderRegistry.DESATURATE.roundabout$setUniform("InvProjMat", RPostShaderRegistry.InverseProjectionMatrix);
                RPostShaderRegistry.DESATURATE.roundabout$process(tickDelta);
            }
        }
    }

    @Inject(method = "reloadShaders", at=@At("HEAD"))
    private void roundabout$reloadShaders(ResourceProvider provider, CallbackInfo ci)
    {
        RPostShaderRegistry.bootstrap();

        try {
            RCoreShader.roundabout$meltDodgeProgram = Objects.requireNonNull(roundabout$registerShader(provider, "meltdodge")).getProgram();
            RCoreShader.roundabout$loveTrainProgram = Objects.requireNonNull(roundabout$registerShader(provider, "lovetrainlines")).getProgram();
        } catch(Exception e) {
            //put shader debug stuff here
            Roundabout.LOGGER.info("Oops, something went wrong loading shaders");
        }
        Roundabout.LOGGER.info("Reloaded shaders!");
    }

    @Unique private RCoreShader roundabout$registerShader(ResourceProvider provider, String name)
    {
        try
        {
            RCoreShader shader = new RCoreShader(provider, name);

            if (shader.getProgram() != null)
            {
                shader.getProgram().setSampler("DiffuseSampler", this.minecraft.getMainRenderTarget().getColorTextureId());
            }
            else {
                throw new IOException("Shader was null!");
            }

            return shader;
        }
        catch (IOException e)
        {
            Roundabout.LOGGER.warn("roundabout${} failed to load!", name);
            return null;
        }
    }
}
