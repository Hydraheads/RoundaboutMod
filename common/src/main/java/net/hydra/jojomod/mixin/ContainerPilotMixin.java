package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestMenu.class)
public abstract class ContainerPilotMixin {

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void roundabout$allowPilotingChestAccess(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof StandUser user) {
            StandPowers powers = user.roundabout$getStandPowers();
            if (powers != null && powers.isPiloting()) {
                cir.setReturnValue(true);
            }
        }
    }
}