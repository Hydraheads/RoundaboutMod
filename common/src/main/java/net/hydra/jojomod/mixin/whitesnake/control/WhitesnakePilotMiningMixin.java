package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.access.WhitesnakePilotMiningHandler;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = ServerPlayerGameMode.class, priority = 2000)
public abstract class WhitesnakePilotMiningMixin implements WhitesnakePilotMiningHandler {
    @Inject(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$handleMining(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                                   Direction direction, int buildHeight, int sequence,
                                                   CallbackInfo ci) {
        if (!(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting() || !MainUtil.getIsGamemodeApproriateForGrief(player)) return;
        LivingEntity stand = powers.getPilotingStand();
        if (stand == null || !stand.isAlive() || stand.isRemoved()) return;
        ci.cancel();
        roundaboutWhitesnake$handleMining(pos, action, direction, buildHeight, sequence);
    }

    @Override
    public void roundaboutWhitesnake$handleMining(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                                  Direction direction, int buildHeight, int sequence) {
        PowersWhitesnake powers = (PowersWhitesnake) ((StandUser) player).roundabout$getStandPowers();
        LivingEntity stand = powers.getPilotingStand();
        if (stand.getEyePosition().distanceToSqr(Vec3.atCenterOf(pos)) > 36.0D) {
            player.connection.send(new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
            return;
        }
        if (pos.getY() >= buildHeight) {
            player.connection.send(new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
            if (!level.mayInteract(player, pos) || player.blockActionRestricted(level, pos, gameModeForPlayer)) {
                player.connection.send(new ClientboundBlockUpdatePacket(pos, state));
                return;
            }
            if (isCreative()) {
                destroyAndAck(pos, sequence, "creative destroy");
                return;
            }
            destroyProgressStart = gameTicks;
            float progress = 1.0F;
            if (!state.isAir()) {
                state.attack(level, pos, player);
                progress = state.getDestroyProgress(player, player.level(), pos);
            }
            if (!state.isAir() && progress >= 1.0F) {
                destroyAndAck(pos, sequence, "insta mine");
                return;
            }
            if (isDestroyingBlock) {
                player.connection.send(new ClientboundBlockUpdatePacket(destroyPos, level.getBlockState(destroyPos)));
            }
            isDestroyingBlock = true;
            destroyPos = pos.immutable();
            lastSentState = (int) (progress * 10.0F);
            level.destroyBlockProgress(player.getId(), pos, lastSentState);
            return;
        }

        if (action == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
            if (pos.equals(destroyPos)) {
                int elapsed = gameTicks - destroyProgressStart;
                BlockState current = level.getBlockState(pos);
                if (!current.isAir()) {
                    float progress = current.getDestroyProgress(player, player.level(), pos) * (elapsed + 1);
                    if (progress >= 0.7F) {
                        isDestroyingBlock = false;
                        level.destroyBlockProgress(player.getId(), pos, -1);
                        destroyAndAck(pos, sequence, "destroyed");
                    } else if (!hasDelayedDestroy) {
                        isDestroyingBlock = false;
                        hasDelayedDestroy = true;
                        delayedDestroyPos = pos;
                        delayedTickStart = destroyProgressStart;
                    }
                }
            }
            return;
        }

        if (action == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
            isDestroyingBlock = false;
            if (!Objects.equals(destroyPos, pos)) {
                level.destroyBlockProgress(player.getId(), destroyPos, -1);
            }
            level.destroyBlockProgress(player.getId(), pos, -1);
        }
    }

    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;
    @Shadow private GameType gameModeForPlayer;
    @Shadow private int destroyProgressStart;
    @Shadow private int gameTicks;
    @Shadow private boolean isDestroyingBlock;
    @Shadow private BlockPos destroyPos;
    @Shadow private int lastSentState;
    @Shadow private boolean hasDelayedDestroy;
    @Shadow private BlockPos delayedDestroyPos;
    @Shadow private int delayedTickStart;
    @Shadow public abstract boolean isCreative();
    @Shadow protected abstract void destroyAndAck(BlockPos pos, int sequence, String reason);
}
