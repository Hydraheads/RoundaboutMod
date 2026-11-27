package net.hydra.jojomod.mixin.stand_entities;

import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class TeleportStandPassengers {
    /**Minor code for stand passengers*/

    /** Honestly, I don't know if this code actually does anything significant, but allegedly it updates
     * the position when a stand is teleported. Mount code in vanilla does this, so it is a safety
     * measurement. Injects into the Entity Class.*/
    @Inject(method = "teleportPassengers", at = @At("TAIL"))
    protected void sba$teleportPassengers(CallbackInfo ci) {
        if (((Entity)(Object)this) instanceof LivingEntity le) {
            ((StandUser) le).sba$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) le).sba$getFollowers()) {
                    if ($$1.level().dimension() == le.level().dimension()) {
                        $$1.moveTo(this.getX(), this.getY(), this.getZ());
                    }
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.sba$hasStandOut() && standUserData.sba$getStand() instanceof FollowingStandEntity fe) {
                if (fe.level().dimension() == le.level().dimension()) {
                    standUserData.sba$updateStandOutPosition(fe, Entity::moveTo);
                }
            }

        }
    }

    @Inject(method = "teleportTo(DDD)V",at = @At(value = "TAIL"))
    private void sba$teleportPossessor(double $$0, double $$1, double $$2, CallbackInfo ci) {
        if ( ((Entity)(Object)this) instanceof LivingEntity LE) {
            StandUser SU = (StandUser) LE;
            if (SU.sba$isPossessed() && SU.sba$getPossessor() != null) {
                SU.sba$getPossessor().teleportTo($$0,$$1,$$2);
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    public double getX() {
        return 0;
    }

    @Shadow
    @Final
    public double getY() {
        return 0;
    }
    @Shadow
    @Final
    public double getZ() {
        return 0;
    }

}
