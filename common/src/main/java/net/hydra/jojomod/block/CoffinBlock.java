package net.hydra.jojomod.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Map;

public class CoffinBlock extends BedBlock {
    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");
    private static final Map<Player.BedSleepingProblem, Component> sleepResults = ImmutableMap.of(Player.BedSleepingProblem.TOO_FAR_AWAY, Component.translatable("text.roundabout.vampire.coffin_too_far"), Player.BedSleepingProblem.OBSTRUCTED, Component.translatable("text.roundabout.vampire.coffin_obstructed"));
    public CoffinBlock(DyeColor $$0, Properties $$1) {
        super($$0, $$1);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new CoffinBlockEntity($$0, $$1, DyeColor.BLACK);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return $$0.isClientSide ? createTickerHelper($$2, ModBlocks.COFFIN_BLOCK_ENTITY, CoffinBlockEntity::lidAnimateTick) : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> $$0, BlockEntityType<E> $$1, BlockEntityTicker<? super E> $$2
    ) {
        return $$1 == $$0 ? (BlockEntityTicker<A>) $$2 : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (!state.is(this)) {
                return InteractionResult.CONSUME;
            } else if (state.getValue(BedBlock.OCCUPIED)) {
                return InteractionResult.CONSUME;
            }
        }

        if (!level.isDay() && !level.isThundering()) {
            player.displayClientMessage(Component.translatable("text.roundabout.vampire.coffin_cant_sleep"), true);
        }

        if (!state.getValue(BedBlock.OCCUPIED)) {

        }

        if (!BedBlock.canSetSpawn(level)) {

        } else if (state.getValue(BedBlock.OCCUPIED)) {
            player.displayClientMessage(Component.translatable("text.roundabout.vampire.coffin_occupied"), true);
            return InteractionResult.CONSUME;
        } else {
            final BlockPos finalPos = pos;
            BlockState finalState = state;
            player.startSleepInBed(pos).ifLeft(sleepResult1 -> {
                if (sleepResult1 != null) {
                    player.displayClientMessage(sleepResults.getOrDefault(sleepResult1, sleepResult1.getMessage()), true);
                }
            });
            return InteractionResult.CONSUME;
        }
        return InteractionResult.CONSUME;
    }


    @Override
    public void fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4) {
        super.fallOn($$0, $$1, $$2, $$3, $$4 * 2F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter $$0, Entity $$1) {
        $$1.setDeltaMovement($$1.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }
}