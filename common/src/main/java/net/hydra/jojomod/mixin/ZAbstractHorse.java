package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class ZAbstractHorse extends Animal {
    protected ZAbstractHorse(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    /**Reduced gravity changes fall damage calcs*/
    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$calculateFallDamage(float $$0, float $$1, CallbackInfoReturnable<Integer> cir) {
        StandUser user = ((StandUser) this);
        if (user.roundabout$getLeapTicks() > -1 || user.roundabout$isBubbleEncased()) {
            cir.setReturnValue(0);
            return;
        }
    }
}
