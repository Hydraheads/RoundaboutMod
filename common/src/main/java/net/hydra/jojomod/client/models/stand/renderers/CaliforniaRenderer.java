package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.CaliforniaKingBedModel;
import net.hydra.jojomod.client.models.stand.CinderellaModel;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CaliforniaRenderer extends StandRenderer<CaliforniaKingBedEntity> {

    private static final ResourceLocation PART_8_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/base.png");

    public CaliforniaRenderer(EntityRendererProvider.Context context) {
        super(context, new CaliforniaKingBedModel<>(context.bakeLayer(ModEntityRendererClient.CALIFORNIA_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CaliforniaKingBedEntity entity) {
        switch (entity.getSkin())
        {
            default:
                return PART_8_SKIN;
        }
    }

    @Override
    public void render(CaliforniaKingBedEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
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
