package net.hydra.jojomod.client.shader.callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ShaderEvents {
    private static final List<OnShaderLoadCallback> registeredResourceProvider = new ArrayList<>();
    private static final List<OnLevelRenderedCallback> registeredLevelRendered = new ArrayList<>();

    public static void registerResourceProvider(OnShaderLoadCallback listener)
    {
        registeredResourceProvider.add(listener);
    }

    public static void registerOnLevelRendered(OnLevelRenderedCallback listener)
    {
        registeredLevelRendered.add(listener);
    }

    public static void invokeOnResourceProvider(ResourceProvider provider)
    {
        for (OnShaderLoadCallback c : registeredResourceProvider)
            c.onResourceProvider(provider);
    }

    public static void invokeOnLevelRendered(PoseStack matrices, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightTexture lightTexture, Matrix4f projectionMatrix)
    {
        for (OnLevelRenderedCallback c : registeredLevelRendered)
            c.onLevelRendered(matrices, partialTick, finishNanoTime, renderBlockOutline, camera, renderer, lightTexture, projectionMatrix);
    }
}