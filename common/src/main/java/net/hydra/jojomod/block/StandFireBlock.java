package net.hydra.jojomod.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hydra.jojomod.access.IFireBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StandFireBlock extends BaseEntityBlock {
    private final float fireDamage;
    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {
            if ($$1.getBlockEntity($$2) instanceof StandFireBlockEntity $$5) {
            }

            super.onRemove($$0, $$1, $$2, $$3, $$4);
        }
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new StandFireBlockEntity($$0, $$1);
    }
    public StandFireBlock(Properties $$0) {
        super($$0);
        this.fireDamage = 1;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0)).setValue(NORTH, false)).setValue(EAST, false)).setValue(SOUTH, false)).setValue(WEST, false)).setValue(UP, false));
        this.shapesCache = ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().filter(($$0x) -> {
            return (Integer)$$0x.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), StandFireBlock::calculateShape)));
    }


    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;
    private static final int IGNITE_INSTANT = 60;
    private static final int IGNITE_EASY = 30;
    private static final int IGNITE_MEDIUM = 15;
    private static final int IGNITE_HARD = 5;
    private static final int BURN_INSTANT = 100;
    private static final int BURN_EASY = 60;
    private static final int BURN_MEDIUM = 20;
    private static final int BURN_HARD = 5;
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap();

    private static VoxelShape calculateShape(BlockState $$0x) {
        VoxelShape $$1 = Shapes.empty();
        if ((Boolean)$$0x.getValue(UP)) {
            $$1 = UP_AABB;
        }

        if ((Boolean)$$0x.getValue(NORTH)) {
            $$1 = Shapes.or($$1, NORTH_AABB);
        }

        if ((Boolean)$$0x.getValue(SOUTH)) {
            $$1 = Shapes.or($$1, SOUTH_AABB);
        }

        if ((Boolean)$$0x.getValue(EAST)) {
            $$1 = Shapes.or($$1, EAST_AABB);
        }

        if ((Boolean)$$0x.getValue(WEST)) {
            $$1 = Shapes.or($$1, WEST_AABB);
        }

        return $$1.isEmpty() ? DOWN_AABB : $$1;
    }

    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return this.canSurvive($$0, $$3, $$4) ? this.getStateWithAge($$3, $$4, (Integer)$$0.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }

    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return (VoxelShape)this.shapesCache.get($$0.setValue(AGE, 0));
    }

    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return this.getStateForPlacement($$0.getLevel(), $$0.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter $$0, BlockPos $$1) {
        BlockPos $$2 = $$1.below();
        BlockState $$3 = $$0.getBlockState($$2);
        if (!this.canBurn($$3) && !$$3.isFaceSturdy($$0, $$2, Direction.UP)) {
            BlockState $$4 = this.defaultBlockState();
            Direction[] var6 = Direction.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction $$5 = var6[var8];
                BooleanProperty $$6 = (BooleanProperty)PROPERTY_BY_DIRECTION.get($$5);
                if ($$6 != null) {
                    $$4 = (BlockState)$$4.setValue($$6, this.canBurn($$0.getBlockState($$1.relative($$5))));
                }
            }

            return $$4;
        } else {
            return this.defaultBlockState();
        }
    }

    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return $$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP) || this.isValidFireLocation($$1, $$2);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return createTickerHelper($$2, ModBlocks.STAND_FIRE_BLOCK_ENTITY, StandFireBlockEntity::tickFire);
    }


    protected boolean isNearRain(Level $$0, BlockPos $$1) {
        return $$0.isRainingAt($$1) || $$0.isRainingAt($$1.west()) || $$0.isRainingAt($$1.east()) || $$0.isRainingAt($$1.north()) || $$0.isRainingAt($$1.south());
    }

    private int getBurnOdds(BlockState $$0) {
        return ((IFireBlock)Blocks.FIRE).roundabout$getBurnOdds($$0);
    }

    private int getIgniteOdds(BlockState $$0) {
        return ((IFireBlock)Blocks.FIRE).roundabout$getIgniteOdds($$0);
    }

    public void checkBurnOut(Level $$0, BlockPos $$1, int $$2, RandomSource $$3, int $$4) {
        int $$5 = this.getBurnOdds($$0.getBlockState($$1));
        if ($$3.nextInt($$2) < $$5) {
            BlockState $$6 = $$0.getBlockState($$1);
            if ($$3.nextInt($$4 + 10) < 5 && !$$0.isRainingAt($$1)) {
                int $$7 = Math.min($$4 + $$3.nextInt(5) / 4, 15);
                $$0.setBlock($$1, this.getStateWithAge($$0, $$1, $$7), 3);
            } else {
                $$0.removeBlock($$1, false);
            }

            Block $$8 = $$6.getBlock();
            if ($$8 instanceof TntBlock) {
                TntBlock.explode($$0, $$1);
            }
        }

    }

    public BlockState getStateWithAge(LevelAccessor $$0, BlockPos $$1, int $$2) {
        BlockState $$3 = getState($$0, $$1);
        return $$3.is(ModBlocks.STAND_FIRE) ? (BlockState)$$3.setValue(AGE, $$2) : $$3;
    }

    public boolean isValidFireLocation(BlockGetter $$0, BlockPos $$1) {
        Direction[] var3 = Direction.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Direction $$2 = var3[var5];
            if (this.canBurn($$0.getBlockState($$1.relative($$2)))) {
                return true;
            }
        }

        return false;
    }

    public int getIgniteOdds(LevelReader $$0, BlockPos $$1) {
        if (!$$0.isEmptyBlock($$1)) {
            return 0;
        } else {
            int $$2 = 0;
            Direction[] var4 = Direction.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Direction $$3 = var4[var6];
                BlockState $$4 = $$0.getBlockState($$1.relative($$3));
                $$2 = Math.max(this.getIgniteOdds($$4), $$2);
            }

            return $$2;
        }
    }
    private static final int SECONDS_ON_FIRE = 8;
    protected static final float AABB_OFFSET = 1.0F;
    protected static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);



    public static BlockState getState(BlockGetter $$0, BlockPos $$1) {
        BlockPos $$2 = $$1.below();
        BlockState $$3 = $$0.getBlockState($$2);
        return SoulFireBlock.canSurviveOnBlock($$3) ? Blocks.SOUL_FIRE.defaultBlockState() :
                ((StandFireBlock)ModBlocks.STAND_FIRE).getStateForPlacement($$0, $$1);
    }

    @Override
    public void animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3) {
        if ($$3.nextInt(24) == 0) {
            $$1.playLocalSound(
                    (double)$$2.getX() + 0.5,
                    (double)$$2.getY() + 0.5,
                    (double)$$2.getZ() + 0.5,
                    SoundEvents.FIRE_AMBIENT,
                    SoundSource.BLOCKS,
                    1.0F + $$3.nextFloat(),
                    $$3.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }

        BlockPos $$4 = $$2.below();
        BlockState $$5 = $$1.getBlockState($$4);
        if (!this.canBurn($$5) && !$$5.isFaceSturdy($$1, $$4, Direction.UP)) {
            if (this.canBurn($$1.getBlockState($$2.west()))) {
                for (int $$10 = 0; $$10 < 2; $$10++) {
                    double $$11 = (double)$$2.getX() + $$3.nextDouble() * 0.1F;
                    double $$12 = (double)$$2.getY() + $$3.nextDouble();
                    double $$13 = (double)$$2.getZ() + $$3.nextDouble();
                    $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$11, $$12, $$13, 0.0, 0.0, 0.0);
                }
            }

            if (this.canBurn($$1.getBlockState($$2.east()))) {
                for (int $$14 = 0; $$14 < 2; $$14++) {
                    double $$15 = (double)($$2.getX() + 1) - $$3.nextDouble() * 0.1F;
                    double $$16 = (double)$$2.getY() + $$3.nextDouble();
                    double $$17 = (double)$$2.getZ() + $$3.nextDouble();
                    $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$15, $$16, $$17, 0.0, 0.0, 0.0);
                }
            }

            if (this.canBurn($$1.getBlockState($$2.north()))) {
                for (int $$18 = 0; $$18 < 2; $$18++) {
                    double $$19 = (double)$$2.getX() + $$3.nextDouble();
                    double $$20 = (double)$$2.getY() + $$3.nextDouble();
                    double $$21 = (double)$$2.getZ() + $$3.nextDouble() * 0.1F;
                    $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$19, $$20, $$21, 0.0, 0.0, 0.0);
                }
            }

            if (this.canBurn($$1.getBlockState($$2.south()))) {
                for (int $$22 = 0; $$22 < 2; $$22++) {
                    double $$23 = (double)$$2.getX() + $$3.nextDouble();
                    double $$24 = (double)$$2.getY() + $$3.nextDouble();
                    double $$25 = (double)($$2.getZ() + 1) - $$3.nextDouble() * 0.1F;
                    $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$23, $$24, $$25, 0.0, 0.0, 0.0);
                }
            }

            if (this.canBurn($$1.getBlockState($$2.above()))) {
                for (int $$26 = 0; $$26 < 2; $$26++) {
                    double $$27 = (double)$$2.getX() + $$3.nextDouble();
                    double $$28 = (double)($$2.getY() + 1) - $$3.nextDouble() * 0.1F;
                    double $$29 = (double)$$2.getZ() + $$3.nextDouble();
                    $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$27, $$28, $$29, 0.0, 0.0, 0.0);
                }
            }
        } else {
            for (int $$6 = 0; $$6 < 3; $$6++) {
                double $$7 = (double)$$2.getX() + $$3.nextDouble();
                double $$8 = (double)$$2.getY() + $$3.nextDouble() * 0.5 + 0.5;
                double $$9 = (double)$$2.getZ() + $$3.nextDouble();
                $$1.addParticle(ParticleTypes.LARGE_SMOKE, $$7, $$8, $$9, 0.0, 0.0, 0.0);
            }
        }
    }

    protected boolean canBurn(BlockState $$0) {
        return this.getIgniteOdds($$0) > 0;
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if (!$$3.fireImmune()) {
            $$3.setRemainingFireTicks($$3.getRemainingFireTicks() + 1);
            if ($$3.getRemainingFireTicks() == 0) {
                $$3.setSecondsOnFire(8);
            }
        }

        $$3.hurt($$1.damageSources().inFire(), this.fireDamage);
        super.entityInside($$0, $$1, $$2, $$3);
    }


    private static boolean inPortalDimension(Level $$0) {
        return $$0.dimension() == Level.OVERWORLD || $$0.dimension() == Level.NETHER;
    }

    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {
    }

    @Override
    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide()) {
            $$0.levelEvent(null, 1009, $$1, 0);
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }

    public static boolean canBePlacedAt(Level $$0, BlockPos $$1, Direction $$2) {
        BlockState $$3 = $$0.getBlockState($$1);
        return !$$3.isAir() ? false : getState($$0, $$1).canSurvive($$0, $$1) || isPortal($$0, $$1, $$2);
    }

    private static boolean isPortal(Level $$0, BlockPos $$1, Direction $$2) {
        if (!inPortalDimension($$0)) {
            return false;
        } else {
            BlockPos.MutableBlockPos $$3 = $$1.mutable();
            boolean $$4 = false;

            for (Direction $$5 : Direction.values()) {
                if ($$0.getBlockState($$3.set($$1).move($$5)).is(Blocks.OBSIDIAN)) {
                    $$4 = true;
                    break;
                }
            }

            if (!$$4) {
                return false;
            } else {
                Direction.Axis $$6 = $$2.getAxis().isHorizontal() ? $$2.getCounterClockWise().getAxis() : Direction.Plane.HORIZONTAL.getRandomAxis($$0.random);
                return PortalShape.findEmptyPortalShape($$0, $$1, $$6).isPresent();
            }
        }
    }


    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$3.is($$0.getBlock())) {
            if (inPortalDimension($$1)) {
                Optional<PortalShape> $$5 = PortalShape.findEmptyPortalShape($$1, $$2, Direction.Axis.X);
                if ($$5.isPresent()) {
                    $$5.get().createPortalBlocks();
                    return;
                }
            }

            if (!$$0.canSurvive($$1, $$2)) {
                $$1.removeBlock($$2, false);
            }
        }
    }

    private static int getFireTickDelay(RandomSource $$0) {
        return 30 + $$0.nextInt(10);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(new Property[]{AGE, NORTH, EAST, SOUTH, WEST, UP});
    }

    private void setFlammable(Block $$0, int $$1, int $$2) {
        this.igniteOdds.put($$0, $$1);
        this.burnOdds.put($$0, $$2);
    }

    static {
        AGE = BlockStateProperties.AGE_15;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        UP = PipeBlock.UP;
        PROPERTY_BY_DIRECTION = (Map)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(($$0) -> {
            return $$0.getKey() != Direction.DOWN;
        }).collect(Util.toMap());
        UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
