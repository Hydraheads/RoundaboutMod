package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class FirearmLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public FirearmLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    private void renderFirearm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float partialTicks, ItemStack stack, boolean rightSide, boolean mainArmRight) {
        boolean isSnub = stack.getItem() instanceof SnubnoseRevolverItem;
        boolean isTommy = stack.getItem() instanceof TommyGunItem;
        boolean isColt = stack.getItem() instanceof ColtRevolverItem;
        boolean isJackal = stack.getItem() instanceof JackalRifleItem;

        if (!isSnub && !isTommy && !isColt && !isJackal) return;

        boolean actualRightArm = rightSide == mainArmRight;

        if (actualRightArm) {
            getParentModel().rightArm.translateAndRotate(poseStack);
        } else {
            getParentModel().leftArm.translateAndRotate(poseStack);
        }

        if (isSnub) {
            poseStack.translate(
                    actualRightArm ? -0.032F : 0.032F,
                    0.55F,
                    -0.66F
            );
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            ModStrayModels.SNUBNOSE_REVOLVER_MODEL.render(
                    entity, partialTicks, poseStack, bufferSource, packedLight,
                    1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, 1.0F
            );
        }

        if (isTommy) {
            poseStack.translate(
                    actualRightArm ? 0.09F : -0.09F,
                    0.77F,
                    -1.2F
            );
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.9F, 0.9F, 0.9F);
            ModStrayModels.TOMMY_GUN_MODEL.render(
                    entity, partialTicks, poseStack, bufferSource, packedLight,
                    1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, 1.0F
            );
        }

        if (isColt) {
            poseStack.translate(
                    actualRightArm ? -0.032F : 0.032F,
                    0.52F,
                    -1.50F
            );
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            ModStrayModels.COLT_REVOLVER_MODEL.render(
                    entity, partialTicks, poseStack, bufferSource, packedLight,
                    1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, 1.0F
            );
        }

        if (isJackal) {
            poseStack.translate(
                    actualRightArm ? 0.09F : -0.09F,
                    0.77F,
                    -1.2F
            );
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.9F, 0.9F, 0.9F);
            ModStrayModels.JACKAL_RIFLE_MODEL.render(
                    entity, partialTicks, poseStack, bufferSource, packedLight,
                    1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, entity.hurtTime > 0 ? 0.0F : 1.0F, 1.0F
            );
        }
    }


    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        if (entity instanceof Player player) {
            if (entity.isInvisible() || entity.isInvisibleTo(player)) return;
        }

        LivingEntity livent = entity;

        ItemStack held = entity.getMainHandItem();
        ItemStack offHeld = entity.getOffhandItem();

        boolean isHoldingFirearmRight = (held.getItem() instanceof FirearmItem);
        boolean isHoldingFirearmLeft = (offHeld.getItem() instanceof FirearmItem);

        boolean mainArmRight = entity instanceof Player player && player.getMainArm() == HumanoidArm.RIGHT;

        poseStack.pushPose();

        if (isHoldingFirearmRight) {
            poseStack.pushPose();
            renderFirearm(poseStack, bufferSource, packedLight, entity, partialTicks, held, true, mainArmRight);
            poseStack.popPose();
        }

        boolean skipOffhand = false;
        if (entity instanceof Player player) {
            if (player.isUsingItem() && player.getUseItem() == held) {
                skipOffhand = true;
            }
        }

        if (isHoldingFirearmLeft && !skipOffhand) {
            poseStack.pushPose();
            renderFirearm(poseStack, bufferSource, packedLight, entity, partialTicks, offHeld, false, mainArmRight);
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}

