package net.hydra.jojomod.block;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;
import net.hydra.jojomod.event.ModParticles;
import org.jetbrains.annotations.Nullable;

public final class HallucinatoryAcidBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    private static final int[][] SPREAD_DIRECTIONS = new int[][]{
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {0, 1}, {1, 1}
    };
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 4);
    public static final IntegerProperty SKIN = IntegerProperty.create("skin", 0, 16);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape[] SHAPES = new VoxelShape[]{Shapes.empty(),
            Block.box(0, 0, 0, 16, 4, 16), Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 0, 0, 16, 12, 16), Block.box(0, 0, 0, 16, 16, 16)};

    public HallucinatoryAcidBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LAYERS, 1).setValue(SKIN, 0)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, SKIN, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(WATERLOGGED,
                context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (fluidState.getType().isSame(Fluids.WATER)
                && ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid) {
            return level.setBlock(pos, fluidState.createLegacyBlock(), Block.UPDATE_ALL);
        }
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        if (direction.getAxis().isHorizontal()
                && adjacentState.getBlock() instanceof HallucinatoryAcidBlock
                && adjacentState.getValue(LAYERS) >= state.getValue(LAYERS)) {
            return true;
        }
        return super.skipRendering(state, adjacentState, direction);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.LAND;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof WhitesnakeEntity whitesnake
                && whitesnake.isMeltingModeActive()) return;
        if (level.getBlockEntity(pos) instanceof HallucinatoryAcidBlockEntity acid && acid.isOwner(entity)) return;
        int layers = state.getValue(LAYERS);
        if (entity.getY() - pos.getY() > SHAPES[layers].bounds().getYsize()) return;
        double strength = layers / 4.0D;
        double fullHeightMultiplier = entity instanceof Player ? 0.5D : 0.75D;
        double horizontalMultiplier = 1.0D - (1.0D - fullHeightMultiplier) * strength;
        double verticalMultiplier = 1.0D - 0.9D * strength;
        entity.makeStuckInBlock(state,
                new Vec3(horizontalMultiplier, verticalMultiplier, horizontalMultiplier));
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (((StandUser) player).roundabout$getStandPowers().getActivePower() == PowerIndex.MINING) {
            return super.getDestroyProgress(state, player, level, pos);
        }
        return 1.0F / 300.0F;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 2);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (pos.getY() <= level.getMinBuildHeight() || !FallingBlock.isFree(level.getBlockState(pos.below()))) {
            return;
        }
        CompoundTag transfer = new CompoundTag();
        if (level.getBlockEntity(pos) instanceof HallucinatoryAcidBlockEntity acid) {
            transfer = acid.saveTransferData();
            acid.clearDissolveProgress(level);
        }
        FallingBlockEntity falling = FallingBlockEntity.fall(level, pos, state);
        falling.blockData = transfer;
    }

    public static void mergeFallingAcid(ServerLevel level, BlockPos landingPos,
                                        BlockState fallingState, CompoundTag transfer) {
        int remaining = fallingState.getValue(LAYERS);
        BlockPos cursor = landingPos;
        while (remaining > 0 && cursor.getY() < level.getMaxBuildHeight()) {
            BlockState current = level.getBlockState(cursor);
            if (current.getBlock() instanceof HallucinatoryAcidBlock) {
                int currentLayers = current.getValue(LAYERS);
                int moved = Math.min(4 - currentLayers, remaining);
                if (moved > 0) {
                    level.setBlockAndUpdate(cursor, current.setValue(LAYERS, currentLayers + moved));
                    remaining -= moved;
                }
                cursor = cursor.above();
                continue;
            }
            if (!current.canBeReplaced() || !canReplaceWithAcid(current)) return;
            int placedLayers = Math.min(4, remaining);
            level.setBlockAndUpdate(cursor, fallingState.setValue(LAYERS, placedLayers)
                    .setValue(WATERLOGGED, current.getFluidState().is(Fluids.WATER)));
            if (level.getBlockEntity(cursor) instanceof HallucinatoryAcidBlockEntity placed) {
                placed.loadTransferData(transfer);
            }
            remaining -= placedLayers;
            cursor = cursor.above();
        }
    }

    public static void trySpread(ServerLevel level, BlockPos pos, BlockState state,
                                 HallucinatoryAcidBlockEntity source) {
        int layers = state.getValue(LAYERS);
        if (layers <= 1) return;
        int[] direction = SPREAD_DIRECTIONS[level.random.nextInt(SPREAD_DIRECTIONS.length)];
        BlockPos targetPos = pos.offset(direction[0], 0, direction[1]);
        BlockState targetState = level.getBlockState(targetPos);
        if (targetState.getBlock() instanceof HallucinatoryAcidBlock
                && targetState.getValue(SKIN).intValue() == state.getValue(SKIN).intValue()) {
            int targetLayers = targetState.getValue(LAYERS);
            if (targetLayers >= 4) return;
            level.setBlockAndUpdate(pos, state.setValue(LAYERS, layers - 1));
            level.setBlockAndUpdate(targetPos, targetState.setValue(LAYERS, targetLayers + 1));
            return;
        }
        if (!targetState.canBeReplaced() || !canReplaceWithAcid(targetState)
                || !hasSpreadSupport(level, targetPos)) return;

        level.setBlockAndUpdate(pos, state.setValue(LAYERS, layers - 1));
        level.setBlockAndUpdate(targetPos, state.setValue(LAYERS, 1).setValue(WATERLOGGED,
                targetState.getFluidState().is(Fluids.WATER)));
        if (level.getBlockEntity(targetPos) instanceof HallucinatoryAcidBlockEntity target) {
            source.copyLifetimeTo(target);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof HallucinatoryAcidBlockEntity acid) {
            if (ClientNetworking.getAppropriateConfig().whitesnakeSettings
                    .hallucinatoryAcidDripsCreateBlocks && random.nextInt(16) == 0) {
                tryCreateDripDeposit(level, pos, state, acid);
            }
            trySpread(level, pos, state, acid);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0) return;
        BlockPos supportPos = pos.below();
        BlockState support = level.getBlockState(supportPos);
        BlockPos airPos = supportPos.below();
        if (!support.isFaceSturdy(level, supportPos, Direction.UP)
                || !level.getBlockState(airPos).getCollisionShape(level, airPos).isEmpty()) return;
        if (ModParticles.HALLUCINATORY_ACID_DRIP == null) return;
        level.addParticle(ModParticles.HALLUCINATORY_ACID_DRIP,
                pos.getX() + 0.1D + random.nextDouble() * 0.8D,
                supportPos.getY() - 0.05D,
                pos.getZ() + 0.1D + random.nextDouble() * 0.8D,
                state.getValue(SKIN), 0.0D, 0.0D);
    }

    private static void tryCreateDripDeposit(ServerLevel level, BlockPos pos, BlockState sourceState,
                                              HallucinatoryAcidBlockEntity source) {
        BlockPos landing = findDripLanding(level, pos);
        if (landing == null) return;
        BlockState landingState = level.getBlockState(landing);
        int skin = sourceState.getValue(SKIN);
        if (landingState.getBlock() instanceof HallucinatoryAcidBlock) {
            if (landingState.getValue(SKIN) != skin || landingState.getValue(LAYERS) >= 4) return;
            level.setBlockAndUpdate(landing, landingState.setValue(LAYERS,
                    landingState.getValue(LAYERS) + 1));
        } else {
            if (!landingState.canBeReplaced() || !canReplaceWithAcid(landingState)) return;
            level.setBlockAndUpdate(landing, sourceState.setValue(LAYERS, 1).setValue(WATERLOGGED,
                    landingState.getFluidState().is(Fluids.WATER)));
        }
        if (level.getBlockEntity(landing) instanceof HallucinatoryAcidBlockEntity deposited) {
            source.copyLifetimeTo(deposited);
        }
    }

    private static @Nullable BlockPos findDripLanding(ServerLevel level, BlockPos acidPos) {
        BlockPos supportPos = acidPos.below();
        BlockState support = level.getBlockState(supportPos);
        if (!support.isFaceSturdy(level, supportPos, Direction.UP)) return null;
        BlockPos cursor = supportPos.below();
        for (int distance = 0; distance < 32 && cursor.getY() >= level.getMinBuildHeight(); distance++) {
            if (!level.hasChunkAt(cursor)) return null;
            BlockState current = level.getBlockState(cursor);
            if (current.getBlock() instanceof HallucinatoryAcidBlock) return cursor;
            if (!current.canBeReplaced() || !canReplaceWithAcid(current)) return null;
            BlockPos belowPos = cursor.below();
            BlockState below = level.getBlockState(belowPos);
            if (below.getBlock() instanceof HallucinatoryAcidBlock) {
                return belowPos;
            }
            if (below.isFaceSturdy(level, belowPos, Direction.UP)) return cursor;
            cursor = belowPos;
        }
        return null;
    }

    private static boolean hasSpreadSupport(Level level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState support = level.getBlockState(below);
        return support.isFaceSturdy(level, below, Direction.UP)
                || support.getBlock() instanceof HallucinatoryAcidBlock
                && support.getValue(LAYERS) == 4;
    }

    private static boolean canReplaceWithAcid(BlockState state) {
        if (state.isAir()) return true;
        return state.getFluidState().is(Fluids.WATER)
                && !ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == Direction.DOWN) level.scheduleTick(pos, this, 2);
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HallucinatoryAcidBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type,
                ModBlocks.HALLUCINATORY_ACID_BLOCK_ENTITY,
                HallucinatoryAcidBlockEntity::serverTick);
    }
}
