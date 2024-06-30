package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IFireBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

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

    @Override
    public int roundabout$getBurnOdds(BlockState $$0){
        return getBurnOdds($$0);
    }
    @Override
    public boolean roundabout$canBurn(BlockState $$0) {
        return canBurn($$0);
    }

}