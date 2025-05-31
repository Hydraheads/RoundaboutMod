package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class ZBlockStateBase {
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void roundabout$entityInside(Level level, BlockPos pos, Entity entity, CallbackInfo ci)
    {
        if (entity instanceof LivingEntity LE)
        {
            if (((StandUser)LE).roundabout$isParallelRunning())
            {
                ci.cancel();
            }
        }
    }
}
