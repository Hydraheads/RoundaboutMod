package net.hydra.jojomod.chocolatedisco.mixin;

import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.hydra.jojomod.event.powers.StandPowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.hydra.jojomod.chocolatedisco.PowersChocolateDisco;

@Mixin(StandPowers.class)
public class ChocolateDiscoStandSwitchMixin {

    @Inject(
            method = "onStandSwitch",
            at = @At("HEAD"),
            remap = false
    )
    private void chocolateDisco$clearQueueOnStandSwitch(
            CallbackInfo ci
    ) {

        StandPowers powers =
                (StandPowers) (Object) this;

        if (!(powers instanceof PowersChocolateDisco)) {
            return;
        }

        ServerPlayer player =
                powers.getSelf() instanceof ServerPlayer serverPlayer
                        ? serverPlayer
                        : null;

        if (player == null) {
            return;
        }

        ChocolateDiscoNetworking
                .onChocolateDiscoStandSwitch(player);
    }
}