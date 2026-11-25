package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.JumpinJackFlashModel;
import net.hydra.jojomod.client.models.stand.renderers.JumpinJackFlashRenderer;
import net.hydra.jojomod.entity.stand.JumpinJackFlashEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.stand.powers.PowersJumpinJackFlash;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

public final class JJFPlayerArmsOverlay {
    private static final ModelPart ROOT = JumpinJackFlashModel.createBodyLayer().bakeRoot();
    private static final ModelPart CHEST = ROOT.getChild("stand").getChild("stand2")
            .getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
    private static final ModelPart RIGHT = CHEST.getChild("right_arm");
    private static final ModelPart LEFT = CHEST.getChild("left_arm");

    private JJFPlayerArmsOverlay() {
    }

    public static boolean shouldRender(LivingEntity entity) {
        return entity instanceof StandUser user
                && user.roundabout$getStandPowers() instanceof PowersJumpinJackFlash powers
                && (user.roundabout$getStand() instanceof JumpinJackFlashEntity stand
                        ? stand.jjf$isArmsMode() : powers.jjf$isArmsMode())
                && entity.isAlive()
                && PowerTypes.hasStandActive(entity)
                && ClientUtil.canSeeStands(ClientUtil.getPlayer());
    }

    public static void render(LivingEntity entity, HumanoidModel<?> playerModel, PoseStack poseStack,
                              MultiBufferSource bufferSource, int packedLight) {
        if (!shouldRender(entity)) {
            return;
        }
        renderArm(poseStack, bufferSource, packedLight, playerModel.rightArm, true, entity);
        renderArm(poseStack, bufferSource, packedLight, playerModel.leftArm, false, entity);
    }

    public static void renderArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                 ModelPart playerArm, boolean right, LivingEntity entity) {
        ModelPart arm = right ? RIGHT : LEFT;
        arm.resetPose();
        arm.setPos(0.0F, 0.0F, 0.0F);
        arm.xRot = 0.0F;
        arm.yRot = 0.0F;
        arm.zRot = 0.0F;
        ModelPart lower = arm.getChild(right ? "lower_right_arm" : "lower_left_arm");
        lower.getChild(right ? "right_spin_off" : "left_spin_off").visible = true;
        lower.getChild(right ? "right_spin_on" : "left_spin_on").visible = false;
        poseStack.pushPose();
        playerArm.translateAndRotate(poseStack);
        poseStack.translate((right ? 1.5D : -1.5D) / 16.0D, 0.0D, 0.0D);
        byte skin = ((StandUser) entity).roundabout$getStandSkin();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(
                JumpinJackFlashRenderer.textureForSkin(skin)));
        arm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
