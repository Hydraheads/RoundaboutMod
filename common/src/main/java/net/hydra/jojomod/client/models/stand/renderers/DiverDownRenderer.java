package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.DiverDownModel;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DiverDownRenderer extends StandRenderer<DiverDownEntity>{


    @Override public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return main_skin;
    }

    private static final ResourceLocation main_skin = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/base.png");

    public DiverDownRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDownModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_LAYER)), 0f);
    }
    @Override
    public void render(DiverDownEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(DiverDownEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
