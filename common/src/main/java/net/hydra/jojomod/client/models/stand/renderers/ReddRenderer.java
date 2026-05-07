package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.RattModel;
import net.hydra.jojomod.client.models.stand.ReddModel;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.ReddEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ReddRenderer extends StandRenderer<ReddEntity> {
    private static final ResourceLocation REDD_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/redd.png");


    public ReddRenderer(EntityRendererProvider.Context context) {
        super(context, new ReddModel(context.bakeLayer(ModEntityRendererClient.REDD_LAYER)), 0f);
    }

    public ResourceLocation getTextureLocation(ReddEntity entity) {
        return REDD_SKIN;
    }


    public void render(ReddEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.75F,0.75F,0.75F);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable

    protected RenderType getRenderType(ReddEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
