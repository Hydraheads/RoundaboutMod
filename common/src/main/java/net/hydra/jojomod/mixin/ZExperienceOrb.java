package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class ZExperienceOrb extends Entity {
    public ZExperienceOrb(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {

        ((IEntityAndData)this).roundabout$addSecondToQueue();
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.03, 0.0));
            }
        }
        ((IEntityAndData)this).roundabout$tickQVec();
    }

}
