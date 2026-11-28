package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinModel.class)
public class PiglinTuskShootMixin<T extends Mob> extends PlayerModel<T> {
    public PiglinTuskShootMixin(ModelPart $$0, boolean $$1) {
        super($$0, $$1);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V", at = @At("TAIL"))
    public void roundabout$SetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {

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
