package net.hydra.jojomod.mixin.freezing;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class FrozenAbstractZombieModel<T extends Monster> extends HumanoidModel<T> {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/model/AnimationUtils;animateZombieArms(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;ZFF)V",
                    shift = At.Shift.BEFORE), cancellable = true)
    public void roundabout$setupAnimFreeze3(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        if ($$0 != null && HeatUtil.isBodyFrozen($$0)) {
            ClientUtil.animateZombieArmsNoBob(this.leftArm, this.rightArm, this.isAggressive($$0), this.attackTime, $$3);
            ci.cancel();
        }
    }
    public FrozenAbstractZombieModel(ModelPart $$0) {
        super($$0);
    }

    @Shadow public abstract boolean isAggressive(T monster);
}
