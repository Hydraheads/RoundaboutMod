package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.PurpleHazeModel;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PurpleHazeRenderer extends StandRenderer<PurpleHazeEntity> {

    public static final ResourceLocation DEFAULT_PURPLE_HAZE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/purple_haze/default_purple_haze.png");
    public static final ResourceLocation MIG_PLAGUE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/purple_haze/mig_plague.png");

    public PurpleHazeRenderer(EntityRendererProvider.Context context) {
        super(context, new PurpleHazeModel<>(context.bakeLayer(ModEntityRendererClient.PURPLE_HAZE_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(PurpleHazeEntity entity) {
        switch (entity.getSkin())
        {
            case (PurpleHazeEntity.MIG_PLAGUE):
                return MIG_PLAGUE;
            default:
                return DEFAULT_PURPLE_HAZE;
        }
    }


    @Override
    public void render(PurpleHazeEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.8f * factor, 0.8f * factor, 0.8f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(PurpleHazeEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
