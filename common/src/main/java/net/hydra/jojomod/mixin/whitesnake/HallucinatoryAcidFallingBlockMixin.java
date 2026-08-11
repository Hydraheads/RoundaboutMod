package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.block.HallucinatoryAcidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class HallucinatoryAcidFallingBlockMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$mergeFallingAcid(CallbackInfo ci) {
        FallingBlockEntity falling = (FallingBlockEntity) (Object) this;
        if (!(falling.level() instanceof ServerLevel level)) return;
        BlockState fallingState = falling.getBlockState();
        if (!(fallingState.getBlock() instanceof HallucinatoryAcidBlock)) return;

        int topY = Mth.floor(falling.getY());
        int bottomY = Mth.floor(falling.getY()
                + Math.min(0.0D, falling.getDeltaMovement().y - 0.08D));
        int x = Mth.floor(falling.getX());
        int z = Mth.floor(falling.getZ());
        for (int y = topY; y >= bottomY && y >= level.getMinBuildHeight(); y--) {
            BlockPos target = new BlockPos(x, y, z);
            if (!(level.getBlockState(target).getBlock() instanceof HallucinatoryAcidBlock)) continue;
            CompoundTag transfer = falling.blockData == null ? new CompoundTag() : falling.blockData.copy();
            HallucinatoryAcidBlock.mergeFallingAcid(level, target, fallingState, transfer);
            falling.discard();
            ci.cancel();
            return;
        }
    }
}
