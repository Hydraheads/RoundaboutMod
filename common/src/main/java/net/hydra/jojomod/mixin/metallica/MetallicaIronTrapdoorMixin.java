package net.hydra.jojomod.mixin.metallica;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrapDoorBlock.class)
public abstract class MetallicaIronTrapdoorMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$openIronTrapdoorWithMetallica(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.is(Blocks.IRON_TRAPDOOR)) {

            boolean playerHasMetallica = false;

            if (player instanceof StandUser standUser) {
                if (standUser.roundabout$getStandPowers() instanceof PowersMetallica) {
                    playerHasMetallica = true;
                }
            }

            if (playerHasMetallica) {
                state = state.cycle(TrapDoorBlock.OPEN);
                level.setBlock(pos, state, 10);

                level.playSound(player, pos,
                        state.getValue(TrapDoorBlock.OPEN) ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE,
                        SoundSource.BLOCKS,
                        1.0F,
                        level.getRandom().nextFloat() * 0.1F + 0.9F
                );

                level.gameEvent(player, state.getValue(TrapDoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }
}