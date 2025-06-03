package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SoftAndWetBaseRenderer<M extends StandEntity> extends StandRenderer<SoftAndWetEntity> {

    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/manga.png");
    private static final ResourceLocation LIGHT_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/light.png");
    private static final ResourceLocation BETA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/beta.png");
    private static final ResourceLocation KING_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/king.png");
    private static final ResourceLocation DROWNED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/drowned.png");
    private static final ResourceLocation FIGURE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/figure.png");
    private static final ResourceLocation STRIPED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/striped.png");
    private static final ResourceLocation DEBUT_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/debut.png");

    public SoftAndWetBaseRenderer(EntityRendererProvider.Context context, StandModel root) {
        super(context, root,0f);
        this.addLayer(new SoftAndWetDrownedGlowingLayer<>(this));
    }

    @Override public ResourceLocation getTextureLocation(SoftAndWetEntity entity) {
        return switch (entity.getSkin()) {
            case (SoftAndWetEntity.MANGA_SKIN) -> MANGA_SKIN;
            case (SoftAndWetEntity.BETA_SKIN) -> BETA_SKIN;
            case (SoftAndWetEntity.KING_SKIN) -> KING_SKIN;
            case (SoftAndWetEntity.DROWNED_SKIN), (SoftAndWetEntity.DROWNED_SKIN_2) -> DROWNED_SKIN;
            case (SoftAndWetEntity.FIGURE_SKIN) -> FIGURE_SKIN;
            case (SoftAndWetEntity.STRIPED) -> STRIPED_SKIN;
            case (SoftAndWetEntity.DEBUT) -> DEBUT_SKIN;
            default -> LIGHT_SKIN;
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

