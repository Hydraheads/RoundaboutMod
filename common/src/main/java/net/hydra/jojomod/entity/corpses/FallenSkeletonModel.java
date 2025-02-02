package net.hydra.jojomod.entity.corpses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FallenSkeletonModel<T extends FallenMob & RangedAttackMob> extends HumanoidModel<T> {
    public FallenSkeletonModel(ModelPart $$0) {
        super($$0);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild(
                "right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F)
        );
        $$1.addOrReplaceChild(
                "left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F)
        );
        $$1.addOrReplaceChild(
                "right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F)
        );
        $$1.addOrReplaceChild(
                "left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F)
        );
        return LayerDefinition.create($$0, 64, 32);
    }

    public void prepareMobModel(T $$0, float $$1, float $$2, float $$3) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack $$4 = $$0.getItemInHand(InteractionHand.MAIN_HAND);
        if ($$4.is(Items.BOW) && $$0.isAggressive()) {
            if ($$0.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }

        super.prepareMobModel($$0, $$1, $$2, $$3);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        if (!$$0.getActivated()){
            this.head.resetPose();
            this.body.resetPose();
            this.rightArm.resetPose();
            this.rightLeg.resetPose();
            this.leftArm.resetPose();
            this.leftLeg.resetPose();
        } else {
            super.setupAnim($$0, $$1, $$2, $$3, $$4, $$5);
            ItemStack $$6 = $$0.getMainHandItem();
            if ($$0.isAggressive() && ($$6.isEmpty() || !$$6.is(Items.BOW))) {
                float $$7 = Mth.sin(this.attackTime * (float) Math.PI);
                float $$8 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
                this.rightArm.zRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightArm.yRot = -(0.1F - $$7 * 0.6F);
                this.leftArm.yRot = 0.1F - $$7 * 0.6F;
                this.rightArm.xRot = (float) (-Math.PI / 2);
                this.leftArm.xRot = (float) (-Math.PI / 2);
                this.rightArm.xRot -= $$7 * 1.2F - $$8 * 0.4F;
                this.leftArm.xRot -= $$7 * 1.2F - $$8 * 0.4F;
                AnimationUtils.bobArms(this.rightArm, this.leftArm, $$3);
            }
        }
    }

    @Override
    public void translateToHand(HumanoidArm $$0, PoseStack $$1) {
        float $$2 = $$0 == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart $$3 = this.getArm($$0);
        $$3.x += $$2;
        $$3.translateAndRotate($$1);
        $$3.x -= $$2;
    }
}