package net.zetalasis.client.shader.callback;

public interface IRendererCallback {
    void roundabout$LEVEL_RENDER_FINISH(float partialTick);
    void roundabout$GAME_RENDERER_FINISH(float tickDelta);

    /** Register shader dependant stuff here
     * Shaders are registered in RPostShaderRegistry */
    void roundabout$bootstrap();
}
