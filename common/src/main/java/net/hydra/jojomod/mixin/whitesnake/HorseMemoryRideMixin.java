package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class HorseMemoryRideMixin {
    @Inject(method = "interact", at = @At("RETURN"), cancellable = true)
    private void roundaboutWhitesnake$mountHorseMemoryMob(Player player, InteractionHand hand,
                                                           CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob) (Object) this;
        DiscBearer bearer = (DiscBearer) mob;
        if (cir.getReturnValue() != InteractionResult.PASS
                || !bearer.roundabout$hasMemoryDisc()
                || bearer.roundabout$getMemoryPersonality() != MemoryPersonality.HORSE
                || player.isSecondaryUseActive() || !mob.getPassengers().isEmpty()) return;
        if (!mob.level().isClientSide()) player.startRiding(mob);
        cir.setReturnValue(InteractionResult.sidedSuccess(mob.level().isClientSide()));
    }
}
