package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameAnimalTrigger.class)
public abstract class ZTameAnimalTriggerMixin {

    @Inject(method = "trigger", at = @At("HEAD"))
    private void roundabout$onAnimalTamed(ServerPlayer player, Animal entity, CallbackInfo ci) {
        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersGoldExperience ge) {
            ge.onAnimalTamed();
        }
    }
}