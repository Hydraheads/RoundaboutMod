package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class ZombieTuskShootMixin<T extends Monster> extends HumanoidModel<T> {

    public ZombieTuskShootMixin(ModelPart $$0) {
        super($$0);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V",at = @At("TAIL"))
    private void roundabout$animateTuskShoot(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        StandUser SU = (StandUser) $$0;

        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
            AnimationDefinition anim = PowersTusk.getAnimation(SU);
            if (anim != null) {

                if (PT.isShooting(HumanoidArm.LEFT)) {
                    this.leftArm.resetPose();
                    this.leftArm.yRot += 0.1F + this.head.yRot;
                    this.leftArm.xRot += (float) (-Math.PI / 2) + this.head.xRot;
                }
                if (PT.isShooting(HumanoidArm.RIGHT)) {
                    this.rightArm.resetPose();
                    this.rightArm.yRot += -0.1F + this.head.yRot;
                    this.rightArm.xRot += (float) (-Math.PI / 2) + this.head.xRot;
                }


            }
        }

    }
}
