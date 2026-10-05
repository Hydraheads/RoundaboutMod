package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.renderers.DiverDownBaseRenderer;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class DiverLegsLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private record LegPair(ModelPart leftLeg, ModelPart rightLeg) {}

    private final Map<Byte, LegPair> skinLegModels = new HashMap<>();
    private final LegPair defaultLegs;

    public DiverLegsLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);

        this.defaultLegs = bakeDiverLegs(context, ModEntityRendererClient.DIVER_DOWN_LAYER);
        this.skinLegModels.put((byte) 0, this.defaultLegs);
        this.skinLegModels.put(DiverDownEntity.BETA_DIVER, bakeDiverLegs(context, ModEntityRendererClient.DIVER_DOWN_BETA_LAYER));
        this.skinLegModels.put(DiverDownEntity.WORLD_DIVER, bakeDiverLegs(context, ModEntityRendererClient.DIVER_DOWN_WORLD_LAYER));
        // In the future, follow this template to add more
        // this.skinLegModels.put(DiverDownEntity.[MODEL_NAME], bakeDiverLegs(context, ModEntityRendererClient.[MODEL_NAME]_LAYER));
    }

    private static LegPair bakeDiverLegs(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        ModelPart standRoot = context.bakeLayer(layer);
        ModelPart standLegs = standRoot.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("legs");
        return new LegPair(standLegs.getChild("left_leg"), standLegs.getChild("right_leg"));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        if (entity == null || entity.isInvisible()) return;

        if (entity instanceof StandUser su && su.roundabout$hasDiverLegs()) {
            if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) return;

            StandUser diverOwner = null;
            if (su.roundabout$getStandPowers() instanceof PowersDiverDown) {
                diverOwner = su;
            } else if (su.roundabout$getDiverUser() != null && su.roundabout$getDiverUser().getSelf() instanceof StandUser ownerSu) {
                diverOwner = ownerSu;
            }

            byte skinByte = (diverOwner != null) ? diverOwner.roundabout$getStandSkin() : (byte) 0;
            ResourceLocation texture = DiverDownBaseRenderer.getSkin(skinByte);

            LegPair legs = this.skinLegModels.getOrDefault(skinByte, this.defaultLegs);

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

            //haha they're wearing diver jeans XD
            legs.rightLeg().setRotation(0.0F, 0.0F, 0.0F);
            legs.leftLeg().setRotation(0.0F, 0.0F, 0.0F);

            legs.rightLeg().setPos(0.0F, -1.0F, 0.0F);
            legs.leftLeg().setPos(0.0F, -1.0F, 0.0F);

            // Haha they're wearing diver jeans XD
            poseStack.pushPose();
            this.getParentModel().rightLeg.translateAndRotate(poseStack);
            poseStack.scale(1.09F, 1.09F, 1.09F);
            legs.rightLeg().render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);
            poseStack.popPose();
            poseStack.pushPose();
            this.getParentModel().leftLeg.translateAndRotate(poseStack);
            poseStack.scale(1.09F, 1.09F, 1.09F);
            legs.leftLeg().render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);
            poseStack.popPose();
        }
    }
}