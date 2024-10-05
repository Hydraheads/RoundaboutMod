package net.hydra.jojomod.mixin;

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
public class ZPassengers {
    /**Minor code for stand passengers*/

    /** Honestly, I don't know if this code actually does anything significant, but allegedly it updates
     * the position when a stand is teleported. Mount code in vanilla does this, so it is a safety
     * measurement. Injects into the Entity Class.*/
    @Inject(method = "teleportPassengers", at = @At("TAIL"))
    protected void roundabout$teleportPassengers(CallbackInfo ci) {
        if (((Entity)(Object)this) instanceof LivingEntity le) {


            ((StandUser) le).roundabout$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) le).roundabout$getFollowers()) {
                    $$1.moveTo(this.getX(),this.getY(),this.getZ());
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.roundabout$hasStandOut()) {
                standUserData.roundabout$updateStandOutPosition(standUserData.roundabout$getStand(), Entity::moveTo);
            }

        }
    }

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
