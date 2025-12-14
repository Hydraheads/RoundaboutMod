package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class FateAbstractHorseMixin extends Animal {
    //Vampire Horses Jump Higher

    @Inject(method = "getCustomJump()D", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getCustomJump(CallbackInfoReturnable<Double> cir) {
        if (((IMob)this).roundabout$isVampire()) {
            cir.setReturnValue(this.getAttributeValue(Attributes.JUMP_STRENGTH)*1.5F);
            return;
        }
    }
    @Inject(method = "calculateFallDamage(FF)I", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$calculateFallDamageVampHorse(float $$0, float $$1, CallbackInfoReturnable<Integer> cir) {
        if (((IMob)this).roundabout$isVampire()) {
            cir.setReturnValue(Mth.ceil((($$0/2) * 0.5F - 3.0F) * $$1));
            return;
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    protected FateAbstractHorseMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }
}
