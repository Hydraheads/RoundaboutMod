package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ravager.class)
public abstract class HorseMemoryRavagerMixin {
    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$usePlayerAsController(CallbackInfoReturnable<LivingEntity> cir) {
        Ravager ravager = (Ravager) (Object) this;
        if (!roundaboutWhitesnake$hasHorseMemory(ravager)) return;
        if (ravager.getFirstPassenger() instanceof Player player) cir.setReturnValue(player);
    }

    private static boolean roundaboutWhitesnake$hasHorseMemory(Ravager ravager) {
        DiscBearer bearer = (DiscBearer) ravager;
        return bearer.roundabout$hasMemoryDisc()
                && bearer.roundabout$getMemoryPersonality() == MemoryPersonality.HORSE;
    }
}
