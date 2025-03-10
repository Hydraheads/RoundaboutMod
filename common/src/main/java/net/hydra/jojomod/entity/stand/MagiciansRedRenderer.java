package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class MagiciansRedRenderer extends StandRenderer<MagiciansRedEntity> {

    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red.png");
    public MagiciansRedRenderer(EntityRendererProvider.Context context) {
        super(context, new MagiciansRedModel<>(context.bakeLayer(ModEntityRendererClient.MAGICIANS_RED_LAYER)),0f);
    }

    @Override
    public ResourceLocation getTextureLocation(MagiciansRedEntity entity) {
            return PART_3_SKIN;
    }

    @Override
    public void render(MagiciansRedEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(MagiciansRedEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


}
