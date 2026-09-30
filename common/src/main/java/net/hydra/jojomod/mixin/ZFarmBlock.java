package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class ZFarmBlock {

    // Time erase and d4c farmland trample fix
    @Inject(method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    public void rdbt$fallOnD(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4, CallbackInfo ci) {
        if (!$$0.isClientSide){
            if (PowerTypes.isExistentiallyElsewhere($$3) && !PowerTypes.canInteractInExistence($$3)){
                ci.cancel();
            }
        }

    }
}
