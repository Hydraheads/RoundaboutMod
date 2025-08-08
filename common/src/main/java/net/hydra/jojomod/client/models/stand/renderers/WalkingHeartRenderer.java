package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.D4CModel;
import net.hydra.jojomod.client.models.stand.TheWorldModel;
import net.hydra.jojomod.client.models.stand.WalkingHeartModel;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WalkingHeartRenderer extends StandRenderer<WalkingHeartEntity> {

    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/walking_heart/base.png");
    public WalkingHeartRenderer(EntityRendererProvider.Context context) {
        super(context, new WalkingHeartModel<>(context.bakeLayer(ModEntityRendererClient.WALKING_HEART_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(WalkingHeartEntity entity) {
        return switch (entity.getSkin()) {
            case (WalkingHeartEntity.MANGA_SKIN) -> MANGA;
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
