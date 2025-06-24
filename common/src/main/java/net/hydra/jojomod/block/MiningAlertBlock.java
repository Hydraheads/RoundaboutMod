package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MiningAlertBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public MiningAlertBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, false)));
    }
    public MiningAlertBlock(Properties $$0, boolean untrue) {
        super($$0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState p_153848_, BlockPlaceContext p_153849_) {
        return true;
    }


    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        BlockState bState = (BlockState)super.getStateForPlacement(blockPlaceContext).setValue(WATERLOGGED, bl);
        return bState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }
    @Override
    public FluidState getFluidState(BlockState $$0) {
        return $$0.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState($$0);
    }
    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.INVISIBLE;
    }
    @Override
    public float getShadeBrightness(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return 1.0F;
    }
    @Override
    public boolean propagatesSkylightDown(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        super.onPlace($$0, $$1, $$2, $$3, $$4);
        $$1.scheduleTick($$2, this, 200);
    }
    @Override
    public void animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3) {
        double $$4 = (double) $$2.getX();
        double $$5 = (double) $$2.getY();
        double $$6 = (double) $$2.getZ();
        $$1.addAlwaysVisibleParticle(
                ModParticles.EXCLAMATION, $$4+ 0.5F, $$5+ 0.5F, $$6+ 0.5F, 0.0, 0.04, 0.0
        );
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }
    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        $$1.removeBlock($$2, false);
    }


    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            return Shapes.empty();
    }
    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {
    }
}
