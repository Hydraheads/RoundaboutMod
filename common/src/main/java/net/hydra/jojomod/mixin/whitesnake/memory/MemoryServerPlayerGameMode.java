package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class MemoryServerPlayerGameMode {
    @Unique private BlockState sba$preUseBlockState = null;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void sba$blockMemorylessBreaking(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                                     Direction direction, int height, int sequence, CallbackInfo ci) {
        ServerPlayer player = ((ServerPlayerGameModeAccessor) this).sba$getPlayer();
        if (!DiscItemData.hasPlayerControl(player)) ci.cancel();
    }

    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void sba$blockControlledItemUse(ServerPlayer player, Level level, ItemStack stack,
                                                    InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser) player).sba$getStandPowers() instanceof PowersWhitesnake powers) {
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
                && !((DiscBearer) player).sba$hasMemoryDisc())) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void sba$blockControlledBlockUse(ServerPlayer player, Level level, ItemStack stack,
                                                     InteractionHand hand, BlockHitResult hit,
                                                     CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser) player).sba$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting() && powers.isMeltingMode()) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }
        if (!DiscItemData.hasPlayerControl(player)) cir.setReturnValue(InteractionResult.FAIL);
    }

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void sba$onBlockMined(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = ((ServerPlayerGameModeAccessor) this).sba$getPlayer();
        if (!player.isCreative() && ((StandUser) player).sba$getStandPowers() instanceof PowersGoldExperience ge) {
            BlockState state = player.level().getBlockState(pos);
            if (PowersGoldExperience.isMatureCrop(state)) {
                ge.onCropHarvested();
            } else if (PowersGoldExperience.isPreciousOre(state) && player.hasCorrectToolForDrops(state)) {
                ge.onPreciousOreMined();
            }
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"))
    private void sba$beforeUseItemOn(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        this.sba$preUseBlockState = null;
        if (cir.isCancelled()) {
            return;
        }
        BlockState state = level.getBlockState(hitResult.getBlockPos());
        if (PowersGoldExperience.isRightClickHarvestable(state)) {
            this.sba$preUseBlockState = state;
        }
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void sba$afterUseItemOn(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.sba$preUseBlockState != null) {
            BlockState postState = level.getBlockState(hitResult.getBlockPos());
            if (PowersGoldExperience.wasRightClickHarvested(this.sba$preUseBlockState, postState)
                    && ((StandUser) player).sba$getStandPowers() instanceof PowersGoldExperience ge) {
                ge.onCropHarvested();
            }
            this.sba$preUseBlockState = null;
        }
    }
}
