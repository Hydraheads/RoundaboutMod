package net.zetalasis.client.shader;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;

public class RPostShaderRegistry {
    private static HashMap<String, IPostChainAccessor> shaders = new HashMap<>();

    public static IPostChainAccessor D4C_DIMENSION_TRANSITION = null;
    public static IPostChainAccessor D4C_ALT_DIMENSION = null;

    /** Super Secret Shaders */
    public static IPostChainAccessor DESATURATE = null;
    public static IPostChainAccessor DECONVERGE = null;
    public static IPostChainAccessor PHOSPHOR = null;

    public static void bootstrap()
    {
        Roundabout.LOGGER.info("Registering post effects");

        D4C_DIMENSION_TRANSITION = register("d4cdimtransition");
        D4C_ALT_DIMENSION = register("d4caltdim");

        /** Super Secret Shaders */
        DESATURATE = register("desaturate");
        DECONVERGE = register("deconverge");
        PHOSPHOR = register("phosphor");
    }

    public static @Nullable IPostChainAccessor register(String name)
    {
        try
        {
            Minecraft client = Minecraft.getInstance();

            TextureManager tm = client.getTextureManager();
            ResourceManager rm = client.getResourceManager();

            IPostChainAccessor shader = IPostChainAccessor.getInstance(new PostChain(tm, rm, client.getMainRenderTarget(), new ResourceLocation("shaders/post/"+name+".json")));

            shaders.put(name, shader);

            Roundabout.LOGGER.info("Registered post shader \"{}\" successfully", name);
            return shader;
        }
        catch (IOException e)
        {
            Roundabout.LOGGER.warn("Failed to register shader \"{}\"!\n\"{}\"", name, e.toString());
        }
        return null;
    }

    public static @Nullable IPostChainAccessor getByName(String name)
    {
        return shaders.get(name);
    }
}
