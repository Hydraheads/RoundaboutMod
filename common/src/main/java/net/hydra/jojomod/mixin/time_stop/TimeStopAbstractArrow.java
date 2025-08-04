package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.TimeMovingProjectile;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractArrow.class)
public abstract class TimeStopAbstractArrow extends Entity{
    /**Makes the arrows, knives, and tridents thrown in timestop special*/

    @Inject(method = "playerTouch", at = @At(value = "HEAD"),cancellable = true)
    private void roundaboutS$hakeTime(Player $$0, CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this))) {
            if (!this.level().isClientSide && (this.inGround || this.isNoPhysics())) {
                if (this.tryPickup($$0)) {
                    $$0.take(this, 1);
                    this.discard();
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$SetPosForTS(CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this)) && ((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            super.tick();
            TimeMovingProjectile.tick((AbstractArrow) (Object) this);
            this.checkInsideBlocks();
            ci.cancel();
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public TimeStopAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    protected boolean tryPickup(Player $$0){return false;}

    @Shadow
    protected boolean inGround;
    @Shadow
    public boolean isNoPhysics(){
        return false;
    }
}
