package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.item.FogBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerGameMode.class)
public abstract class ZServerPlayerGameMode {


    @Shadow @Final protected ServerPlayer player;

    @Shadow protected ServerLevel level;

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract void destroyAndAck(BlockPos $$0, int $$1, String $$2);

    @Shadow private GameType gameModeForPlayer;

    @Shadow private int destroyProgressStart;

    @Shadow private int gameTicks;

    @Shadow private boolean isDestroyingBlock;

    @Shadow private BlockPos destroyPos;

    @Shadow private int lastSentState;

    @Shadow private boolean hasDelayedDestroy;

    @Shadow private BlockPos delayedDestroyPos;

    @Shadow private int delayedTickStart;
    @Inject(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$handleBlockBreakActionM(BlockPos $$0, ServerboundPlayerActionPacket.Action $$1, Direction $$2, int $$3, int $$4, CallbackInfo ci) {
        if (this.player != null) { StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (piloting != null && piloting.isAlive() && !piloting.isRemoved() && powers instanceof PowersJustice) {
                Roundabout.LOGGER.info("1");
                BlockState $$6 = this.level.getBlockState($$0);
                if (!$$6.isAir() && $$6.getBlock() instanceof FogBlock) {
                    Roundabout.LOGGER.info("2");
                    roundabout$handleBlockBreakAction($$0,$$1,$$2,$$3,$$4);
                }
                ci.cancel();
            }
        }
    }

    @Unique
    public void roundabout$handleBlockBreakAction(BlockPos $$0, ServerboundPlayerActionPacket.Action $$1, Direction $$2, int $$3, int $$4) {

        Roundabout.LOGGER.info("3");
        double ROUNDABOUT$MAX_INTERACTION_DISTANCE = Mth.square(ClientNetworking.getAppropriateConfig().justiceFogAndPilotRange+15);
        if (this.player.getEyePosition().distanceToSqr(Vec3.atCenterOf($$0)) > ROUNDABOUT$MAX_INTERACTION_DISTANCE) {
        } else if ($$0.getY() >= $$3) {
            Roundabout.LOGGER.info("4");
            this.player.connection.send(new ClientboundBlockUpdatePacket($$0, this.level.getBlockState($$0)));
        } else {
            Roundabout.LOGGER.info("5");
            if ($$1 == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                if (!this.level.mayInteract(this.player, $$0)) {
                    this.player.connection.send(new ClientboundBlockUpdatePacket($$0, this.level.getBlockState($$0)));
                    return;
                }

                if (this.isCreative()) {
                    this.destroyAndAck($$0, $$4, "creative destroy");
                    return;
                }

                if (this.player.blockActionRestricted(this.level, $$0, this.gameModeForPlayer)) {
                    this.player.connection.send(new ClientboundBlockUpdatePacket($$0, this.level.getBlockState($$0)));
                    return;
                }
                this.destroyProgressStart = this.gameTicks;
                float $$5 = 1.0F;
                BlockState $$6 = this.level.getBlockState($$0);
                if (!$$6.isAir()) {
                    $$6.attack(this.level, $$0, this.player);
                    $$5 = $$6.getDestroyProgress(this.player, this.player.level(), $$0);
                }

                if (!$$6.isAir() && $$5 >= 1.0F) {
                    this.destroyAndAck($$0, $$4, "insta mine");
                } else {
                    if (this.isDestroyingBlock) {
                        this.player.connection.send(new ClientboundBlockUpdatePacket(this.destroyPos, this.level.getBlockState(this.destroyPos)));
                    }

                    this.isDestroyingBlock = true;
                    this.destroyPos = $$0.immutable();
                    int $$7 = (int)($$5 * 10.0F);
                    this.level.destroyBlockProgress(this.player.getId(), $$0, $$7);
                    this.lastSentState = $$7;
                }
            } else if ($$1 == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
                if ($$0.equals(this.destroyPos)) {
                    int $$8 = this.gameTicks - this.destroyProgressStart;
                    BlockState $$9 = this.level.getBlockState($$0);
                    if (!$$9.isAir()) {
                        float $$10 = $$9.getDestroyProgress(this.player, this.player.level(), $$0) * (float)($$8 + 1);
                        if ($$10 >= 0.7F) {
                            this.isDestroyingBlock = false;
                            this.level.destroyBlockProgress(this.player.getId(), $$0, -1);
                            this.destroyAndAck($$0, $$4, "destroyed");
                            return;
                        }

                        if (!this.hasDelayedDestroy) {
                            this.isDestroyingBlock = false;
                            this.hasDelayedDestroy = true;
                            this.delayedDestroyPos = $$0;
                            this.delayedTickStart = this.destroyProgressStart;
                        }
                    }
                }

            } else if ($$1 == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
                this.isDestroyingBlock = false;
                if (!Objects.equals(this.destroyPos, $$0)) {
                    this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
                }

                this.level.destroyBlockProgress(this.player.getId(), $$0, -1);
            }
        }
    }
}
