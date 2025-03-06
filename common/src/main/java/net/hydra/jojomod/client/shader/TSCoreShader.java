package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.callback.ResourceProviderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Supplier;

/* class for handling core shaders & programs */
public class TSCoreShader {
    /* final list of instances */
    private static List<ShaderInstance> shaderInstances = new ArrayList<>();

    /* list of identifiers to be registered */
    private static List<String> registrar = new ArrayList<>();
    @Nullable
    private static ResourceProvider resourceProvider;

    public static void register(String name)
    {
        registrar.add(name);
        if (resourceProvider != null)
            registerShader(name);
    }

    private static void registerShader(String s)
    {
        if (resourceProvider == null)
            throw new MissingResourceException("Missing ResourceProvider", "", "");

        // if you're curious as to why the shaders are in minecraft, it's because it requires an extra mixin to fake paths in the resourceprovider
        // or just use a modded resource provider
        ResourceLocation location = new ResourceLocation("minecraft", "shaders/core/"+s+".json");
        if (resourceProvider.getResource(location).isEmpty())
        {
            Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/core/{}.json\" (File Not Found)", s);
            return;
        }

        try
        {
            ShaderInstance instance = new ShaderInstance(resourceProvider, s, DefaultVertexFormat.POSITION_COLOR_TEX);
            shaderInstances.add(instance);

            Roundabout.LOGGER.info("Registered shader \"roundabout:shaders/core/{}.json\" successfully!", s);
        }
        catch (IOException e)
        {
            Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/core/{}.json\" (IOException)", s);
            return;
        }
    }

    /* sets up the callback */
    public static void bootstrapShaders()
    {
        ResourceProviderEvent.register(provider -> {
            resourceProvider = provider;
            for (String s : registrar)
            {
                registerShader(s);
            }
        });
    }

    public static void clear()
    {
        for (ShaderInstance i : shaderInstances)
        { i.close(); }

        shaderInstances.clear();
    }

    // need to figure out positions in order to get this to work properly
    public static void renderShaderFullscreen(ShaderInstance instance)
    {
        Minecraft client = Minecraft.getInstance();
        RenderTarget mainRenderTarget = client.getMainRenderTarget();
        Window window = client.getWindow();

        instance.setSampler("DiffuseSampler", mainRenderTarget.getColorTextureId());
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, (float)window.getWidth(), (float)window.getHeight(), 0.0F, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        if (instance.MODEL_VIEW_MATRIX != null) {
            instance.MODEL_VIEW_MATRIX.set(new Matrix4f().translation(0.0F, 0.0F, -2000.0F));
        }

        if (instance.PROJECTION_MATRIX != null) {
            instance.PROJECTION_MATRIX.set(matrix4f);
        }

        instance.apply();

        float f = (float) window.getWidth();
        float g = (float) window.getHeight();
        float h = (float)mainRenderTarget.viewWidth / (float)mainRenderTarget.width;
        float i = (float)mainRenderTarget.viewHeight / (float)mainRenderTarget.height;

        Tesselator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, (double)g, 0.0).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex((double)f, (double)g, 0.0).uv(h, 0.0F).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex((double)f, 0.0, 0.0).uv(h, i).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, i).color(255, 255, 255, 255).endVertex();
        BufferUploader.draw(bufferBuilder.end());

        instance.clear();
    }

    // Call instance.clear() when you're done rendering it.
    public static void startShaderRender(ShaderInstance instance)
    {
        Minecraft client = Minecraft.getInstance();
        RenderTarget mainRenderTarget = client.getMainRenderTarget();
        Window window = client.getWindow();

        instance.setSampler("DiffuseSampler", mainRenderTarget.getColorTextureId());
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, (float)window.getWidth(), (float)window.getHeight(), 0.0F, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        if (instance.MODEL_VIEW_MATRIX != null) {
            instance.MODEL_VIEW_MATRIX.set(new Matrix4f().translation(0.0F, 0.0F, -2000.0F));
        }

        if (instance.PROJECTION_MATRIX != null) {
            instance.PROJECTION_MATRIX.set(matrix4f);
        }

        instance.apply();
    }

    @Nullable
    public static Supplier<ShaderInstance> getByIndex(int index)
    {
        if (index >= shaderInstances.size() || index < 0)
            return null;

        return () -> Objects.requireNonNull(shaderInstances.get(index));
    }
}
