package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.shaders.Uniform;
import net.zetalasis.client.shader.IPostChainAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(PostChain.class)
public class PostChainMixin implements IPostChainAccessor {
    @Shadow @Final private List<PostPass> passes;
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
        processCount += 1;

        this.roundabout$resize();
        roundabout$setUniform("FrameCount", processCount);
        ((PostChain)(Object)this).process(tickDelta);
        Minecraft client = Minecraft.getInstance();
        client.getMainRenderTarget().bindWrite(false);
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
                if ((value) instanceof Float)
                { u.set((float) value); }
                else if (value instanceof Integer) {
                    u.set((int) value); }
                else if (value instanceof Matrix3f) {
                    u.set((Matrix3f) value); }
                else if (value instanceof Matrix4f) {
                    u.set((Matrix4f) value); }
                else if (value instanceof Vector3f) {
                    u.set((Vector3f) value); }
                else if (value instanceof Vector4f) {
                    u.set((Vector4f) value); }
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
}
