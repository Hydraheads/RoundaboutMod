package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceRequiemEntity;
import net.hydra.jojomod.entity.stand.KingNothingEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class StandHeldItemLayer <T extends StandEntity, M extends StandModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public StandHeldItemLayer(RenderLayerParent<T, M> p_234846_, ItemInHandRenderer p_234847_) {
        super(p_234846_);
        this.itemInHandRenderer = p_234847_;
    }

    public void render(PoseStack stack, MultiBufferSource bufferSource, int p_117206_, T entity, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
        boolean flag = entity.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack =  entity.getHeldItem();
        if (!itemstack.isEmpty()) {
            stack.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                stack.translate(0.0F, 0.75F, 0.0F);
                stack.scale(0.5F, 0.5F, 0.5F);
            }

            if (itemstack.getItem() instanceof BlockItem){
                //p_117204_.scale(3,3,3);
            }
            if (entity instanceof KingNothingEntity) {
                this.renderArmWithItem(entity, itemstack,
                        ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        HumanoidArm.RIGHT, stack, bufferSource, p_117206_);
            }
            else this.renderArmWithItem(entity, itemstack,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    HumanoidArm.LEFT, stack, bufferSource, p_117206_);
            stack.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int p_117191_) {
        if (!itemStack.isEmpty()) {
            ClientUtil.pushPoseAndCooperate(poseStack,25);
            float shiftZ = 1.3F;
            float shiftY = 1;
            float shiftX = 3F;
            if (livingEntity instanceof KingNothingEntity) {
                float angle = livingEntity.getViewXRot(0);
                // these numbers need tweaks but i think id go insane if i kept going for now
                float zMult = angle < 0 ? 0.4f : 1f;
                float yMult = angle > 0 ? 1.1f : -2.2f;
                float single = (float) Math.sin(Math.toRadians(angle));
                shiftX = -0.5f;
                shiftZ = 4.5f - (single * 12f * zMult);
                shiftY = 7f - (single * 4.5f * yMult);
            }
            else if (MainUtil.isThrownBlockItem((itemStack.getItem()))){
                if(!(livingEntity instanceof SeperatedArmEntity)) {
                    if (((StandEntity) livingEntity).getUser() != null) {
                        shiftZ = 0 - Math.max(0, Math.min(((StandUser) ((StandEntity) livingEntity).getUser()).roundabout$getAttackTimeDuring(), 10F)) * 1.4F;
                        shiftY = -0.5F;
                        shiftX = 4;
                    }
                }
            } else if (livingEntity instanceof StarPlatinumEntity SP){
                shiftZ = -1F;
            } else if (livingEntity instanceof GoldExperienceRequiemEntity GER) {
                float time = livingEntity.tickCount + Minecraft.getInstance().getFrameTime();
                // bobbing the item up and down while idle
                float bobOffset = Mth.sin(time * 0.1F) * 0.5F;

                float peakTime = 0.25F;       // seconds when item hits highest point
                float startBelow = -8.0F;     // starting offset below resting (negative = lower)
                float overshootAbove = 2.0F;  // peak offset slightly above resting (positive = higher)

                // first tick snapping somewhere fix
                float grabRise = (!GER.itemThrowAnimation.isStarted() && !GER.itemRetractAnimation.isStarted())
                        ? startBelow : 0.0F;

                if (GER.itemGrabAnimation.isStarted()) {
                    // for the initial grab, the item should quickly rise up to the finger like the anime
                    float grabSec = GER.itemGrabAnimation.getAccumulatedTime() / 1000.0F;
                    if (grabSec < 0.625F) {
                        if (grabSec < peakTime) {
                            float t = grabSec / peakTime;
                            float eased = t * t * (3.0F - 2.0F * t);
                            grabRise = Mth.lerp(eased, startBelow, overshootAbove);
                        } else {
                            float t = (grabSec - peakTime) / (0.625F - peakTime);
                            float eased = t * t * (3.0F - 2.0F * t);
                            grabRise = Mth.lerp(eased, overshootAbove, 0.0F);
                        }
                    } else {
                        grabRise = 0.0F;
                    }
                }

                shiftZ = -12.3F;
                shiftY = 15.5F + bobOffset + grabRise;
                shiftX = 3.8F;
            }
            this.getParentModel().translateToHand(humanoidArm, poseStack, shiftZ, shiftY, shiftX);
            if (livingEntity instanceof GoldExperienceRequiemEntity GER
                    && !MainUtil.isThrownBlockItem(itemStack.getItem())
                    && !GER.itemThrowAnimation.isStarted()) {
                ModelPart hand = this.getParentModel().rightHand;
                poseStack.mulPose(Axis.XP.rotation(-hand.xRot));
                poseStack.mulPose(Axis.YP.rotation(-hand.yRot));
                poseStack.mulPose(Axis.ZP.rotation(-hand.zRot));
            }
            if (livingEntity instanceof KingNothingEntity) {
                poseStack.mulPose(Axis.XP.rotationDegrees(livingEntity.getViewXRot(0) - 65));
            }
            else if (!(livingEntity instanceof SeperatedArmEntity) &&
                    MainUtil.isThrownBlockItem((itemStack.getItem()))) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(-65.0F));
            }
            if (livingEntity instanceof SeperatedArmEntity){
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,-90),0,0.7F,0.5F);
            }
            if (livingEntity instanceof KingNothingEntity) {
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,-45),0,0.7F,0.5F);
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean flag = humanoidArm == HumanoidArm.LEFT;
            poseStack.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

            if (livingEntity instanceof GoldExperienceRequiemEntity) {
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 110.0F), 0.0F, 0.25F, 0.0F);
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 2.5F), 0.0F, 0.25F, 0.0F);
            }
            if (!(livingEntity instanceof SeperatedArmEntity || livingEntity instanceof KingNothingEntity) && MainUtil.isThrownBlockItem(itemStack.getItem())){
                poseStack.scale(3,3,3);
            }
            if (itemStack.getItem() instanceof AnubisItem) {
                ItemStack stack = new ItemStack(ModItems.ANUBIS_ITEM);
                stack.getOrCreateTag().putFloat("CustomModelData", 1F);
                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,90),0,0,0);
                poseStack.scale(1.2F,1.2F,1.2F);
                this.itemInHandRenderer.renderItem(livingEntity,stack,ItemDisplayContext.GROUND,true,poseStack,bufferSource,p_117191_);
            } else {
                this.itemInHandRenderer.renderItem(livingEntity, itemStack, displayContext, flag, poseStack, bufferSource, p_117191_);
            }
            ClientUtil.popPoseAndCooperate(poseStack, 25);
        }
    }
}
