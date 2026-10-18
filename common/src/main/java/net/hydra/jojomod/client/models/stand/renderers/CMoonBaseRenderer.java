package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.CMoonModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.CMoonEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CMoonBaseRenderer<M extends StandEntity> extends StandRenderer<CMoonEntity> {

    private static final ResourceLocation Anime = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/cmoon/CMoonTexture.png");

    public CMoonBaseRenderer(EntityRendererProvider.Context context, StandModel root) {
        super(context, root,0f);
    }

    public static ResourceLocation getSkin(byte BT){
        if (BT == CMoonEntity.Anime){
            return Anime;
        }
        return Anime;
    }

    @Override
    public ResourceLocation getTextureLocation(CMoonEntity entity) {
        byte BT = entity.getSkin();
        return getSkin(BT);
    }

    @Override
    public void render(CMoonEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(CMoonEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


}

