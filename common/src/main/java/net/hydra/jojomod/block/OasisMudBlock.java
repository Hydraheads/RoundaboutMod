package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class OasisMudBlock extends BaseEntityBlock {

    public static final BooleanProperty COPYING = BooleanProperty.create("copying");
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;


    public OasisMudBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COPYING, false).setValue(LEVEL, 0));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlocks.OASIS_MUD_BLOCK_ENTITY, OasisMudBlockEntity::tickBlockEnt);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definitionBuilder) {
        definitionBuilder.add(COPYING, LEVEL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OasisMudBlockEntity(blockPos, blockState);
    }





    @Nullable
    private static BlockState getCopiedBlockState(BlockState blockState, BlockGetter level, BlockPos blockPos) {

        if (!blockState.getValue(COPYING)) {
            return null;
        }

        if (level.getBlockEntity(blockPos) instanceof OasisMudBlockEntity be) {
            return be.getCopiedState();
        }
        return null;
    }

    // possibly include update neighbor flag when creating the mud blocks?
    public static final int UPDATE = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;



    public static boolean replaceBlock(ServerLevel level, BlockPos blockPos, int durationTicks) {
        BlockState blockState = level.getBlockState(blockPos);


        BlockState placed = ModBlocks.OASIS_MUD_BLOCK.defaultBlockState().setValue(COPYING, true).setValue(LEVEL, blockState.getLightEmission());

        if (!level.setBlock(blockPos, placed, UPDATE)) return false;

        if (level.getBlockEntity(blockPos) instanceof OasisMudBlockEntity blockEntity) {
            blockEntity.initialize(blockState, durationTicks);
            level.sendBlockUpdated(blockPos, placed, placed, Block.UPDATE_CLIENTS);
            return true;
        }
        return false;
    }



    @Override
    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter level, BlockPos blockPos) {
        BlockState copiedBlockState = getCopiedBlockState(blockState, level, blockPos);

        if (copiedBlockState != null) {
            return copiedBlockState.getDestroyProgress(player, level, blockPos);
        } else {
            return super.getDestroyProgress(blockState, player, level, blockPos);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        BlockState copiedBlockState = getCopiedBlockState(blockState, level, blockPos);

        if (copiedBlockState != null) {
            return copiedBlockState.getBlock().getCloneItemStack(level, blockPos, copiedBlockState);
        } else {
            return super.getCloneItemStack(level, blockPos, blockState);
        }
    }

/*
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }

 */


}
