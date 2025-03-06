package net.hydra.jojomod.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.callback.ResourceProviderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

// TODO: fix fabulous rendertargets
// fabulous graphics mode should be used for fixed transparency
// but fabulous splits up the rendertargets across several framebuffers to fix transparency
// so ill have to target all of them one day

// this sorta works rn tho

/* class for handling post shaders & programs */
public class TSPostShader {
    /* final list of instances */
    private static final List<PostShaderWithName> shaderInstances = new ArrayList<>();

    /* list of identifiers to be registered */
    private static final List<String> registrar = new ArrayList<>();

    public static void register(String name)
    {
        registrar.add(name);
    }

    /* sets up the callback */
    public static void bootstrapShaders()
    {
        register("fog");

        ResourceProviderEvent.register(provider -> {
            shaderInstances.clear();
            for (String s : registrar)
            {
                ResourceLocation location = Roundabout.location("shaders/post/"+s+".json");
                if (provider.getResource(location).isEmpty())
                {
                    Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/post/{}.json\" (File Not Found)", s);
                    continue;
                }

                try
                {
                    PostChain instance = new PostChain(
                            Minecraft.getInstance().getTextureManager(),
                            Minecraft.getInstance().getResourceManager(),
                            Minecraft.getInstance().getMainRenderTarget(),
                            location);

                    shaderInstances.add(new PostShaderWithName(s, instance));

                    Roundabout.LOGGER.info("Registered shader \"roundabout:shaders/post/{}.json\" successfully!", s);
                }
                catch (IOException e)
                {
                    Roundabout.LOGGER.warn("FAILED to load shader \"roundabout:shaders/post/{}.json\" (IOException [\"{}\"])", s, e.getMessage());
                    continue;
                }
            }

            FOG_SHADER = getByName("fog");
            assert FOG_SHADER != null; // will cause a nullptr exception if FOG_SHADER is null while we try to access passes
            // given fog is a pretty key component, crashing is warranted
            FOG_SHADER_PASSES = getPasses(FOG_SHADER.get());
        });
    }

    public static void renderShader(PostShaderWithName instance, float tickDelta)
    {
        Minecraft client = Minecraft.getInstance();

        if (client.level != null)
        {
            PostChain chain = instance.getChain();
            chain.resize(client.getMainRenderTarget().width, client.getMainRenderTarget().height);
            chain.process(tickDelta);
        }
    }

    public static void setFloatUniform(List<PostPass> passes, String name, float value)
    {
        for (PostPass p : passes)
        {
            Uniform u = p.getEffect().getUniform(name);
            if (u != null)
            {
                u.set(value);
            }
        }
    }

    public static void setVec3Uniform(List<PostPass> passes, String name, Vector3f value)
    {
        for (PostPass p : passes)
        {
            Uniform u = p.getEffect().getUniform(name);
            if (u != null)
            {
                u.set(value);
            }
        }
    }

    public static void setSamplerUniform(List<PostPass> passes, String name, int value)
    {
        for (PostPass p : passes)
        {
            Uniform u = p.getEffect().getUniform(name);
            if (u != null)
            {
                u.set(value);
            }
        }
    }

    // TODO: add the rest of the uniform types

    @Nullable
    public static List<PostPass> getPasses(PostShaderWithName instance)
    {
        PostChain chain = instance.getChain();

        try
        {
            Field passesField = chain.getClass().getDeclaredField("passes");
            passesField.setAccessible(true);

            List<PostPass> passes = (List<PostPass>)passesField.get(chain);
            return passes;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Supplier<PostShaderWithName> getByIndex(int index)
    {
        if (index < 0)
            throw new IndexOutOfBoundsException("index was below 0");

        if (index >= shaderInstances.size())
            throw new IndexOutOfBoundsException("index was not in range");

        return () -> Objects.requireNonNull(shaderInstances.get(index));
    }

    @Nullable
    public static Supplier<PostShaderWithName> getByName(String string)
    {
        for (PostShaderWithName s : shaderInstances)
        {
            if (s.getName().equals(string))
                return () -> s;
        }

        return null;
    }

    @Nullable
    public static Supplier<PostShaderWithName> FOG_SHADER;
    @Nullable
    public static List<PostPass> FOG_SHADER_PASSES;
}
