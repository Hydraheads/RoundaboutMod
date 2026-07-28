package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.AnubisHumanoidModel;
import net.hydra.jojomod.entity.stand.AnubisEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class AnubisRenderer extends StandRenderer<AnubisEntity> {

    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/anubis/anime_human.png");
    public AnubisRenderer(EntityRendererProvider.Context context) {
        super(context, new AnubisHumanoidModel<>(context.bakeLayer(ModEntityRendererClient.ANUBIS)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(AnubisEntity entity) {
        return ANIME;
    }

    @Override
    public void render(AnubisEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(AnubisEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
