package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class ZBlockBehaviour {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir)
    {
        if (((StandUser)player).roundabout$isParallelRunning())
        {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }

    /***
    Inject(method = "getOcclusionShape", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CallbackInfoReturnable<VoxelShape> cir) {
        if (ClientUtil.hiddenBlocks.contains($$2)) {

            Roundabout.LOGGER.info("ye2");
            cir.setReturnValue(Shapes.empty());
        }
    }
     **/
}
