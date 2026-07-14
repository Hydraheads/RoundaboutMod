package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class KingCrimsonRenderer<M extends StandEntity> extends StandRenderer<KingCrimsonEntity>{

    private static final ResourceLocation PART_5_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/manga.png");

    public KingCrimsonRenderer(EntityRendererProvider.Context context) {
        super(context, new KingCrimsonModel<>(context.bakeLayer(ModEntityRendererClient.KING_CRIMSON_LAYER)),0f);
    }


    @Override
    public ResourceLocation getTextureLocation(KingCrimsonEntity entity) {
        byte BT = entity.getSkin();
        if (BT == KingCrimsonEntity.MANGA_SKIN){
            return MANGA_SKIN;
        }
        return PART_5_SKIN;
    }

    @Override
    public void render(KingCrimsonEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(KingCrimsonEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
