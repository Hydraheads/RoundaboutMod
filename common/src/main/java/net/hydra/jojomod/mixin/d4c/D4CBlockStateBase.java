package net.hydra.jojomod.mixin.d4c;

import net.hydra.jojomod.access.IBlockStateBase;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class D4CBlockStateBase implements IBlockStateBase {
    @Shadow protected abstract BlockState asState();


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
