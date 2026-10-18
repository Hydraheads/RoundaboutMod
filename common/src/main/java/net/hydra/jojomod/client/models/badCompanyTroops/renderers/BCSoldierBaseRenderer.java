package net.hydra.jojomod.client.models.badCompanyTroops.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.badCompanyTroops.BCSoldier;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BCSoldierBaseRenderer extends StandRenderer<BCSoldier> {

    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/anime.png");

    public BCSoldierBaseRenderer(EntityRendererProvider.Context context, StandModel<BCSoldier> entityModel, float f) {
        super(context, entityModel,f);
    }

    @Override
    public ResourceLocation getTextureLocation(BCSoldier entity) {

        return ANIME;
    }

    @Override
    public void render(BCSoldier mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumer, int i) {
        float factor = 0.3f;
        if (mobEntity.isDisplay) {
            factor = 0.9f;
        }
        if(mobEntity.isBaby()){
            matrixStack.scale(0.60f * factor, 0.60f * factor, 0.60f * factor);
        } else {
            matrixStack.scale(0.80f * factor, 0.80f * factor, 0.80f * factor);
        }
        if (mobEntity.getBundle()) {
            renderExtraSoldier(1, 0, 0, mobEntity, f, g, matrixStack, vertexConsumer, i);
        }


        super.render(mobEntity, f, g, matrixStack, vertexConsumer, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BCSoldier entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }

    private void renderExtraSoldier(float x, float y, float z, BCSoldier mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumer, int i) {
        matrixStack.pushPose();
        matrixStack.translate( x, y, z );

        this.setupRotations(mobEntity, matrixStack, mobEntity.tickCount + g, f, g);
        //matrixStack.mulPose((Axis.ZP.rotationDegrees(180.0F)));
        this.scale(mobEntity, matrixStack, g);

        VertexConsumer vertexConsumerResult = vertexConsumer.getBuffer(RenderType.entityTranslucent(getTextureLocation(mobEntity)));
        this.model.renderToBuffer(matrixStack, vertexConsumerResult, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, i);
        matrixStack.popPose();
    }
}
