package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;

public interface DepthRenderTarget {
    int getStillDepthBuffer();
    void freezeDepthBuffer();

    static DepthRenderTarget getFrom(RenderTarget target) {
        return (DepthRenderTarget) target;
    }

    static int getStillDepthMap(RenderTarget target) {
        return getFrom(target).getStillDepthBuffer();
    }

    static void freezeDepthMap(RenderTarget target) {
        getFrom(target).freezeDepthBuffer();
    }
}
