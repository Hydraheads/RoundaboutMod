package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class ZHumanoidModel<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    /**Add player animations, such as TS floating*/

    @Shadow
    @Final
    public ModelPart rightArm;
    @Shadow
    @Final
    public ModelPart leftArm;
    @Shadow
    @Final
    public ModelPart rightLeg;
    @Shadow
    @Final
    public ModelPart leftLeg;
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isUsingItem()Z", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundaboutSetupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci){
        if (((StandUser) $$0).roundaboutGetTSJump()) {
            this.rightArm.xRot = 0.2F + Mth.cos(Math.abs(($$1)/5) * 0.6662F + (float) Math.PI) * 2.0F * Math.abs(($$2)/5) * 0.5F;
            this.leftArm.xRot = -0.2F + Mth.cos(Math.abs(($$1)/5) * 0.6662F) * 2.0F * Math.abs(($$2)/5) * 0.5F;
            this.rightLeg.xRot = 0.4F;
            this.rightLeg.yRot = 0.005F;
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = 0.6F;
            this.leftLeg.yRot = -0.005F;
            this.leftLeg.zRot = -0.07853982F;
        }
    }

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
