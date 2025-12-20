package net.hydra.jojomod.mixin.shaders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.zetalasis.client.shader.TimestopShaderManager;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class ShaderLevelRenderer {
    @Inject(method = "renderLevel", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupLevel(Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void render(PoseStack $$0, float partialTick, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci)
    {
        if (Minecraft.getInstance().player == null)
            return;

        if (TimestopShaderManager.TIMESTOP_DEPTH_BUFFER != null)
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        //TimestopShaderManager.renderAll(partialTick);
    }
}