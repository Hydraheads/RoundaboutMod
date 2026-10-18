package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GamblingTableBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<GamblingTablePart> PART = ModBlocks.GAMBLING_TABLE_PART;

    // the 3 things at the end is for the actual size of the individual block
    protected static final VoxelShape TABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public GamblingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                //the "main block"
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, GamblingTablePart.FRONT_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return TABLE_SHAPE;
    }

    // position methods start

    // Calculates the world position of any part relative to the FRONT_LEFT anchor.
    public static BlockPos getPosForPart(BlockPos anchor, Direction facing, GamblingTablePart part) {
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        return switch (part) {
            case FRONT_LEFT -> anchor;
            case FRONT_RIGHT -> anchor.relative(right);
            case BACK_LEFT -> anchor.relative(back);
            case BACK_RIGHT -> anchor.relative(back).relative(right);
        };
    }

    // finds the main block position from any table part.
    public static BlockPos getMasterPos(BlockPos pos, Direction facing, GamblingTablePart part) {
        Direction left = facing.getCounterClockWise();
        Direction front = facing;
        return switch (part) {
            case FRONT_LEFT -> pos;
            case FRONT_RIGHT -> pos.relative(left);
            case BACK_LEFT -> pos.relative(front);
            case BACK_RIGHT -> pos.relative(front).relative(left);
        };
    }

    // position methods end

    // placement start

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite(); // face towards placer
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        // check if all 4 spaces are clear and within world height
        for (GamblingTablePart part : GamblingTablePart.values()) {
            BlockPos partPos = getPosForPart(pos, facing, part);
            if (!level.getBlockState(partPos).canBeReplaced(context) || level.isOutsideBuildHeight(partPos)) {
                return null;
            }
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(PART, GamblingTablePart.FRONT_LEFT);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            // place the remaining 3 blocks
            for (GamblingTablePart part : GamblingTablePart.values()) {
                if (part == GamblingTablePart.FRONT_LEFT) continue;
                BlockPos partPos = getPosForPart(pos, facing, part);
                level.setBlock(partPos, state.setValue(PART, part), 3);
            }
        }
    }

    // block interaction start

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Only FRONT_LEFT holds the block entity inventory
        return state.getValue(PART) == GamblingTablePart.FRONT_LEFT ? new GamblingTableBlockEntity(pos, state) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockPos masterPos = getMasterPos(pos, state.getValue(FACING), state.getValue(PART));
            BlockEntity blockEntity = level.getBlockEntity(masterPos);
            if (blockEntity instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        GamblingTablePart part = state.getValue(PART);
        BlockPos masterPos = getMasterPos(pos, facing, part);

        for (GamblingTablePart p : GamblingTablePart.values()) {
            if (p == part) continue;
            BlockPos checkPos = getPosForPart(masterPos, facing, p);
            if (checkPos.equals(neighborPos)) {
                if (!neighborState.is(this) || neighborState.getValue(FACING) != facing || neighborState.getValue(PART) != p) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && player.isCreative()) {
            Direction facing = state.getValue(FACING);
            GamblingTablePart part = state.getValue(PART);
            BlockPos masterPos = getMasterPos(pos, facing, part);
            BlockEntity blockEntity = level.getBlockEntity(masterPos);
            if (blockEntity instanceof GamblingTableBlockEntity tableEntity) {
                tableEntity.clearContent();
            }

            for (GamblingTablePart p : GamblingTablePart.values()) {
                BlockPos checkPos = getPosForPart(masterPos, facing, p);
                BlockState s = level.getBlockState(checkPos);
                if (s.is(this)) {
                    // flag 35 means no block drops
                    // aka don't give blocks for the block parts
                    level.setBlock(checkPos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, checkPos, Block.getId(s));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);
            GamblingTablePart part = state.getValue(PART);
            BlockPos masterPos = getMasterPos(pos, facing, part);

            if (part == GamblingTablePart.FRONT_LEFT) {
                BlockEntity blockEntity = level.getBlockEntity(masterPos);
                if (blockEntity instanceof GamblingTableBlockEntity tableEntity) {
                    tableEntity.returnAllItems();
                    Containers.dropContents(level, masterPos, tableEntity);
                    level.updateNeighbourForOutputSignal(masterPos, this);
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // block interaction end
}