package net.hydra.jojomod.mixin.block_throw;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public abstract class ThrowBoat extends Entity{
    /**A mixin dedicatied to preventing being pushed by boats you grab and throw.*/

    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    public void roundabout$canCollideWith(Entity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 instanceof LivingEntity le && ((StandUser)le).roundabout$getStandPowers().cancelCollision(this)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canBeCollidedWith", at = @At("HEAD"), cancellable = true)
    public void roundabout$canBeCollidedWith(CallbackInfoReturnable<Boolean> cir) {
        Entity $$0 = this.getVehicle();
        if ($$0 instanceof StandEntity le && le.getUser() != null && ((StandUser)le.getUser()).roundabout$getStandPowers().cancelCollision(this)){
            cir.setReturnValue(false);
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public ThrowBoat(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}
