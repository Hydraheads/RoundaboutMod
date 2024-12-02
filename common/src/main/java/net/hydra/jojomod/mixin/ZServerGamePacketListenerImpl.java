package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.item.FogBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ZServerGamePacketListenerImpl {
    @Shadow public ServerPlayer player;

    @Shadow @Nullable private Vec3 awaitingPositionFromClient;

    @Shadow
    private static boolean wasBlockPlacementAttempt(ServerPlayer $$0, ItemStack $$1) {
        return false;
    }


    @Inject(method = "handleUseItemOn(Lnet/minecraft/network/protocol/game/ServerboundUseItemOnPacket;)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$handleUseItemOn(ServerboundUseItemOnPacket $$0, CallbackInfo ci) {
        if (this.player != null) {
            StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (piloting != null && piloting.isAlive() && !piloting.isRemoved() && powers instanceof PowersJustice) {
                InteractionHand $$2 = $$0.getHand();
                ItemStack $$3 = this.player.getItemInHand($$2);
                if ($$3.getItem() instanceof FogBlockItem) {
                    ROUNDABOUT$MAX_INTERACTION_DISTANCE = Mth.square(ClientNetworking.getAppropriateConfig().justiceFogAndPilotRange + 15);
                    roundabout$handleUseItemOn($$0);
                }
                ci.cancel();
            }
        }
    }

    @Unique
    public double ROUNDABOUT$MAX_INTERACTION_DISTANCE = Mth.square(100.0);
    @Unique
    public void roundabout$handleUseItemOn(ServerboundUseItemOnPacket $$0) {
        PacketUtils.ensureRunningOnSameThread($$0, ((ServerGamePacketListener)(Object)this), this.player.serverLevel());
        this.player.connection.ackBlockChangesUpTo($$0.getSequence());
        ServerLevel $$1 = this.player.serverLevel();
        InteractionHand $$2 = $$0.getHand();
        ItemStack $$3 = this.player.getItemInHand($$2);
        if ($$3.isItemEnabled($$1.enabledFeatures())) {
            BlockHitResult $$4 = $$0.getHitResult();
            Vec3 $$5 = $$4.getLocation();
            BlockPos $$6 = $$4.getBlockPos();
            Vec3 $$7 = Vec3.atCenterOf($$6);
            if (!(this.player.getEyePosition().distanceToSqr($$7) > ROUNDABOUT$MAX_INTERACTION_DISTANCE)) {
                Roundabout.LOGGER.info("6");
                Vec3 $$8 = $$5.subtract($$7);
                double $$9 = 1.0000001;
                if (Math.abs($$8.x()) < 1.0000001 && Math.abs($$8.y()) < 1.0000001 && Math.abs($$8.z()) < 1.0000001) {
                    Roundabout.LOGGER.info("7");
                    Direction $$10 = $$4.getDirection();
                    this.player.resetLastActionTime();
                    int $$11 = this.player.level().getMaxBuildHeight();
                    if ($$6.getY() < $$11) {
                        if (this.awaitingPositionFromClient == null
                                && this.player.distanceToSqr((double)$$6.getX() + 0.5, (double)$$6.getY() + 0.5, (double)$$6.getZ() + 0.5) < ROUNDABOUT$MAX_INTERACTION_DISTANCE
                                && $$1.mayInteract(this.player, $$6)) {
                            InteractionResult $$12 = this.player.gameMode.useItemOn(this.player, $$1, $$3, $$2, $$4);
                            if ($$10 == Direction.UP && !$$12.consumesAction() && $$6.getY() >= $$11 - 1 && wasBlockPlacementAttempt(this.player, $$3)) {
                                Component $$13 = Component.translatable("build.tooHigh", $$11 - 1).withStyle(ChatFormatting.RED);
                                this.player.sendSystemMessage($$13, true);
                            } else if ($$12.shouldSwing()) {
                                this.player.swing($$2, true);
                            }
                        }
                    } else {
                        Component $$14 = Component.translatable("build.tooHigh", $$11 - 1).withStyle(ChatFormatting.RED);
                        this.player.sendSystemMessage($$14, true);
                    }

                    this.player.connection.send(new ClientboundBlockUpdatePacket($$1, $$6));
                    this.player.connection.send(new ClientboundBlockUpdatePacket($$1, $$6.relative($$10)));
                }
            }
        }
    }
}
