package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class ZMob extends LivingEntity {
    protected ZMob(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract boolean isAggressive();

    /**Minor code, mobs in a barrage should not be attacking*/
    @Inject(method = "doHurtTarget", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TryAttack(Entity $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).isDazed() || ((StandUser) this).roundabout$isRestrained()) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$Tick(CallbackInfo ci) {
        if (this instanceof Enemy || (this instanceof NeutralMob && !(((Mob)(Object) this) instanceof TamableAnimal))) {
            if (((StandUser) this).roundabout$isRestrained()) {
                int ticks = ((StandUser) this).roundabout$getRestrainedTicks();
                if (ticks < 50) {
                    ticks++;
                    ((StandUser) this).roundabout$setRestrainedTicks(ticks);
                }
                if (ticks >= 50) {
                    if (this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()) {
                        SE.ejectPassengers();
                        if (SE.getUser() != null) {
                            //((StandUser)SE.getUser())
                            boolean candoit = true;
                            Vec3 vec3d3 = SE.getUser().getForward();
                            for (var i = 0; i < this.getBbHeight(); i++) {
                                if (this.level().getBlockState(new BlockPos(
                                        (int) vec3d3.x(), (int) (vec3d3.y + i),
                                        (int) vec3d3.z)).isSolid()) {
                                    candoit = false;
                                    break;
                                }
                            }
                            if (candoit) {
                                this.dismountTo(vec3d3.x, vec3d3.y, vec3d3.z);
                            } else {
                                this.dismountTo(SE.getUser().getX(), SE.getUser().getY(), SE.getUser().getZ());
                            }


                        }
                        ((StandUser) this).roundabout$setRestrainedTicks(-1);
                    }
                }
            } else {
                int ticks = ((StandUser) this).roundabout$getRestrainedTicks();
                if (ticks != -1) {
                    ticks = -1;
                    ((StandUser) this).roundabout$setRestrainedTicks(ticks);
                }
            }
        }
    }

}
