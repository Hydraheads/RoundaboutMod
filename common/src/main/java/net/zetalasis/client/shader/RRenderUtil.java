package net.zetalasis.client.shader;

import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/** Static class for various rendering utils */
public class RRenderUtil {
    /** Shader logic should be changed if Minecraft is using the Fabulous transparency shader.
     * @return Is Minecraft using the Fabulous transparency shader? */
    public static boolean isUsingFabulous()
    {
        return (Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FABULOUS);
    }

    static boolean fabulousWarned = false;
    public static void sendFabulousWarning()
    {
        if (isUsingFabulous())
        {
            if (!fabulousWarned)
                fabulousWarned = true;
            else
                return;

            Minecraft client = Minecraft.getInstance();

            if (client.player != null)
            {
                client.player.sendSystemMessage(Component.translatable("roundabout.warning.fabulous_graphics.v2"));
            }
        }
    }
}
