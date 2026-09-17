package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerOpenersCounter.class)
public abstract class ContainerOpenersPilotMixin {

    @Inject(method = "getOpenCount", at = @At("RETURN"), cancellable = true)
    private void roundabout$addStandOpeners(Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        int count = cir.getReturnValue();
        for (Player player : level.players()) {
            if (player instanceof StandUser user) {
                StandPowers powers = user.roundabout$getStandPowers();
                if (powers != null && powers.isPiloting()) {
                    StandEntity stand = powers.getStandEntity(player);
                    // Check if stand is within 8 blocks of the chest
                    if (stand != null && stand.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0D) {
                        // If they actually have a chest menu open, add them to the chest's opener count!
                        if (player.containerMenu != player.inventoryMenu) {
                            count++;
                        }
                    }
                }
            }
        }
        cir.setReturnValue(count);
    }
}