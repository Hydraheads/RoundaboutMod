package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleColumnBlock.class)
public class ZBubbleColumn {

    /**Make bubble column not animate of pull TS*/
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    public void roundabout$animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3, CallbackInfo ci) {
        if (((TimeStop)$$1).inTimeStopRange($$2)){
            ci.cancel();
        }
    }
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    public void roundabout$entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3, CallbackInfo ci) {
        if (((TimeStop)$$1).inTimeStopRange($$2)){
            ci.cancel();
        }
    }

}
