package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.KingNothingEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class StandArrowHeldItemLayer<T extends GoldExperienceEntity, M extends StandModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public StandArrowHeldItemLayer(RenderLayerParent<T, M> p_234846_, ItemInHandRenderer p_234847_) {
        super(p_234846_);
        this.itemInHandRenderer = p_234847_;
    }

    public void render(PoseStack stack, MultiBufferSource bufferSource, int p_117206_, T entity, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
        //boolean flag = entity.getMainArm() == HumanoidArm.RIGHT;
        if (!entity.thisIsRequiemAnimation.isStarted()) {
            return;
        }

        if (entity.getUser() == null || !(((StandUser)entity.getUser()).roundabout$getStandPowers() instanceof PowersGoldExperience GE && GE.attackTimeDuring <= 20)) {
            return;
        }

        ItemStack itemstack = GE.getStandArrow();
        if (itemstack != null && !itemstack.isEmpty()) {
            stack.pushPose();
            if (this.getParentModel().young) {
                //float f = 0.5F;
                stack.translate(0.0F, 0.75F, 0.0F);
                stack.scale(0.5F, 0.5F, 0.5F);
            }

           this.renderArmWithItem(entity, itemstack,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    HumanoidArm.LEFT, stack, bufferSource, p_117206_);
            stack.popPose();
        }
    }

    protected void renderArmWithItem(T livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int p_117191_) {
        if (!itemStack.isEmpty()) {
            ClientUtil.pushPoseAndCooperate(poseStack,25);
            float shiftZ = 1.3F;
            float shiftY = 1;
            float shiftX = 3F;

            this.getParentModel().translateToHand(humanoidArm, poseStack, shiftZ, shiftY, shiftX);

            poseStack.mulPose(Axis.XP.rotationDegrees(-65.0F));

            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean flag = humanoidArm == HumanoidArm.LEFT;
            poseStack.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

            this.itemInHandRenderer.renderItem(livingEntity, itemStack, displayContext, flag, poseStack, bufferSource, p_117191_);

            ClientUtil.popPoseAndCooperate(poseStack, 25);
        }
    }
}
