package net.hydra.jojomod.block;

import net.hydra.jojomod.item.BloodyStoneMaskBlockItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Predicate;



public class StoneMaskBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape EAST_AABB = Block.box(0.0, 4, 3.5, 5.5, 12, 12.5);
    protected static final VoxelShape WEST_AABB = Block.box(10.5, 4, 3.5, 16.0, 12, 12.5);
    protected static final VoxelShape SOUTH_AABB = Block.box(3.5, 4, 0.0, 12.5, 12, 5.5);
    protected static final VoxelShape NORTH_AABB = Block.box(3.5, 4, 10.5, 12.5, 12, 16.0);

    @Nullable
    private BlockPattern snowGolemBase;
    @Nullable
    private BlockPattern snowGolemFull;
    @Nullable
    private BlockPattern ironGolemBase;
    @Nullable
    private BlockPattern ironGolemFull;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE = $$0 -> $$0 != null && ($$0.is(Blocks.CARVED_PUMPKIN) || $$0.is(Blocks.JACK_O_LANTERN));

    protected StoneMaskBlock(BlockBehaviour.Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if ($$1.getOpposite() == $$0.getValue(FACING) && !$$0.canSurvive($$3, $$4)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if ((Boolean)$$0.getValue(WATERLOGGED)) {
                $$3.scheduleTick($$4, Fluids.WATER, Fluids.WATER.getTickDelay($$3));
            }

            return super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return (BlockState)$$0.setValue(FACING, $$1.rotate((Direction)$$0.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    private boolean canAttachTo(BlockGetter $$0, BlockPos $$1, Direction $$2) {
        BlockState $$3 = $$0.getBlockState($$1);
        return $$3.isSolid();
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        Direction $$3 = (Direction)$$0.getValue(FACING);
        return this.canAttachTo($$1, $$2.relative($$3.getOpposite()), $$3);
    }
    @Override
    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.rotate($$1.getRotation((Direction)$$0.getValue(FACING)));
    }
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        BlockState $$2;
        if (!$$0.replacingClickedOnBlock()) {
            $$2 = $$0.getLevel().getBlockState($$0.getClickedPos().relative($$0.getClickedFace().getOpposite()));
            if ($$2.is(this) && $$2.getValue(FACING) == $$0.getClickedFace()) {
                return null;
            }
        }

        $$2 = this.defaultBlockState();
        LevelReader $$3 = $$0.getLevel();
        BlockPos $$4 = $$0.getClickedPos();
        FluidState $$5 = $$0.getLevel().getFluidState($$0.getClickedPos());
        Direction[] var6 = $$0.getNearestLookingDirections();
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Direction $$6 = var6[var8];
            if ($$6.getAxis().isHorizontal()) {
                $$2 = (BlockState) $$2.setValue(FACING, $$6.getOpposite());
                if ($$2.canSurvive($$3, $$4)) {
                    return (BlockState) $$2.setValue(WATERLOGGED, $$5.getType() == Fluids.WATER);
                }
            }
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true; // Ensures custom occlusion shape is used (if applicable)
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        switch ((Direction)$$0.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
            default:
                return EAST_AABB;
        }
    }

    public void convertToRegularMask(Level level, BlockPos pos, BlockState state){
        if (state.getBlock() instanceof BloodyStoneMaskBlock){
            level.setBlockAndUpdate(pos,ModBlocks.BLOODY_STONE_MASK_BLOCK.defaultBlockState().
                    setValue(FACING,state.getValue(FACING)).
                    setValue(WATERLOGGED,state.getValue(WATERLOGGED)));
        }
    }
    public void convertToRegularMask(LevelAccessor level, BlockPos pos, BlockState state){
        if (state.getBlock() instanceof BloodyStoneMaskBlock){
            level.setBlock(pos,ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.defaultBlockState().
                    setValue(FACING,state.getValue(FACING)).
                    setValue(WATERLOGGED,true),4);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }
    @Override
    public FluidState getFluidState(BlockState $$0) {
        return $$0.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState($$0);
    }
}