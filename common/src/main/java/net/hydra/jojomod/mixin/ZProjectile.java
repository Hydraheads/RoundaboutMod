package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Projectile.class)
public abstract class ZProjectile extends Entity implements IProjectileAccess{
    /**The main goal of this mixin is to make projectiles spawned after a timestop move partially in one*/

    @Unique
    private boolean roundabout$IsTimeStopCreated = false;
    @Unique
    private float roundaboutSpeedMultiplier = 0.75F;

    public ZProjectile(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    public float roundabout$getRoundaboutSpeedMultiplier(){
        return this.roundaboutSpeedMultiplier;
    }
    public float setRoundaboutSpeedMultiplier(float roundaboutSpeedMultiplier){
        return this.roundaboutSpeedMultiplier = roundaboutSpeedMultiplier;
    }

    public boolean roundabout$getRoundaboutIsTimeStopCreated(){
        return roundabout$IsTimeStopCreated;
    }
    public void roundabout$setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated){
        this.roundabout$IsTimeStopCreated = roundaboutIsTimeStopCreated;
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
    public boolean roundabout$CanHitEntity(Entity $$0x) {
        return canHitEntity($$0x);
    }

    @Override
    public void roundabout$CheckInsideBlocks() {
        this.checkInsideBlocks();
    }

    @Shadow
    public void shoot(double $$0, double $$1, double $$2, float $$3, float $$4) {
    }

    @Inject(method = "setOwner", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$SetOwner(@Nullable Entity $$0, CallbackInfo ci) {
        if ($$0 != null) {
            if ($$0 instanceof LivingEntity) {
                if (((TimeStop) $$0.level()).inTimeStopRange($$0) && !(((TimeStop) $$0.level()).CanTimeStopEntity($$0))) {
                    this.roundabout$setRoundaboutIsTimeStopCreated(true);
                    if (!$$0.level().isClientSide) {
                        ((StandUser) $$0).roundabout$getStandPowers().hasActedInTS = true;
                    }
                }
            }
        }
    }

    @Inject(method = "shootFromRotation", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutShootFromRotation(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        if ((((Projectile) (Object) this) instanceof ThrowableProjectile) && ((TimeStop) $$0.level()).inTimeStopRange($$0) && this.roundabout$getRoundaboutIsTimeStopCreated()) {
            float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
            float $$7 = -Mth.sin($$1 * (float) (Math.PI / 180.0));
            float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
            this.shoot((double) $$6, (double) $$7, (double) $$8, $$4, $$5);
            Vec3 $$9 = $$0.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
            ci.cancel();
        }
    }
}
