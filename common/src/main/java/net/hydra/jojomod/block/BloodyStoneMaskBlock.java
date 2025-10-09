package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BloodyStoneMaskBlock extends StoneMaskBlock{
    protected BloodyStoneMaskBlock(Properties $$0) {
        super($$0);
    }
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if (!($$1.getOpposite() == $$0.getValue(FACING) && !$$0.canSurvive($$3, $$4))) {
            if ((Boolean)$$0.getValue(WATERLOGGED)) {
                convertToRegularMask($$3,$$4,$$0);
            }
        }
        return super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    public boolean placeLiquid(LevelAccessor $$0, BlockPos $$1, BlockState $$2, FluidState $$3) {
        if (!$$2.getValue(BlockStateProperties.WATERLOGGED) && $$3.getType() == Fluids.WATER) {
            if (!$$0.isClientSide()) {
                $$0.setBlock($$1, $$2.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                $$0.scheduleTick($$1, $$3.getType(), $$3.getType().getTickDelay($$0));
            }

            convertToRegularMask($$0,$$1,$$2);
            return true;
        } else {
            return false;
        }
    }

}
