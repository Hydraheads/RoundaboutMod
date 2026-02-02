package net.hydra.jojomod.client.models.mobs.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class AnubisGuardianLayer <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;
    public AnubisGuardianLayer(RenderLayerParent<T, M> $$0, ItemInHandRenderer ihr) {
        super($$0);
        itemInHandRenderer = ihr;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T t, float v, float v1, float v2, float v3, float v4, float v5) {
        if (shouldRenderArms(t)) {
            renderArms(poseStack,multiBufferSource,i,t,v,v1,v2,v3,v4,v5);
        }
        if (shouldRenderCrossed(t)) {
            renderCrossed(poseStack, multiBufferSource, i, t, v, v1, v2, v3, v4, v5);
        }

      //  poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,-90),0,0,0);
      //  poseStack.translate(0,0.3,0.3);
      //  this.itemInHandRenderer.renderItem(t, ModItems.ANUBIS_ITEM.getDefaultInstance(),ItemDisplayContext.GROUND,false,poseStack,multiBufferSource,i);
    }

    public void renderCrossed(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        $$0.pushPose();
        $$0.translate(0.0F, 0.4F, -0.4F);
        $$0.mulPose(Axis.XP.rotationDegrees(180.0F));
        ItemStack $$10 = $$3.getItemBySlot(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem($$3, $$10, ItemDisplayContext.GROUND, false, $$0, $$1, $$2);
        $$0.popPose();
    }

    public void renderArms(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        boolean $$10 = $$3.getMainArm() == HumanoidArm.RIGHT;
        ItemStack $$11 = $$10 ? $$3.getOffhandItem() : $$3.getMainHandItem();
        ItemStack $$12 = $$10 ? $$3.getMainHandItem() : $$3.getOffhandItem();
        if (!$$11.isEmpty() || !$$12.isEmpty()) {
            $$0.pushPose();
            if (this.getParentModel().young) {
                float $$13 = 0.5F;
                $$0.translate(0.0F, 0.75F, 0.0F);
                $$0.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderArmWithItem($$3, $$12, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, $$0, $$1, $$2);
            this.renderArmWithItem($$3, $$11, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, $$0, $$1, $$2);
            $$0.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, HumanoidArm $$3, PoseStack $$4, MultiBufferSource $$5, int $$6) {
        if (!$$1.isEmpty()) {
            $$4.pushPose();
            ((ArmedModel)this.getParentModel()).translateToHand($$3, $$4);
            $$4.mulPose(Axis.XP.rotationDegrees(-90.0F));
            $$4.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean $$7 = $$3 == HumanoidArm.LEFT;
            $$4.translate((float)($$7 ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            this.itemInHandRenderer.renderItem($$0, $$1, $$2, $$7, $$4, $$5, $$6);
            $$4.popPose();
        }
    }


    public boolean shouldRenderCrossed(T t) {
        if (t instanceof AnubisGuardian AG) {
            return AG.getArmPose().equals(AbstractIllager.IllagerArmPose.CROSSED);
        }
        return false;
    }
    public boolean shouldRenderArms(T t) {
        if (t instanceof AnubisGuardian AG) {
            return AG.getArmPose().equals(AbstractIllager.IllagerArmPose.ATTACKING)
                    || AG.getArmPose().equals(AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE)
                    || AG.getArmPose().equals(AbstractIllager.IllagerArmPose.CROSSBOW_HOLD)
                    || AG.getArmPose().equals(AbstractIllager.IllagerArmPose.BOW_AND_ARROW);
        }
        return false;
    }
}
