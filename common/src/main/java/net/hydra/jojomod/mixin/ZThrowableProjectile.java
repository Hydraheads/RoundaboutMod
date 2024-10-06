package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.TimeMovingProjectile;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrowableProjectile.class)
public abstract class ZThrowableProjectile extends Entity {

    public ZThrowableProjectile(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$SetPosForTS(CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((ThrowableProjectile)(Object)this)) && ((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            super.tick();
            TimeMovingProjectile.tick((ThrowableProjectile) (Object) this);
            this.checkInsideBlocks();
            ci.cancel();
        }
    }
}
