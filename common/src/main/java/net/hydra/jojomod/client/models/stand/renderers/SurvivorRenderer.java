package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.StarPlatinumModel;
import net.hydra.jojomod.client.models.stand.SurvivorModel;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SurvivorRenderer<M extends StandEntity> extends StandRenderer<SurvivorEntity> {

    private static final ResourceLocation BASE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/base.png");
    private static final ResourceLocation BASE_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/base.png");

    public SurvivorRenderer(EntityRendererProvider.Context context) {
        super(context, new SurvivorModel<>(context.bakeLayer(ModEntityRendererClient.SURVIVOR_LAYER)),0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SurvivorEntity entity) {
        byte BT = entity.getSkin();
        if (BT == TheWorldEntity.PART_3_SKIN)
            return BASE;
        return BASE;
    }

    @Override
    public void render(SurvivorEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.8F + mobEntity.getRandomSize();
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SurvivorEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


}

