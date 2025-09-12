package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.WalkingHeartModel;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WalkingHeartRenderer extends StandRenderer<WalkingHeartEntity> {

    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/base.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/model.png");
    private static final ResourceLocation GOTHIC = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/gothic.png");
    private static final ResourceLocation PALE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/pale.png");
    private static final ResourceLocation VERDANT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/verdant.png");
    private static final ResourceLocation SPIDER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/spider.png");
    private static final ResourceLocation VALENTINE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/valentine.png");
    private static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/purple.png");
    public WalkingHeartRenderer(EntityRendererProvider.Context context) {
        super(context, new WalkingHeartModel<>(context.bakeLayer(ModEntityRendererClient.WALKING_HEART_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(WalkingHeartEntity entity) {
        return switch (entity.getSkin()) {
            case (WalkingHeartEntity.MANGA_SKIN) -> MANGA;
            case (WalkingHeartEntity.VALENTINE_SKIN) -> VALENTINE;
            case (WalkingHeartEntity.VERDANT_SKIN) -> VERDANT;
            case (WalkingHeartEntity.MODEL_SKIN) -> MODEL;
            case (WalkingHeartEntity.GOTHIC_SKIN) -> GOTHIC;
            case (WalkingHeartEntity.PALE_SKIN) -> PALE;
            case (WalkingHeartEntity.PURPLE_SKIN) -> PURPLE;
            case (WalkingHeartEntity.SPIDER_SKIN) -> SPIDER;
            default -> MANGA;
        };
    }

    @Override
    public void render(WalkingHeartEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(WalkingHeartEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
