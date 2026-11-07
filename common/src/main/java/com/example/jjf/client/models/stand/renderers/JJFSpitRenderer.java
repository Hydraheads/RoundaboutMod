package com.example.jjf.client.models.stand.renderers;

import com.example.jjf.entity.projectile.JJFSpitEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class JJFSpitRenderer extends EntityRenderer<JJFSpitEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    "minecraft",
                    "textures/entity/llama/spit.png"
            );

    private final LlamaSpitModel<JJFSpitEntity> model;

    public JJFSpitRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);

        model = new LlamaSpitModel<>(
                context.bakeLayer(
                        ModelLayers.LLAMA_SPIT
                )
        );
    }

    @Override
    public void render(
            JJFSpitEntity entity,
            float yaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light
    ) {
        poseStack.pushPose();

        model.renderToBuffer(
                poseStack,
                buffer.getBuffer(
                        model.renderType(TEXTURE)
                ),
                light,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1.0F
        );

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(
            JJFSpitEntity entity
    ) {
        return TEXTURE;
    }
}
