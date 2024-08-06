package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IHumanoidModelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class ZHumanoidModel<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel, IHumanoidModelAccess {

    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart hat;
    /**Add player animations, such as TS floating*/

    @Shadow
    @Final
    public ModelPart rightArm;
    @Shadow
    @Final
    public ModelPart leftArm;
    @Shadow
    @Final
    public ModelPart body;
    @Shadow
    @Final
    public ModelPart rightLeg;
    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    public HumanoidModel.ArmPose leftArmPose;
    @Shadow
    public HumanoidModel.ArmPose rightArmPose;
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isUsingItem()Z", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundaboutSetupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci){
        if ($$0 instanceof Player && ((IPlayerEntity)$$0).roundabout$GetPos() == PlayerPosIndex.TS_FLOAT) {
            this.rightArm.xRot = 0F + Mth.cos(Math.abs(($$1)/5) * 0.6662F + (float) Math.PI) * 2.0F * Math.abs(($$2)/5) * 0.5F;
            this.leftArm.xRot = -0.22F + Mth.cos(Math.abs(($$1)/5) * 0.6662F) * 2.0F * Math.abs(($$2)/5) * 0.5F;
            this.rightLeg.xRot = 0.4F;
            this.rightLeg.yRot = 0.005F;
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = 0.6F;
            this.leftLeg.yRot = -0.005F;
            this.leftLeg.zRot = -0.07853982F;
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundaboutSetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci){

       if ($$0 instanceof Player){
            byte pos = ((IPlayerEntity)$$0).roundabout$GetPos();
            if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD){
               //this.body.xRot = -20.0F;
                int dodgeTime = ((IPlayerEntity)$$0).roundabout$getDodgeTime();
                float fl;
                if (dodgeTime > -1) {
                    if (dodgeTime > 5) {
                        fl = ((11 - ((float) dodgeTime + 1 + $$2 - 1.0F)) / 20.0F * 1.6F);
                    } else {
                        fl = ((float) dodgeTime + 1 + $$2 - 1.0F) / 20.0F * 1.6F;
                    }
                    if (fl != 0) {
                        fl = Mth.sqrt(fl);
                    }
                    if (fl > 1.0F) {
                        fl = 1.0F;
                    }

                    if (pos == PlayerPosIndex.DODGE_BACKWARD){
                        fl*=-1;
                        this.leftLeg.xRot = (float) (-fl*1.2);
                    } else {
                        this.leftLeg.xRot = -fl;
                    }
                    this.rightLeg.xRot = fl;
                    this.leftArm.xRot = -fl;
                    this.rightArm.xRot = fl;
                    this.head.xRot += (float) (-fl*0.3);
                    this.setupAttackAnimation($$0, $$3);


                    boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                    if ($$0.isUsingItem()) {
                        boolean $$10 = $$0.getUsedItemHand() == InteractionHand.MAIN_HAND;
                        if ($$10 == $$9) {
                            this.poseRightArm($$0);
                        } else {
                            this.poseLeftArm($$0);
                        }
                    } else {
                        boolean $$11 = $$9 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
                        if ($$9 != $$11) {
                            this.poseLeftArm($$0);
                            this.poseRightArm($$0);
                        } else {
                            this.poseRightArm($$0);
                            this.poseLeftArm($$0);
                        }
                    }

                }
           }
       }
    }
    @Shadow
    protected void setupAttackAnimation(T $$0, float $$1) {
    }
    @Shadow
    private void poseLeftArm(T $$0) {

    }
    @Shadow
    private void poseRightArm(T $$0) {

    }
    @Unique
    @Override
    public Iterable<ModelPart> roundabout$getBodyParts(){return bodyParts();}

    @Shadow
    protected Iterable<ModelPart> headParts() {
        return null;
    }

    @Shadow
    protected Iterable<ModelPart> bodyParts() {
        return null;
    }

    @Shadow
    public void translateToHand(HumanoidArm var1, PoseStack var2) {}

    @Shadow
    public void setupAnim(T var1, float var2, float var3, float var4, float var5, float var6) {}

    @Shadow
    public ModelPart getHead() {
        return null;
    }
}
