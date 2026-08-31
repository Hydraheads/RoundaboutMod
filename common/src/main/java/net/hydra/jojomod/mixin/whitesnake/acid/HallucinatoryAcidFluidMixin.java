package net.hydra.jojomod.mixin.whitesnake.acid;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class HallucinatoryAcidFluidMixin {
    @Inject(method = "canHoldFluid", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$treatAcidAsReplaceable(BlockGetter level, BlockPos pos,
                                                              BlockState state, Fluid fluid,
                                                              CallbackInfoReturnable<Boolean> cir) {
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid
                && fluid.isSame(Fluids.WATER)
                && isAcid(state)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canSpreadTo", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$washThroughAcid(BlockGetter level, BlockPos sourcePos,
                                                       BlockState sourceState, Direction direction,
                                                       BlockPos targetPos, BlockState targetState,
                                                       FluidState targetFluid, Fluid fluid,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid
                && fluid.isSame(Fluids.WATER)
                && isAcid(targetState)) {
            cir.setReturnValue(true);
        }
    }

    private static boolean isAcid(BlockState state) {
        return state.is(ModBlocks.HALLUCINATORY_ACID)
                || state.is(ModBlocks.HALLUCINATORY_ACID_WALL);
    }
}
