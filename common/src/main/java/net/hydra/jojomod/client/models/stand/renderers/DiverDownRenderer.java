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

    public DiverDownRenderer(EntityRendererProvider.Context context) {
        super(context, new DiverDownModel<>(context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        byte BT = entity.getSkin();
        switch (BT) {
            case DiverDownEntity.PART_6 -> {return PART_6;}
            case DiverDownEntity.LAVA_DIVER -> {return LAVA_DIVER;}
            case DiverDownEntity.RED_DIVER -> {return RED_DIVER;}
            case DiverDownEntity.ORANGE_DIVER -> {return ORANGE_DIVER;}
            case DiverDownEntity.TREASURE_DIVER -> {return TREASURE_DIVER;}
            default -> {return PART_6;}
        }
    }

    private static final ResourceLocation PART_6 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/base.png");
    private static final ResourceLocation LAVA_DIVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/lavadiver.png");
    private static final ResourceLocation RED_DIVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/reddiver.png");
    private static final ResourceLocation ORANGE_DIVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/orangediver.png");
    private static final ResourceLocation TREASURE_DIVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/diver_down/treasurediver.png");

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
