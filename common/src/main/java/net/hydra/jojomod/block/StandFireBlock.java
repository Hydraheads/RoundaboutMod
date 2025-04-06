package net.hydra.jojomod.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.GasolineSplatterEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
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
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(COLOR,0));
        this.shapesCache = ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().filter(($$0x) -> {
            return (Integer)$$0x.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), StandFireBlock::calculateShape)));
    }


    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE;
    public static final IntegerProperty COLOR= ModBlocks.COLOR;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
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


    private boolean isFacingDown(BlockState s)
    {
        return !(s.getValue(StandFireBlock.UP) || s.getValue(StandFireBlock.NORTH) || s.getValue(StandFireBlock.EAST) || s.getValue(StandFireBlock.SOUTH) || s.getValue(StandFireBlock.WEST));
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape $$1 = Shapes.empty();

        if ((Boolean)state.getValue(UP)) {
            $$1 = UP_AABB;
        }

        if ((Boolean)state.getValue(NORTH)) {
            $$1 = Shapes.or($$1, NORTH_AABB);
        }

        if ((Boolean)state.getValue(SOUTH)) {
            $$1 = Shapes.or($$1, SOUTH_AABB);
        }

        if ((Boolean)state.getValue(EAST)) {
            $$1 = Shapes.or($$1, EAST_AABB);
        }

        if ((Boolean)state.getValue(WEST)) {
            $$1 = Shapes.or($$1, WEST_AABB);
        }

        return $$1.isEmpty() ? DOWN_AABB : $$1;
    }

    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if ($$0.is(ModBlocks.STAND_FIRE)) {
            int color = $$0.getValue(COLOR);
            return this.canSurvive($$0, $$3, $$4) ? this.getStateWithAge($$3, $$4, (Integer) $$0.getValue(AGE)).setValue(COLOR, color) : Blocks.AIR.defaultBlockState();
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return (VoxelShape)this.shapesCache.get($$0.setValue(AGE, 0));
    }

    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return this.getStateForPlacement($$0.getLevel(), $$0.getClickedPos());
    }

    public BlockState getStateForPlacement(BlockGetter getter, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = getter.getBlockState(belowPos);
        if (!this.canBurn(belowState) && !belowState.isFaceSturdy(getter, belowPos, Direction.UP)) {
            BlockState finalizedState = this.defaultBlockState();
            Direction[] directions = Direction.values();

            for (Direction direction : directions) {
                BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
                if (property != null) {
                    finalizedState = finalizedState.setValue(property, this.canBurn(getter.getBlockState(pos.relative(direction))));
                }
            }

            // fix for upside down fire
            finalizedState = finalizedState.setValue(UP, false);

            return finalizedState;
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

    public void checkBurnOut(Level $$0, BlockPos $$1, int $$2, RandomSource $$3, int $$4, StandFireBlockEntity sfb) {
        int $$5 = this.getBurnOdds($$0.getBlockState($$1));
        if ($$3.nextInt($$2) < $$5) {
            BlockState $$6 = $$0.getBlockState($$1);
            if ($$3.nextInt($$4 + 10) < 5 && !$$0.isRainingAt($$1)) {
                if (sfb.standUser != null && ((StandUser)sfb.standUser).roundabout$getStandPowers() instanceof PowersMagiciansRed PM) {
                    int $$7 = Math.min($$4 + $$3.nextInt(5) / 4, 15);
                    if (sfb.getBlockState().is(ModBlocks.STAND_FIRE)) {
                        int color = sfb.getBlockState().getValue(COLOR);
                        BlockState bs = this.getStateWithAge($$0, $$1, $$7);
                        if (bs.is(ModBlocks.STAND_FIRE)) {
                            bs = bs.setValue(COLOR, color);
                            $$0.setBlockAndUpdate($$1, bs);
                            BlockEntity be = $$0.getBlockEntity($$1);
                            if (be instanceof StandFireBlockEntity sfbe) {
                                sfbe.snapNumber = sfb.snapNumber;
                                sfbe.standUser = sfb.standUser;
                                sfbe.fireColorType = sfb.fireColorType;
                                sfbe.fireIDNumber = PM.getNewFireId();
                            }
                        }
                    }
                }
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
        return ((StandFireBlock)ModBlocks.STAND_FIRE).getStateForPlacement($$0, $$1);
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
    }

    protected boolean canBurn(BlockState $$0) {
        return this.getIgniteOdds($$0) > 0;
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if (!$$1.isClientSide() && $$1.getBlockEntity($$2) instanceof StandFireBlockEntity fb) {

            if (fb.standUser != null && (fb.standUser.is($$3) || ($$3.hasPassenger(fb.standUser)) ||

                    (fb.standUser != null && !(fb.standUser instanceof Monster) && !($$3 instanceof Monster) &&
                            !(fb.standUser instanceof Mob LE && LE.getTarget() !=null && LE.getTarget().is($$3))
                    ) ||

                    ($$3 instanceof TamableAnimal TA && TA.getOwner() != null && TA.getOwner().is(fb.standUser)))){
                $$1.removeBlock($$2, false);
            } else {
                if ($$3 instanceof LivingEntity LE) {
                    StandUser user = ((StandUser) $$3);
                    user.roundabout$setRemainingStandFireTicks(user.roundabout$getRemainingFireTicks() + 1);
                    if (user.roundabout$getRemainingFireTicks() == 0) {
                        if ($$1.getBlockEntity($$2) instanceof StandFireBlockEntity $$5) {
                            user.roundabout$setSecondsOnStandFire(3);
                            if (fb.standUser != null){
                                user.roundabout$setOnStandFire($$5.fireColorType,fb.standUser);
                            } else {
                                user.roundabout$setOnStandFire($$5.fireColorType);
                            }
                        }
                    }
                    float fd = 1;
                    if (user.roundabout$getStandPowers().getReducedDamage(LE)){
                        fd = (float) (fd*(ClientNetworking.getAppropriateConfig().
                                damageMultipliers.standFireOnPlayers*0.01));
                    } else {
                        fd = (float) (fd*(ClientNetworking.getAppropriateConfig().
                                damageMultipliers.standFireOnMobs*0.01));
                    }
                    Vec3 prevVelocity = LE.getDeltaMovement();
                    LE.hurt(ModDamageTypes.of($$1, ModDamageTypes.STAND_FIRE, fb.standUser), fd);
                    LE.setDeltaMovement(prevVelocity);
                } else if ($$3 instanceof GasolineSplatterEntity || $$3 instanceof GasolineCanEntity){
                    if (!$$3.isRemoved()) {
                        ((ServerLevel) $$1).sendParticles(ParticleTypes.FLAME, $$3.getX(), $$3.getY() + $$3.getEyeHeight(), $$3.getZ(),
                                40, 0.0, 0.2, 0.0, 0.2);
                        ((ServerLevel) $$1).sendParticles(ParticleTypes.EXPLOSION, $$3.getX(), $$3.getY() + $$3.getEyeHeight(), $$3.getZ(),
                                1, 0.5, 0.5, 0.5, 0.2);
                        MainUtil.gasExplode(null, (ServerLevel) $$3.level(), $$3.getOnPos(), 0, 2, 4, MainUtil.gasDamageMultiplier() * 14);
                        $$3.discard();
                    }
                }
            }
        }
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


    @SuppressWarnings("deprecation")
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
        $$0.add(new Property[]{AGE, NORTH, EAST, SOUTH, WEST, UP, COLOR});
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
