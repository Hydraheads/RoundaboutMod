package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonEggBlock.class)
public class ZDragonEggBlock {
    @Inject(method = "attack", at = @At(value = "HEAD"), cancellable = true, require = 0)
    protected void roundabout$attack(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, CallbackInfo ci) {
        if (PowerTypes.isExistentiallyElsewhere($$3) && !PowerTypes.canInteractInExistence($$3)){
            ci.cancel();
        }
    }
}
