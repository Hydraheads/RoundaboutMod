package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class MemoryServerPlayerGameMode {
    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void roundabout$blockMemorylessBreaking(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                                     Direction direction, int height, int sequence, CallbackInfo ci) {
        ServerPlayer player = ((ServerPlayerGameModeAccessor) this).roundabout$getPlayer();
        if (!DiscItemData.hasPlayerControl(player)) ci.cancel();
    }

    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void roundabout$blockControlledItemUse(ServerPlayer player, Level level, ItemStack stack,
                                                    InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers) {
            if (powers.isPiloting() && powers.isMeltingMode()) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }
            if (stack.isEdible() && powers.isAutoMode()) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }
        }
        if (!DiscItemData.hasPlayerControl(player)
                && !(stack.getItem() instanceof MemoryDiscItem
                && !((DiscBearer) player).roundabout$hasMemoryDisc())) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void roundabout$blockControlledBlockUse(ServerPlayer player, Level level, ItemStack stack,
                                                     InteractionHand hand, BlockHitResult hit,
                                                     CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting() && powers.isMeltingMode()) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }
        if (!DiscItemData.hasPlayerControl(player)) cir.setReturnValue(InteractionResult.FAIL);
    }
}
