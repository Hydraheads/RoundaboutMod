package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

public class StencilBuffer {
    public static void beginWrite()
    {
        RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, false);
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glStencilMask(0xFF);

        RenderSystem.colorMask(false, false, false, false);
        RenderSystem.depthMask(false);
    }

    public static void endWrite()
    {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthMask(true);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
    }

    public static void beginRender() {
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL11.glStencilMask(0x00);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
    }

    public static void endRender()
    {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }
}