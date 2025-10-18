package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.ChairRattModel;
import net.hydra.jojomod.client.models.stand.ReddModel;
import net.hydra.jojomod.entity.stand.ChairRattEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.ReddEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ChairRattRenderer extends StandRenderer<ChairRattEntity> {
    private static final ResourceLocation CHAIR_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/chair.png");
    private static final ResourceLocation KING_RAT_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/king_chair.png");

    public ChairRattRenderer(EntityRendererProvider.Context context) {
        super(context, new ChairRattModel(context.bakeLayer(ModEntityRendererClient.CHAIR_RATT_LAYER)), 0f);
    }

    public ResourceLocation getTextureLocation(ChairRattEntity entity) {
        if (entity.getSavedSkin() == RattEntity.KING_RAT_SKIN) {return KING_RAT_SKIN;}
        return CHAIR_SKIN;
    }

    public void render(ChairRattEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.75F,0.75F,0.75F);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable

    protected RenderType getRenderType(ChairRattEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
