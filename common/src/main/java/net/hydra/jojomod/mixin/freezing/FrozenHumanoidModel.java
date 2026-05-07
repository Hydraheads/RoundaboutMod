package net.hydra.jojomod.mixin.freezing;

import net.hydra.jojomod.util.HeatUtil;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class FrozenHumanoidModel<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V",
            shift = At.Shift.BEFORE))
    public void roundabout$setupAnimFreeze1(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5,CallbackInfo ci) {
        if ($$0 != null && HeatUtil.isBodyFrozen($$0)) {
            rdbt$saveXRotR = this.rightArm.xRot;
            rdbt$saveZRotR = this.rightArm.zRot;
            rdbt$saveXRotL = this.leftArm.xRot;
            rdbt$saveZRotL = this.leftArm.zRot;
        }
    }
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V",
                    shift = At.Shift.AFTER))
    public void roundabout$setupAnimFreeze2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5,CallbackInfo ci) {
        if ($$0 != null && HeatUtil.isBodyFrozen($$0)) {
            this.rightArm.xRot = rdbt$saveXRotR;
            this.rightArm.zRot = rdbt$saveZRotR;
            this.leftArm.xRot = rdbt$saveXRotL;
            this.leftArm.zRot = rdbt$saveZRotL;
        }
    }
    @Unique
    float rdbt$saveXRotR = 0;
    @Unique
    float rdbt$saveZRotR = 0;
    @Unique
    float rdbt$saveXRotL = 0;
    @Unique
    float rdbt$saveZRotL = 0;
}
