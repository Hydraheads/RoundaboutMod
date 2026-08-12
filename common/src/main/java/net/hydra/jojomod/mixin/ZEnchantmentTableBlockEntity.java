package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentTableBlockEntity.class)
public class ZEnchantmentTableBlockEntity {
    @ModifyVariable(
            method = "bookAnimationTick",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private static Player roundabout$bookAnimationTick(Player player) {
        if (player != null && PowerTypes.isExistentiallyElsewhere(player)) {
            return null;
        }
        return player;
    }
}
