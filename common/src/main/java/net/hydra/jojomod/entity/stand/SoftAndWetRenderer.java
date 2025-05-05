package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SoftAndWetRenderer extends StandRenderer<SoftAndWetEntity> {

    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/manga.png");
    public SoftAndWetRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(SoftAndWetEntity entity) {
        return switch (entity.getSkin()) {
            default -> MANGA_SKIN;
        };
    }

    @Override
    public void render(SoftAndWetEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SoftAndWetEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}

