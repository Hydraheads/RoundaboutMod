package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.AccessGuardian;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Guardian.class)
public abstract class TimeSlopGuardian extends Monster implements AccessGuardian {
    @Shadow
    abstract void setActiveAttackTarget(int $$0);

    protected TimeSlopGuardian(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    @Unique
    public void rdbt$setAttackTargetG(Entity target){
        if (target != null){
            setActiveAttackTarget(target.getId());
        } else {
            setActiveAttackTarget(0);
        }

    }
    @Inject(method = "getActiveAttackTarget", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(CallbackInfoReturnable<LivingEntity> cir) {
        if (((TimeStop) this.level()).CanTimeStopEntity(this)){
            cir.setReturnValue(null);
        }
    }
}
