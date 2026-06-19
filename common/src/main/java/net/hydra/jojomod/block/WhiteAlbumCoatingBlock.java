package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WhiteAlbumCoatingBlock
        extends IceBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final int NEIGHBORS_TO_AGE = 4;
    private static final int NEIGHBORS_TO_MELT = 2;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.001, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape EMPTY = Block.box(0.0, 0, 0.0, 0, 0, 0);
    protected static final VoxelShape SHAPE_SMALL = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public WhiteAlbumCoatingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return !$$0.canSurvive($$3, $$4) ? Blocks.AIR.defaultBlockState() : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return $$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP) &&
                !$$1.getBlockState($$3).is(ModBlocks.WHITE_ALBUM_ICE_SLAB) &&
                !$$1.getBlockState($$3).is(ModBlocks.WHITE_ALBUM_ICE_BLOCK);
    }
    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.tick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext.isHoldingItem(Items.FLINT_AND_STEEL)) {
            return SHAPE_SMALL;
        } else {
            return EMPTY;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$3 instanceof EntityCollisionContext entitycollisioncontext) {
            Entity entity = entitycollisioncontext.getEntity();
            if (entity != null && entity instanceof LivingEntity LE) {
                if (((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PWA && PWA.hasSkatesActivated()){
                    return Shapes.empty();
                }
                return SHAPE_SMALL;
            }
        }
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean propagatesSkylightDown(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return true;
    }
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if ((randomSource.nextInt(3) == 0 || this.fewerNeigboursThan(serverLevel, blockPos, 4))  && this.slightlyMelt(blockState, serverLevel, blockPos)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutableBlockPos.setWithOffset((Vec3i)blockPos, direction);
                BlockState blockState2 = serverLevel.getBlockState(mutableBlockPos);
                if (!blockState2.is(this) || this.slightlyMelt(blockState2, serverLevel, mutableBlockPos)) continue;
                serverLevel.scheduleTick((BlockPos)mutableBlockPos, this, Mth.nextInt(randomSource, 30, 40));
            }
            return;
        }
        serverLevel.scheduleTick(blockPos, this, Mth.nextInt(randomSource, 30, 40));
    }

    private boolean slightlyMelt(BlockState blockState, Level level, BlockPos blockPos) {
        int i = blockState.getValue(AGE);
        if (i < 3) {
            level.setBlock(blockPos, (BlockState)blockState.setValue(AGE, i + 1), 2);
            return false;
        }
        this.melt2(blockState, level, blockPos);
        return true;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005f);
    }

    @Override
    protected void melt(BlockState $$0, Level $$1, BlockPos $$2) {
        $$1.removeBlock($$2, false);
    }
    protected void melt2(BlockState $$0, Level $$1, BlockPos $$2) {
         $$1.removeBlock($$2, false);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (block.defaultBlockState().is(this) && this.fewerNeigboursThan(level, blockPos, 2)) {
            this.melt2(blockState, level, blockPos);
        }
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    private boolean fewerNeigboursThan(BlockGetter blockGetter, BlockPos blockPos, int i) {
        int j = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutableBlockPos.setWithOffset((Vec3i)blockPos, direction);
            if (!blockGetter.getBlockState(mutableBlockPos).is(this) || ++j < i) continue;
            return false;
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return ItemStack.EMPTY;
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.FLINT_AND_STEEL)) {

                if (!level.isClientSide) {

                    level.setBlock(
                            pos,
                            Blocks.AIR.defaultBlockState(),
                            Block.UPDATE_ALL
                    );

                    stack.hurtAndBreak(
                            1,
                            player,
                            p -> p.broadcastBreakEvent(hand)
                    );
                }

                level.playSound(
                        player,
                        pos,
                        SoundEvents.FLINTANDSTEEL_USE,
                        SoundSource.BLOCKS,
                        1.0F,
                        level.random.nextFloat() * 0.4F + 0.8F
                );
            return InteractionResult.sidedSuccess(level.isClientSide);

        }

        return super.use(state, level, pos, player, hand, hit);
    }
}