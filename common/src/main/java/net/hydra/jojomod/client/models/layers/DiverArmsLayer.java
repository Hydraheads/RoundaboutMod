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

public class DiverArmsLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private record ArmPair(ModelPart leftArm, ModelPart rightArm) {}

    private final Map<Byte, ArmPair> skinArmModels = new HashMap<>();
    private final ArmPair defaultArms;

    public DiverArmsLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);

        this.defaultArms = bakeDiverArms(context, ModEntityRendererClient.DIVER_DOWN_LAYER);
        this.skinArmModels.put((byte) 0, this.defaultArms);
        this.skinArmModels.put(DiverDownEntity.BETA_DIVER, bakeDiverArms(context, ModEntityRendererClient.DIVER_DOWN_BETA_LAYER));
        this.skinArmModels.put(DiverDownEntity.WORLD_DIVER, bakeDiverArms(context, ModEntityRendererClient.DIVER_DOWN_WORLD_LAYER));
        // In the future, follow this template to add more
        // this.skinArmModels.put(DiverDownEntity.[MODEL_NAME], bakeDiverArms(context, ModEntityRendererClient.[MODEL_NAME]_LAYER));
    }

    private static ArmPair bakeDiverArms(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        ModelPart standRoot = context.bakeLayer(layer);
        ModelPart standArms = standRoot.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
        return new ArmPair(standArms.getChild("left_arm"), standArms.getChild("right_arm"));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        if (entity == null || entity.isInvisible()) return;

        if (entity instanceof StandUser su && su.roundabout$hasDiverArms()) {
            if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) return;

            StandUser diverOwner = null;
            if (su.roundabout$getStandPowers() instanceof PowersDiverDown) {
                diverOwner = su;
            } else if (su.roundabout$getDiverUser() != null && su.roundabout$getDiverUser().getSelf() instanceof StandUser ownerSu) {
                diverOwner = ownerSu;
            }

            if (diverOwner != null) {
                byte skin = diverOwner.roundabout$getStandSkin();
                ResourceLocation texture = DiverDownBaseRenderer.getSkin(skin);
                ArmPair arms = skinArmModels.getOrDefault(skin, defaultArms);

                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

                arms.rightArm().setRotation(0.0F, 0.0F, 0.0F);
                arms.leftArm().setRotation(0.0F, 0.0F, 0.0F);
                arms.rightArm().setPos(1.0F, -1.0F, 0.0F);
                arms.leftArm().setPos(-1.0F, -1.0F, 0.0F);

                poseStack.pushPose();

                this.getParentModel().leftArm.translateAndRotate(poseStack);
                poseStack.scale(1.05F, 1.02F, 1.05F);
                arms.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);

                poseStack.popPose();
                poseStack.pushPose();

                this.getParentModel().rightArm.translateAndRotate(poseStack);
                poseStack.scale(1.05F, 1.02F, 1.05F);
                arms.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);

                poseStack.popPose();
            }
        }
    }
}