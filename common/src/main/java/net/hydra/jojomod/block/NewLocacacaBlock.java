package net.hydra.jojomod.block;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NewLocacacaBlock extends LocacacaBlock{
    public NewLocacacaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemLike getFruitType(){
        return ModItems.NEW_LOCACACA;
    }

    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return this.mayPlaceOn($$1.getBlockState($$3), $$1, $$3);
    }
    @Override
    protected boolean mayPlaceOn(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return $$0.getBlock() instanceof CactusBlock;
    }
}
