package net.zetalasis.client.shader.callback;

public interface IRendererCallback {
    void sba$LEVEL_RENDER_FINISH(float partialTick);
    void sba$GAME_RENDERER_FINISH(float tickDelta);

    /** Register shader dependant stuff here
     * Shaders are registered in RPostShaderRegistry */
    void sba$bootstrap();
}
