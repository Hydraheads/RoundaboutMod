package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.client.GERNetherIllusionHelper;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderChunkRegion.class)
public abstract class GERNetherDisguise {

    @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
    private void roundabout$netherIllusionBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (!GERNetherIllusionHelper.active && !GERNetherIllusionHelper.endActive) return;

        BlockState real = cir.getReturnValue();
        if (real.isAir()) return;

        RenderChunkRegion region = (RenderChunkRegion) (Object) this;
        boolean hasCollision = !real.getCollisionShape(region, pos).isEmpty();

        if (GERNetherIllusionHelper.endActive) {
            if (!hasCollision) {
                cir.setReturnValue(Blocks.AIR.defaultBlockState());
            } else {
                cir.setReturnValue(Blocks.END_STONE.defaultBlockState());
            }
            return;
        }

        FluidState fluid = real.getFluidState();

        // disguises water as lava
        if (!fluid.isEmpty() && !hasCollision) {
            if (!fluid.is(FluidTags.LAVA)) {
                FluidState lavaFluid = fluid.isSource()
                        ? Fluids.LAVA.getSource(false)
                        : Fluids.FLOWING_LAVA.getFlowing(
                        fluid.getAmount(),
                        fluid.hasProperty(FlowingFluid.FALLING) && fluid.getValue(FlowingFluid.FALLING)
                );
                cir.setReturnValue(lavaFluid.createLegacyBlock());
            }
            return;
        }

        // disguises everything esle as either netherrack if it's a block, or nothing so that grass and stuff won't show up
        if (!hasCollision) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        } else {
            cir.setReturnValue(Blocks.NETHERRACK.defaultBlockState());
        }
    }
}