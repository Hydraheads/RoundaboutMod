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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public abstract class MetallicaIronDoorMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$openIronDoorWithMetallica(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.is(Blocks.IRON_DOOR)) {

            boolean playerHasMetallica = false;

            if (player instanceof StandUser standUser) {
                if (standUser.roundabout$getStandPowers() instanceof PowersMetallica) {
                    playerHasMetallica = true;
                }
            }

            if (playerHasMetallica) {
                state = state.cycle(DoorBlock.OPEN);
                level.setBlock(pos, state, 10);

                level.playSound(player, pos,
                        state.getValue(DoorBlock.OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE,
                        SoundSource.BLOCKS,
                        1.0F,
                        level.getRandom().nextFloat() * 0.1F + 0.9F
                );

                level.gameEvent(player, state.getValue(DoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }
}