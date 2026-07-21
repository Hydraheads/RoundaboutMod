package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class KingBedBlock extends BedBlock {
    protected static final VoxelShape BASE = Block.box((double)0.0F, (double)3.0F, (double)0.0F, (double)16.0F, (double)7.0F, (double)16.0F);
    public KingBedBlock(DyeColor $$0, Properties $$1) {
        super($$0, $$1);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new KingBedBlockEntity($$0, $$1);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return !$$0.isClientSide() ? createTickerHelper($$2, ModBlocks.KING_BED_BLOCK_ENTITY, KingBedBlockEntity::tickThis) : null;
    }

    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        if ($$1.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if ($$0.getValue(PART) != BedPart.HEAD) {
                $$2 = $$2.relative((Direction)$$0.getValue(FACING));
                $$0 = $$1.getBlockState($$2);
                if (!$$0.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn($$1)) {
                $$1.removeBlock($$2, false);
                BlockPos $$6 = $$2.relative(((Direction)$$0.getValue(FACING)).getOpposite());
                if ($$1.getBlockState($$6).is(this)) {
                    $$1.removeBlock($$6, false);
                }

                Vec3 $$7 = $$2.getCenter();
                $$1.explode((Entity)null, $$1.damageSources().badRespawnPointExplosion($$7), (ExplosionDamageCalculator)null, $$7, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if ((Boolean)$$0.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed($$1, $$2)) {
                    $$3.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                $$3.startSleepInBed($$2).ifLeft(($$1x) -> {
                    if ($$1x.getMessage() != null) {
                        $$3.displayClientMessage($$1x.getMessage(), true);
                    }

                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    private boolean kickVillagerOutOfBed(Level $$0, BlockPos $$1) {
        List<Villager> $$2 = $$0.getEntitiesOfClass(Villager.class, new AABB($$1), LivingEntity::isSleeping);
        if ($$2.isEmpty()) {
            return false;
        } else {
            ((Villager)$$2.get(0)).stopSleeping();
            return true;
        }
    }
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return BASE;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> $$0, BlockEntityType<E> $$1, BlockEntityTicker<? super E> $$2
    ) {
        return $$1 == $$0 ? (BlockEntityTicker<A>) $$2 : null;
    }
}
