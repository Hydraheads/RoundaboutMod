package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkShriekerBlock.class)
public class ZSculkShriekerBlock {

    @Inject(method = "stepOn", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutCancelRenderTicks(Level $$0, BlockPos $$1, BlockState $$2, Entity $$3,
                                             CallbackInfo ci){
        if (!$$0.isClientSide()){
            if ($$3 != null && PowerTypes.isExistentiallyElsewhere($$3) &&
                    !PowerTypes.canInteractInExistence($$3)) {
                ci.cancel();
                return;
            }
        }
    }
}
