package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.entity.goals.AnubisPanicGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Cow.class)
public abstract class AnubisCowMixin extends Animal {

    protected AnubisCowMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At(value = "HEAD"))
    private void roundabout$anubisCowPanic(CallbackInfo ci) {
        this.goalSelector.addGoal(0,new AnubisPanicGoal( ((Cow)(Object)this),2 ));
    }
}
