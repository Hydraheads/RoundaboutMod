package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class InvisiBlock extends BaseEntityBlock {
    protected InvisiBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.valueOf(false)));
    }
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return createTickerHelper($$2, ModBlocks.INVISIBLE_BLOCK_ENTITY, InvisiBlockEntity::tickBlockEnt);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(UNSTABLE);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new InvisiBlockEntity($$0, $$1);
    }

    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {

    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InvisiBlockEntity inv && (inv.getOriginalState().is(Blocks.POWDER_SNOW)
                    || inv.getOriginalState().is(Blocks.MAGMA_BLOCK))) {
                if (entity != null && ((IEntityAndData)entity).roundabout$getTrueInvisibility() <= -1) {
                    inv.restoreNow();
                }
            }
        }
        super.stepOn(level, pos, state, entity); // preserve vanilla behavior
    }
    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof InvisiBlockEntity inv) {
                inv.restoreNow();
            }
        }
    }


    @Override
    public void animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3) {
    }
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack $$6 = player.getItemInHand(hand);
        if (!getIsTNT(level,pos) && !$$6.is(Items.FLINT_AND_STEEL) && !$$6.is(Items.FIRE_CHARGE)) {
            if (!level.isClientSide && player != null && (((IEntityAndData) player).roundabout$getTrueInvisibility() <= -1 ||
                    !(!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BlockItem))) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity instanceof InvisiBlockEntity inv) {
                    inv.restoreNow();
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            if (getIsTNT(level,pos) && $$6.is(Items.FLINT_AND_STEEL) || $$6.is(Items.FIRE_CHARGE)) {
                explode(level, pos, player);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                Item $$7 = $$6.getItem();
                if (!player.isCreative()) {
                    if ($$6.is(Items.FLINT_AND_STEEL)) {
                        $$6.hurtAndBreak(1, player, $$1x -> $$1x.broadcastBreakEvent(hand));
                    } else {
                        $$6.shrink(1);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get($$7));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (getIsTNT($$1,$$2)) {
            if (!$$3.is($$0.getBlock())) {
                if ($$1.hasNeighborSignal($$2)) {
                    explode($$1, $$2);
                    $$1.removeBlock($$2, false);
                }
            }
        } else {
            super.onPlace($$0,$$1,$$2,$$3,$$4);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState $$0, Level $$1, BlockPos $$2, Block $$3, BlockPos $$4, boolean $$5) {
        if (getIsTNT($$1,$$2)) {
            if ($$1.hasNeighborSignal($$2)) {
                explode($$1, $$2);
                $$1.removeBlock($$2, false);
            }
        } else {
            super.neighborChanged($$0,$$1,$$2,$$3,$$4,$$5);
        }
    }
    public boolean getIsTNT(Level $$1, BlockPos $$2){
        BlockEntity ent = $$1.getBlockEntity($$2);
        if (ent instanceof InvisiBlockEntity ibe){
            return (ibe.getOriginalState() != null && ibe.getOriginalState().is(Blocks.TNT));
        }
        return false;
    }

    @Override
    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide() && !$$3.isCreative() && $$2.getValue(UNSTABLE)) {
            explode($$0, $$1);
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }

    @Override
    public void wasExploded(Level $$0, BlockPos $$1, Explosion $$2) {
        if (!$$0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt($$0, (double)$$1.getX() + 0.5, (double)$$1.getY(), (double)$$1.getZ() + 0.5, $$2.getIndirectSourceEntity());
            int $$4 = $$3.getFuse();
            $$3.setFuse((short)($$0.random.nextInt($$4 / 4) + $$4 / 8));
            $$0.addFreshEntity($$3);
        }
    }


    public static void explode(Level $$0, BlockPos $$1, @Nullable LivingEntity $$2) {
        if (!$$0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt($$0, (double)$$1.getX() + 0.5, (double)$$1.getY(), (double)$$1.getZ() + 0.5, $$2);
            BlockEntity ent = $$0.getBlockEntity($$1);
            if (ent instanceof InvisiBlockEntity ibe && $$3 != null){
                ((IEntityAndData)$$3).roundabout$setTrueInvisibility(ibe.ticksUntilRestore);
            }
            $$0.addFreshEntity($$3);
            $$0.playSound(null, $$3.getX(), $$3.getY(), $$3.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$0.gameEvent($$2, GameEvent.PRIME_FUSE, $$1);
        }
    }


    @Override
    public void onProjectileHit(Level $$0, BlockState $$1, BlockHitResult $$2, Projectile $$3) {
        if (!$$0.isClientSide) {
            BlockPos $$4 = $$2.getBlockPos();
            if (getIsTNT($$0,$$4)) {
                Entity $$5 = $$3.getOwner();
                if ($$3.isOnFire() && $$3.mayInteract($$0, $$4)) {
                    explode($$0, $$4, $$5 instanceof LivingEntity ? (LivingEntity) $$5 : null);
                    $$0.removeBlock($$4, false);
                }
            }
        }
    }
    public static void explode(Level $$0, BlockPos $$1) {
        explode($$0, $$1, null);
    }
}
