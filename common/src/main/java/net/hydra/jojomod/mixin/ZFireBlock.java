package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireBlock.class)
public class ZFireBlock implements IFireBlock {
    @Shadow
    private int getBurnOdds(BlockState $$0) {
        return 0;
    }
    @Shadow
    protected boolean canBurn(BlockState $$0) {
        return true;
    }

    @Shadow
    private void setFlammable(Block $$0, int $$1, int $$2) {
    }
    @Override
    public void roundabout$setFlammableBlock(Block $$0, int $$1, int $$2) {
        this.setFlammable($$0, $$1, $$2);
    }

    @Override
    public int roundabout$getBurnOdds(BlockState $$0){
        return getBurnOdds($$0);
    }
    @Override
    public boolean roundabout$canBurn(BlockState $$0) {
        return canBurn($$0);
    }


    @Inject(method = "checkBurnOut", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void roundabout$checkBurnOut(Level $$0, BlockPos $$1, int $$2, RandomSource $$3, int $$4, CallbackInfo ci) {
        BlockState blkSt = $$0.getBlockState($$1);
        Block blk = blkSt.getBlock();
        if (blk instanceof GasolineBlock){
            MainUtil.gasExplode(blkSt, (ServerLevel) $$0, $$1, 0, 2, 4, 10);
            ci.cancel();
        }
    }

    /**Register Flammable blocks here*/
    public void roundabout$bootstrap() {
        this.roundabout$setFlammableBlock(ModBlocks.GASOLINE_SPLATTER, 15, 100);
    }
}