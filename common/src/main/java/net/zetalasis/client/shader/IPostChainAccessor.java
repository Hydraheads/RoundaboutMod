package net.zetalasis.client.shader;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;

import java.util.List;

public interface IPostChainAccessor {
    /** Get all the passes defined by the post effect .json */
    List<PostPass> roundabout$getPasses();

    /** Resizes the texture buffer if there's an update (i.e. window size changes). */
    void roundabout$resize();

    void roundabout$process(float tickDelta);

    /** Attempts to set the uniform in the post effect.
     * @return true if uniform is found, otherwise false */
    boolean roundabout$setUniform(String name, Object value);

    static IPostChainAccessor getInstance(PostChain chain)
    { return (IPostChainAccessor) chain; }
}