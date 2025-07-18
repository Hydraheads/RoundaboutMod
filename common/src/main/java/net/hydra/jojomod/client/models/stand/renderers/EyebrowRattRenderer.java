package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.EyebrowRattModel;
import net.hydra.jojomod.client.models.stand.RattModel;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class EyebrowRattRenderer extends StandRenderer<RattEntity> {
    private static final ResourceLocation REDD_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/redd.png");


    public EyebrowRattRenderer(EntityRendererProvider.Context context) {
        super(context, new EyebrowRattModel<>(context.bakeLayer(ModEntityRendererClient.EYEBROW_RATT_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(RattEntity entity) {
        return REDD_SKIN;
    }

    @Override
    public void render(RattEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(RattEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
