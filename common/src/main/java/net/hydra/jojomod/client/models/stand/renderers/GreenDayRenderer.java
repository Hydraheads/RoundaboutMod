package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GreenDayModel;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GreenDayRenderer extends StandRenderer<GreenDayEntity> {

    private static final ResourceLocation PART_FIVE_GREEN_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/part_four_green_day.png");


    public GreenDayRenderer(EntityRendererProvider.Context context) {
        super(context, new GreenDayModel<>(context.bakeLayer(ModEntityRendererClient.GREEN_DAY_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(GreenDayEntity entity) {
        return switch (entity.getSkin()) {
            case (GreenDayEntity.PART_FIVE_GREEN_DAY) -> PART_FIVE_GREEN_DAY;
            default -> PART_FIVE_GREEN_DAY;

        };
    }

    @Override
    public void render(GreenDayEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.getUser() != null)
            if (((StandUser)mobEntity.getUser()).roundabout$isParallelRunning())
                if (mobEntity.getUser() != Minecraft.getInstance().player)
                    return;

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
    protected RenderType getRenderType(GreenDayEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public float getStandOpacity(GreenDayEntity entity) {
        float base = super.getStandOpacity(entity);

        if (!entity.hasUser())
            return base;

        if (((StandUser)entity.getUser()).roundabout$isParallelRunning())
            return base/2f;
        else
            return base;
    }
}
