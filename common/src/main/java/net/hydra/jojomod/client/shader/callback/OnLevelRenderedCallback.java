package net.hydra.jojomod.client.shader.callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;

public interface OnLevelRenderedCallback {
    void onLevelRendered(PoseStack matrices, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightTexture lightTexture, Matrix4f projectionMatrix);
}
