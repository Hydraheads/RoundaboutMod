package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.CaliforniaKingBedModel;
import net.hydra.jojomod.client.models.stand.CinderellaModel;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CaliforniaRenderer extends StandRenderer<CaliforniaKingBedEntity> {

    private static final ResourceLocation PART_8_SKIN = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/california_king_bed/base.png");
    private static final ResourceLocation SUNSHINE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/sunshine.png");
    private static final ResourceLocation EGYPT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/egypt.png");
    private static final ResourceLocation SPOOKY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/spooky.png");
    private static final ResourceLocation EXPERIENCE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/experience.png");
    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/blue.png");
    private static final ResourceLocation SPINE_ART = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/spine_art.png");
    private static final ResourceLocation HEAVEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/heaven.png");
    private static final ResourceLocation COVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/cover.png");
    private static final ResourceLocation CARD_SUIT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/card_suit.png");

    public CaliforniaRenderer(EntityRendererProvider.Context context) {
        super(context, new CaliforniaKingBedModel<>(context.bakeLayer(ModEntityRendererClient.CALIFORNIA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CaliforniaKingBedEntity entity) {
        return switch (entity.getSkin())
        {
            case (CaliforniaKingBedEntity.SUNSHINE) -> SUNSHINE;
            case (CaliforniaKingBedEntity.EGYPT) -> EGYPT;
            case (CaliforniaKingBedEntity.SPOOKY) -> SPOOKY;
            case (CaliforniaKingBedEntity.EXPERIENCE) -> EXPERIENCE;
            case (CaliforniaKingBedEntity.CARD_SUIT) -> CARD_SUIT;
            case (CaliforniaKingBedEntity.COVER) -> COVER;
            case (CaliforniaKingBedEntity.SPINE_ART) -> SPINE_ART;
            case (CaliforniaKingBedEntity.HEAVEN) -> HEAVEN;
            case (CaliforniaKingBedEntity.BLUE) -> BLUE;
            default -> PART_8_SKIN;
        };
    }

    @Override
    public void render(CaliforniaKingBedEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.sleep.isStarted()){
            factor = 1.5F;
        }
        matrixStack.translate(0,0.3F,0);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.68f * factor, 0.68f * factor, 0.68f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(CaliforniaKingBedEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }

}
