package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerModel.class)
public abstract class AnubisIllagerModelMixin<T extends AbstractIllager>
        extends HierarchicalModel<T>
        implements ArmedModel,
        HeadedModel {


    @Shadow
    @Final
    private ModelPart head;

    @Shadow
    @Final
    private ModelPart rightArm;

    @Shadow
    @Final
    private ModelPart leftArm;

    @Shadow
    @Final
    private ModelPart rightLeg;

    @Shadow
    @Final
    private ModelPart leftLeg;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/AbstractIllager;FFFFF)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/AbstractIllager;getArmPose()Lnet/minecraft/world/entity/monster/AbstractIllager$IllagerArmPose;",shift = At.Shift.BEFORE), cancellable = true)
    private void roundabout$cancelIllagerPose(T abstractIllager, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if ( ((StandUser)abstractIllager).roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.isUsingStand(abstractIllager) ) {
            if (((LivingEntity)abstractIllager).getMainHandItem().isEmpty()) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, h);
            } else {
                AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, abstractIllager, this.attackTime, h);
            }
            ci.cancel();
        }
    }
}
