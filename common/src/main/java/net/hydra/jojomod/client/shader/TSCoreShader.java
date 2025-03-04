package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.callback.ResourceProviderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/* class for handling core shaders & programs */
public class TSCoreShader {
    /* final list of instances */
    private static List<ShaderInstance> shaderInstances = new ArrayList<>();

    /* list of identifiers to be registered */
    private static List<String> registrar = new ArrayList<>();

    public static void register(String name)
    {
        registrar.add(name);
    }

    /* sets up the callback */
    public static void bootstrapShaders()
    {
        ResourceProviderEvent.register(provider -> {
            Roundabout.LOGGER.info("got provider callback");
            for (String s : registrar)
            {
                ResourceLocation location = Roundabout.location("shaders/core/"+s+".json");
                if (provider.getResource(location).isEmpty())
                {
                    Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/core/{}.json\" (File Not Found)", s);
                    continue;
                }

                try
                {
                    ShaderInstance instance = new ShaderInstance(provider, s, DefaultVertexFormat.POSITION_COLOR_TEX);
                    shaderInstances.add(instance);
                    instance.markDirty();

                    Roundabout.LOGGER.info("Registered shader \"roundabout:shaders/core/{}.json\" successfully!", s);
                }
                catch (IOException e)
                {
                    Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/core/{}.json\" (IOException)", s);
                    continue;
                }
            }
        });
    }

    public static void renderShader(ShaderInstance instance, PoseStack matrix)
    {
        Matrix4f modelViewMatrix = matrix.last().pose();
        Matrix4f projectionMatrix = Minecraft.getInstance().gameRenderer.getProjectionMatrix(70);
        instance.apply();

        instance.safeGetUniform("ModelViewMat").set(modelViewMatrix);
        instance.safeGetUniform("ProjMat").set(projectionMatrix);

        instance.apply();
    }

    public static Supplier<ShaderInstance> getByIndex(int index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("index was below 0");

        if (index >= shaderInstances.size())
            throw new IndexOutOfBoundsException("index was not in range");

        return () -> Objects.requireNonNull(shaderInstances.get(index));
    }
}
