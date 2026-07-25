package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.SilverChariotModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SilverChariotRenderer extends StandRenderer<SilverChariotEntity> {

    private static final ResourceLocation DEFAULT_SILVER_CHARIOT = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/silver_chariot.png");

    public SilverChariotRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverChariotModel<>(context.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SilverChariotEntity entity) {
        // Yes, I am aware that it is not proper to have only one case in a switch statement
        return switch (entity.getSkin()) {
            default -> DEFAULT_SILVER_CHARIOT;
        };
    }

    @Override
    public void render(SilverChariotEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(SilverChariotEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, translucent, showOutline);
    }
}
