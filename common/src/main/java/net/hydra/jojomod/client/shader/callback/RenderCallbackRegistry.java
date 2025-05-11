package net.hydra.jojomod.client.shader.callback;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.RRenderUtil;

import java.util.HashSet;

public class RenderCallbackRegistry {
    private static final HashSet<IRendererCallback> registry = new HashSet<>();

    public static void register(IRendererCallback callback)
    {
        registry.add(callback);
    }

    public static void roundabout$LEVEL_RENDER_FINISH(float partialTick)
    {
        for (IRendererCallback callback : registry)
        {
            try {
                callback.roundabout$LEVEL_RENDER_FINISH(partialTick);
            }
            catch (Exception e)
            {
                Roundabout.LOGGER.warn("Exception caught while firing LEVEL_RENDER_FINISH for a callback!\n\"{}\"", e.toString());
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void roundabout$GAME_RENDERER_FINISH(float tickDelta)
    {
        RRenderUtil.sendFabulousWarning();

        for (IRendererCallback callback : registry)
        {
            try {
                callback.roundabout$GAME_RENDERER_FINISH(tickDelta);
            }
            catch (Exception e)
            {
                Roundabout.LOGGER.warn("Exception caught while firing GAME_RENDERER_FINISH for a callback!\n\"{}\"", e.toString());
                e.printStackTrace();
                continue;
            }
        }
    }
}
