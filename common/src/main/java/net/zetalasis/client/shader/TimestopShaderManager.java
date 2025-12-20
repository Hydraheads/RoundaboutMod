package net.zetalasis.client.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.hydra.jojomod.access.IShaderGameRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TimestopShaderManager {
    private static final List<Bubble> BUBBLE_QUEUE = new ArrayList<>();
    public static RenderTarget TIMESTOP_DEPTH_BUFFER = new TextureTarget(1920, 1080, true, true);

    public static void renderBubble(Bubble bubble)
    {
        BUBBLE_QUEUE.add(bubble);
    }

    public static void renderBubble(float tickDelta, Vec3 pos, float radius, Vec3 tint, boolean altType)
    {
        Minecraft client = Minecraft.getInstance();
        Camera camera = client.gameRenderer.getMainCamera();

        float fov = ((IShaderGameRenderer)client.gameRenderer).roundabout$getFov(
                camera,
                tickDelta,
                true
        );

        Vector3f camPos = camera.getPosition().toVector3f();
        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("CameraPos", new Vector4f(camPos.x, camPos.y, camPos.z, fov));

        Quaternionf cameraRot = camera.rotation();
        Vector4f rot = new Vector4f(cameraRot.x, cameraRot.y, cameraRot.z, cameraRot.w);

        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("CameraRot", rot);
        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("BubblePos", pos.toVector3f());
        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("BubbleRadius", radius);
        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("BubbleTint", tint.toVector3f());
        RPostShaderRegistry.TIMESTOP.roundabout$setUniform("DesaturateAllInside", altType ? 1.f : 0.f);

        RPostShaderRegistry.TIMESTOP.roundabout$process(tickDelta);
    }

    public static void renderAll(float tickDelta)
    {
        BUBBLE_QUEUE.forEach(bubble -> renderBubble(tickDelta, bubble.pos, bubble.radius, bubble.tint, bubble.altType));
        BUBBLE_QUEUE.clear();
    }

    public record Bubble(Vec3 pos, float radius, Vec3 tint, boolean altType) {}
}