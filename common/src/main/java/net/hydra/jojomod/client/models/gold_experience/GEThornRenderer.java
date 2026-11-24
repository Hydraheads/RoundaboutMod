package net.hydra.jojomod.client.models.gold_experience;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.models.gold_experience.GEThornModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.gold_experience.GEThornEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GEThornRenderer extends EntityRenderer<GEThornEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("roundabout", "textures/entity/ge_thorn.png");
    private final GEThornModel model;

    public GEThornRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GEThornModel(context.bakeLayer(ModEntityRendererClient.GE_THORN_LAYER));
    }

    @Override
    public void render(GEThornEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // for some reason it spawns in fully grown but like.. only sometimes?? i'm just not gonna have it render on the very first tick.
        if (entity.tickCount == 0) {
            return;
        }
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.501D, 0.0D);

        float ageInTicks = entity.tickCount + partialTicks;
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);

        this.model.renderToBuffer(poseStack, buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GEThornEntity entity) {
        return TEXTURE;
    }
}