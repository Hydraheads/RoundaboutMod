package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Projectile.class)
public class ZProjectile implements IProjectileAccess {
    /**The main goal of this mixin is to make projectiles spawned after a timestop move partially in one*/

    private boolean roundaboutIsTimeStopCreated = false;
    private float roundaboutSpeedMultiplier = 1F;
    public float getRoundaboutSpeedMultiplier(){
        return this.roundaboutSpeedMultiplier;
    }
    public float setRoundaboutSpeedMultiplier(float roundaboutSpeedMultiplier){
        return this.roundaboutSpeedMultiplier = roundaboutSpeedMultiplier;
    }

    public boolean getRoundaboutIsTimeStopCreated(){
        return roundaboutIsTimeStopCreated;
    }
    public void setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated){
        this.roundaboutIsTimeStopCreated = roundaboutIsTimeStopCreated;
    }

    @Override
    public void roundaboutOnHit(HitResult $$0){
        onHit($$0);
    }


    @Shadow
    protected void onHit(HitResult $$0){

    }
    @Shadow
    protected boolean canHitEntity(Entity $$0){
       return false;
    }
    @Override
    public boolean roundaboutCanHitEntity(Entity $$0x) {
        return canHitEntity($$0x);
    }

    @Inject(method = "setOwner", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutSetOwner(@Nullable Entity $$0, CallbackInfo ci) {
        if ($$0 != null) {
            if (((TimeStop) $$0.level()).inTimeStopRange($$0) && !(((TimeStop) $$0.level()).CanTimeStopEntity($$0))) {
                this.setRoundaboutIsTimeStopCreated(true);
            }
        }
    }
}
