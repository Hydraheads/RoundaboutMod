package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.KillerQueenModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class KillerQueenRenderer extends StandRenderer<KillerQueenEntity>{


    @Override public ResourceLocation getTextureLocation(KillerQueenEntity entity) {
        return main_skin;
    }

    private static final ResourceLocation main_skin = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/anime.png");

    public KillerQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new KillerQueenModel<>(context.bakeLayer(ModEntityRendererClient.KILLER_QUEEN_LAYER)), 0f);
    }
    @Override
    public void render(KillerQueenEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(KillerQueenEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
