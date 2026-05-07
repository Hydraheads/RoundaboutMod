package net.hydra.jojomod.mixin.shaders;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IShaderGameRenderer;
import net.minecraft.client.Camera;
import net.zetalasis.client.shader.IPostChainAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.zetalasis.client.shader.TimestopShaderManager;
import org.joml.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.Math;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;

@Mixin(PostChain.class)
public abstract class PostChainMixin implements IPostChainAccessor {
    @Shadow @Final private List<PostPass> passes;

    @Shadow public abstract RenderTarget getTempTarget(String string);

    @Shadow private int screenWidth;
    @Shadow private int screenHeight;
    @Unique private float processCount;

    @Override
    public List<PostPass> roundabout$getPasses() {
        return this.passes;
    }

    @Override
    public void roundabout$resize() {
        Minecraft client = Minecraft.getInstance();
        ((PostChain)((Object)this)).resize(client.getWindow().getWidth(), client.getWindow().getHeight());
    }

    @Override
    public void roundabout$process(float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        processCount += 1;

        this.roundabout$resize();

        /*
        if (TimestopShaderManager.TIMESTOP_DEPTH_BUFFER == null)
        {
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER = this.getTempTarget("timestop");
            TimestopShaderManager.TIMESTOP_DEPTH_BUFFER.resize(screenWidth, screenHeight, true);
            return;
        }*/

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        ((PostChain)(Object)this).process(tickDelta);
        client.getMainRenderTarget().bindWrite(true);

        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
    }

    @Override
    public boolean roundabout$setUniform(String name, Object value) {
        try
        {
            for (PostPass p : this.passes)
            {
                Uniform u = p.getEffect().getUniform(name);
                if (u == null)
                    return false;

                // thanks Mojang for making this the only way by having 3 million overloads
                if ((value) instanceof Float f)
                { u.set(f); }
                else if (value instanceof Integer i) {
                    u.set(i); }
                else if (value instanceof Matrix3f m) {
                    u.set(m); }
                else if (value instanceof Matrix4f m) {
                    u.set(m); }
                else if (value instanceof Vector3f v) {
                    u.set(v); }
                else if (value instanceof Vector4f v) {
                    u.set(v); }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }

    @Inject(method = "getRenderTarget", at = @At("HEAD"), cancellable = true)
    private void injectCustomTargets(String name, CallbackInfoReturnable<RenderTarget> cir)
    {
        if (name.equals("timestop"))
        {
            cir.setReturnValue(TimestopShaderManager.TIMESTOP_DEPTH_BUFFER);
            cir.cancel();
        }
    }
}
