package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(BreedGoal.class)
public abstract class ZBreedGoal extends Goal {

    @Shadow
    @Final
    protected Animal animal;

    @Inject(
            method = "getFreePartner",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$preventParallelBreeding(CallbackInfoReturnable<Animal> cir) {
        Animal partner = cir.getReturnValue();

        if (partner == null) {
            return;
        }

        UUID one = ((IEntityAndData)this.animal).rdbt$getNativeCopy();
        UUID two = ((IEntityAndData)partner).rdbt$getNativeCopy();
        if (one != null && one.equals(partner.getUUID())) {
            cir.setReturnValue(null);
            return;
        }
        if (two != null && two.equals(this.animal.getUUID())) {
            cir.setReturnValue(null);
            return;
        }
    }
}
