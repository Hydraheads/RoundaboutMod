package net.zetalasis.client.shader.callback;

import net.hydra.jojomod.SBA;
import net.zetalasis.client.shader.RRenderUtil;

import java.util.HashSet;

public class RenderCallbackRegistry {
    private static final HashSet<IRendererCallback> registry = new HashSet<>();

    public static void register(IRendererCallback callback)
    {
        registry.add(callback);
    }

    public static void sba$LEVEL_RENDER_FINISH(float partialTick)
    {
        for (IRendererCallback callback : registry)
        {
            try {
                callback.sba$LEVEL_RENDER_FINISH(partialTick);
            }
            catch (Exception e)
            {
                SBA.LOGGER.warn("Exception caught while firing LEVEL_RENDER_FINISH for a callback!\n\"{}\"", e.toString());
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void sba$GAME_RENDERER_FINISH(float tickDelta)
    {
        RRenderUtil.sendFabulousWarning();

        for (IRendererCallback callback : registry)
        {
            try {
                callback.sba$GAME_RENDERER_FINISH(tickDelta);
            }
            catch (Exception e)
            {
                SBA.LOGGER.warn("Exception caught while firing GAME_RENDERER_FINISH for a callback!\n\"{}\"", e.toString());
                e.printStackTrace();
                continue;
            }
        }
    }
}
