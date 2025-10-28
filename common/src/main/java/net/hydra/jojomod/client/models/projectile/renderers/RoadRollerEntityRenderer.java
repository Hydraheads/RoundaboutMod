package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.RoadRollerModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class RoadRollerEntityRenderer extends LivingEntityRenderer<RoadRollerEntity, RoadRollerModel<RoadRollerEntity>> {

    public RoadRollerEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new RoadRollerModel<>($$0.bakeLayer(ModEntityRendererClient.ROAD_ROLLER_LAYER)), 0.0F);
    }

    @Override
    protected boolean shouldShowName(RoadRollerEntity entity) {
        return false;
    }

    @Override
    public void render(RoadRollerEntity $$0, float $$1, float $$2, PoseStack poseStack, MultiBufferSource $$4, int $$5) {
        if (!ClientUtil.getScreenFreeze()) {
            poseStack.pushPose();

            ResourceLocation RL = this.getTextureLocation($$0);
            ClientUtil.saveBufferTexture = RL;

            if ($$0.getVehicle() instanceof StandEntity SE) {
                poseStack.scale(1.8f, 1.8f, 1.8f);
                poseStack.translate(0, 0.07, 0);
            } else {
                poseStack.scale(3f, 3f, 3f);
            }

            super.render($$0, $$1, $$2, poseStack, $$4, $$5);

            VertexConsumer $$6 = $$4.getBuffer(this.model.renderType(RL));

            poseStack.popPose();
        }
    }

    @Override
    protected void setupRotations(RoadRollerEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        if (!(entity.getVehicle() instanceof net.hydra.jojomod.entity.stand.StandEntity)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        }
        else {
            float savedYaw = entity.getLastYaw();
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - savedYaw));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(RoadRollerEntity entity) {
        switch (entity.getCrackiness()) {
            case 1:
                return ModEntities.ROAD_ROLLER_TEXTURE_CRACKED_MEDIUM;
            case 2:
                return ModEntities.ROAD_ROLLER_TEXTURE_CRACKED_HIGH;
            default:
                return ModEntities.ROAD_ROLLER_TEXTURE;
        }
    }
}
