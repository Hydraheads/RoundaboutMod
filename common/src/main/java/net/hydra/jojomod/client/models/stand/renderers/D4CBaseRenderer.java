package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class D4CBaseRenderer<M extends StandEntity> extends StandRenderer<D4CEntity>{

    private static final ResourceLocation BASE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/base.png");

    public D4CBaseRenderer(EntityRendererProvider.Context context, StandModel root) {
        super(context, root,0f);
    }


    public static ResourceLocation getSkin(byte BT){
        if (BT == D4CEntity.BASE){
            return BASE;
        }
        return BASE;
    }

    @Override
    public ResourceLocation getTextureLocation(D4CEntity entity) {
        byte BT = entity.getSkin();
        return getSkin(BT);
    }

    @Override
    public void render(D4CEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(D4CEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
