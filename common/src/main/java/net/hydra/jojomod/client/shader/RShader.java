package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32C;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class RShader extends GLResource {
    /* Type can be:
        * 1: Vertex Shader
        * 2: Fragment Shader
     */
    // TODO: support tesselation and geometry shaders
    public RShader(ResourceProvider provider, ResourceLocation location, int type) throws Exception {
        super(createShader(provider, location, type));
    }

    private static int createShader(ResourceProvider provider, ResourceLocation location, int type) throws Exception {
        if (type <= 0 || type > 2)
            throw new Exception("Unsupported ShaderType passed to new TSShader");

        int handle = 0;

        try
        {
            if (type == 1)
                handle = GlStateManager.glCreateShader(GL20.GL_VERTEX_SHADER);
            else
                handle = GlStateManager.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        }
        catch (Exception e)
        {
            Roundabout.LOGGER.error("Exception caught while attempting to create shader \"{}\"", location.toString());
            return 0;
        }

        if (handle == 0)
            throw new Exception("Failed to create shader \"" + location.toString() + "\"");

        RenderSystem.assertOnRenderThreadOrInit();
        GL20C.glShaderSource(handle, readShaderSource(provider, location));
        GlStateManager.glCompileShader(handle);

        if (GlStateManager.glGetShaderi(handle, GL20C.GL_COMPILE_STATUS) != GL20C.GL_TRUE)
            throw new Exception("Failed to compile shader \"" + location.toString() + "\" with error: \""+GL32C.glGetShaderInfoLog(handle)+"\"");

        Roundabout.LOGGER.info("Successfully registered shader \"{}\"", location.toString());

        return handle;
    }

    private static @Nullable String readShaderSource(ResourceProvider provider, ResourceLocation identifier) {
        Optional<Resource> r = provider.getResource(identifier);
        if (r.isPresent()) {
            try (BufferedReader file = r.get().openAsReader()) {
                return file.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}