package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.client.shader.DepthRenderTarget;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin implements DepthRenderTarget {
    @Unique private int stillDepthTexture = -1;

    @Shadow @Final public boolean useDepth;

    @Shadow public int width;
    @Shadow public int height;

    @Shadow public abstract void bindWrite(boolean $$0);

    @Unique
    private int setupDepthTexture() {
        int shadowMap = GL11.glGenTextures();
        RenderSystem.bindTexture(shadowMap);
        RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GlStateManager._texImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, this.width, this.height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, null);
        return shadowMap;
    }

    /* opcode for PUTFIELD */
    @Inject(method = "createBuffers", at= @At(value = "FIELD", opcode = 0xb5, target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;depthBufferId:I", shift = At.Shift.AFTER))
    private void initFBO(int $$0, int $$1, boolean $$2, CallbackInfo ci)
    {
        if(this.useDepth) {
            this.stillDepthTexture = setupDepthTexture();
        }
    }

    @Override
    public int getStillDepthBuffer() {
        return this.stillDepthTexture;
    }

    @Override
    public void freezeDepthBuffer() {
        if(this.useDepth) {
            this.bindWrite(false);
            RenderSystem.bindTexture(this.stillDepthTexture);
            glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, this.width, this.height);
        }
    }
}
