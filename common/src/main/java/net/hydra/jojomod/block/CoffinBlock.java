package net.hydra.jojomod.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class CoffinBlock extends BedBlock {
    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");
    private static final Map<Player.BedSleepingProblem, Component> sleepResults = ImmutableMap.of(Player.BedSleepingProblem.TOO_FAR_AWAY, Component.translatable("text.roundabout.vampire.coffin_too_far"), Player.BedSleepingProblem.OBSTRUCTED, Component.translatable("text.roundabout.vampire.coffin_obstructed"));
    public CoffinBlock(DyeColor $$0, Properties $$1) {
        super($$0, $$1);
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
}