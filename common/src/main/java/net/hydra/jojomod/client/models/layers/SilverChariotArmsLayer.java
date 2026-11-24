package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.stand.SilverChariotModel;
import net.hydra.jojomod.client.models.stand.renderers.SilverChariotBaseRenderer;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SilverChariotArmsLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    public static SilverChariotArmsLayer<?, ?> INSTANCE;
    private record ArmPair(ModelPart leftArm, ModelPart rightArm) {}
    private record ArmModels(ArmPair firstPerson, ArmPair thirdPerson) {}

    private final Map<Byte, ArmPair> skinArmModels = new HashMap<>();
    private final ArmPair defaultArms;

    public SilverChariotArmsLayer() {
        super(null);
        INSTANCE = this;
        this.defaultArms = bakeSilverChariotArms(SilverChariotModel.getTexturedModelData().bakeRoot());
        this.skinArmModels.put((byte) 0, this.defaultArms);
        this.skinArmModels.put(SilverChariotEntity.PHARAOH, bakeSilverChariotArms(SilverChariotModel.getTexturedModelData().bakeRoot()));
    }
    public SilverChariotArmsLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.defaultArms = null;
    }

    private static ArmPair bakeSilverChariotArms(ModelPart standRoot) {
        ModelPart standArms = standRoot.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
        return new ArmPair(standArms.getChild("left_arm"), standArms.getChild("right_arm"));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity == null || entity.isInvisible() || !MainUtil.isHumanoid2(entity)) return;
        if (ModStrayModels.chariotArmsLayer == null) return;

        if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) return;
        if (entity instanceof StandUser SU) {
            byte skin = SU.roundabout$getStandSkin();
            ResourceLocation texture = SilverChariotBaseRenderer.getSkin(skin);
            ArmPair arms = (ArmPair) ModStrayModels.chariotArmsLayer.skinArmModels.getOrDefault(skin, ModStrayModels.chariotArmsLayer.defaultArms);

            this.resetModelParts(arms.rightArm(), true);
            this.resetModelParts(arms.leftArm(), false);

            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

            arms.rightArm().setRotation(0.0F, 0.0F, 0.0F);
            arms.leftArm().setRotation(0.0F, 0.0F, 0.0F);
            arms.rightArm().setPos(1.1F, -1.0F, 0.0F);
            arms.leftArm().setPos(-1.1F, -1.0F, 0.0F);

            if (SU.roundabout$getStandPowers() instanceof PowersSilverChariot PSC && PSC.hasHandsOut()) {
                // Has to use the left arm for part 5 skins
                if (SilverChariotEntity.isPart5Skin(skin) || (entity instanceof Player player && player.getMainArm() == HumanoidArm.LEFT)) {
                    poseStack.pushPose();
                    this.getParentModel().leftArm.translateAndRotate(poseStack);
                    // poseStack.scale(1.05F, 1.02F, 1.05F);
                    poseStack.scale(1.15F, 1.12F, 1.15F);
                    arms.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);
                    poseStack.popPose();
                } else {
                    poseStack.pushPose();
                    this.getParentModel().rightArm.translateAndRotate(poseStack);
                    // poseStack.scale(1.05F, 1.02F, 1.05F);
                    poseStack.scale(1.15F, 1.12F, 1.15F);
                    arms.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.70F);
                    poseStack.popPose();
                }
            }
        }
    }

    public static void renderFirstPersonArm(PoseStack poseStack, MultiBufferSource bufferSource,
                                            int packedLight, AbstractClientPlayer player,
                                            ModelPart playerArm, boolean isRightArm, float alpha) {
        if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) return;
        if (alpha <= 0.0F) return;

        StandUser SU = (StandUser) player;

        byte skin = SU.roundabout$getStandSkin();
        ResourceLocation texture = SilverChariotBaseRenderer.getSkin(skin);
        ArmPair arms = INSTANCE.skinArmModels.getOrDefault(skin, INSTANCE.defaultArms);
        ModelPart armToRender = isRightArm ? arms.rightArm() : arms.leftArm();
        ModelPart swordToRender = getSword(armToRender, isRightArm);
        ModelPart bladeToRender = getBlade(swordToRender, isRightArm);

        float xRotateLeftArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.leftSwordModelRotateX;
        float yRotateLeftArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.leftSwordModelRotateY;
        float zRotateLeftArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.leftSwordModelRotateZ;

        float xRotateRightArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.rightSwordModelRotateX;
        float yRotateRightArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.rightSwordModelRotateY;
        float zRotateRightArm = ClientNetworking.getAppropriateConfig().silverChariotSettings.rightSwordModelRotateZ;

        // armToRender = swordToRender;
        armToRender.setRotation(0.0F, 0.0F, 0.0F);
        armToRender.setPos(isRightArm ? 1.0F : -1.0F, -1.0F, 0.0F);
        if (isRightArm) {
            // swordToRender.setRotation(45.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F);
            swordToRender.setRotation(xRotateRightArm * Mth.DEG_TO_RAD, yRotateRightArm * Mth.DEG_TO_RAD, zRotateRightArm * Mth.DEG_TO_RAD);
            // poseStack.mulPose(Axis.XP.rotationDegrees(45));
        } else {
            // swordToRender.setRotation(-45.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F);
            swordToRender.setRotation(xRotateLeftArm * Mth.DEG_TO_RAD, yRotateLeftArm * Mth.DEG_TO_RAD, zRotateLeftArm * Mth.DEG_TO_RAD);
            // poseStack.mulPose(Axis.XP.rotationDegrees(-45));
        }
        // armToRender.setPos(isRightArm ? 1.0F : -1.0F, -1.0F, 0.0F);
        // poseStack.translate();

        ClientUtil.pushPoseAndCooperate(poseStack, 8);
        playerArm.translateAndRotate(poseStack);
        poseStack.scale(1.05F, 1.02F, 1.05F);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        armToRender.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha * 0.70F);

        ClientUtil.popPoseAndCooperate(poseStack, 8);
    }

    public static ModelPart getSword(ModelPart playerArm, boolean isRightArm) {
        if (isRightArm) {
            return playerArm.getChild("lower_right_arm").getChild("right_sword");
        } else {
            return playerArm.getChild("lower_left_arm").getChild("left_sword");
        }
        // return playerArm;
    }

    public static ModelPart getBlade(ModelPart sword, boolean isRightArm) {
        if (isRightArm) {
            return sword.getChild("right_blade");
        } else {
            return sword.getChild("left_blade");
        }
        // return sword;
    }

    private void resetModelParts(ModelPart arm, boolean isRightArm) {
        arm.setRotation(0.0F, 0.0F, 0.0F);
        arm.setPos(isRightArm ? 1.1F : -1.1F, -1.0F, 0.0F);

        ModelPart sword = getSword(arm, isRightArm);
        sword.setRotation(0.0F, 0.0F, 0.0F);

        ModelPart blade = getBlade(sword, isRightArm);
        blade.setRotation(0.0F, 0.0F, 0.0F);
    }
}
