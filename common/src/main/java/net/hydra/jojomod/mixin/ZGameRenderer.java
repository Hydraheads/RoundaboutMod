package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.TSShader;
import net.hydra.jojomod.client.shader.TSShaderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
            TSShader fragment = new TSShader(provider, new ResourceLocation("roundabout", "shaders/fog.fsh"), 2);
            TSShader vertex = new TSShader(provider, new ResourceLocation("roundabout", "shaders/fog.vsh"), 1);

            TSShaderManager.roundabout$program = TSShaderManager.createProgram(vertex, fragment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
