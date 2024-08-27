package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class StereoBlock extends JukeboxBlock {
    /**The specs of the Stereo are almost identical to MC jukebox code, out of need to function the same way*/
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPEA = Block.box(5.0, 0.0, 1.0, 11.0, 6.0, 15.0);
    protected static final VoxelShape SHAPEB = Block.box(1.0, 0.0, 5.0, 15.0, 6.0, 11.0);
    protected StereoBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new StereoBlockEntity($$0, $$1);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
            return this.defaultBlockState()
                    .setValue(FACING, $$0.getHorizontalDirection());
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$0.getValue(FACING).getAxis() == Direction.Axis.X){
            return SHAPEA;
        } else {
            return SHAPEB;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING);
        super.createBlockStateDefinition($$0);
    }
    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return $$0.setValue(FACING, $$1.rotate($$0.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.rotate($$1.getRotation($$0.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return $$1.getValue(HAS_RECORD) ? createTickerHelper($$2, ModBlocks.STEREO_BLOCK_ENTITY, StereoBlockEntity::playRecordTick) : null;
    }
    @Override
    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        if ($$0.getValue(HAS_RECORD) && $$1.getBlockEntity($$2) instanceof StereoBlockEntity $$6) {

            $$6.popOutRecord();
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {
            if ($$1.getBlockEntity($$2) instanceof StereoBlockEntity $$5) {
                $$5.popOutRecord();
            }

            super.onRemove($$0, $$1, $$2, $$3, $$4);
        }
    }

    @Override
    public int getSignal(BlockState $$0, BlockGetter $$1, BlockPos $$2, Direction $$3) {
        if ($$1.getBlockEntity($$2) instanceof StereoBlockEntity $$4 && $$4.isRecordPlaying()) {
            return 20;
        }

        return 0;
    }


    @Override
    public int getAnalogOutputSignal(BlockState $$0, Level $$1, BlockPos $$2) {
        if ($$1.getBlockEntity($$2) instanceof StereoBlockEntity $$3 && $$3.getFirstItem().getItem() instanceof RecordItem $$4) {
            return $$4.getAnalogOutput();
        }

        return 0;
    }
}
