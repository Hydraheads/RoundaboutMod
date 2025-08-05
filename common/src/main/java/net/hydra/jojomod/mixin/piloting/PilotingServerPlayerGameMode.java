package net.hydra.jojomod.mixin.piloting;

import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ServerPlayerGameMode.class)
public abstract class PilotingServerPlayerGameMode {

    /**Piloting changes the item use function so that it instead checks for distance from the piloting entity
     * rather than from the player*/

    /**prevents door interactions with justice*/
    @Inject(method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$useItemOn(ServerPlayer serverPlayer, Level level, ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        if (serverPlayer != null && ((StandUser)serverPlayer).roundabout$getStandPowers() instanceof PowersJustice PJ && PJ.isPiloting()) {
            InteractionResult interactionResult2;
            InteractionResult interactionResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.getBlock().isEnabled(level.enabledFeatures())) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
            if (this.gameModeForPlayer == GameType.SPECTATOR) {
                MenuProvider menuProvider = blockState.getMenuProvider(level, blockPos);
                if (menuProvider != null) {
                    serverPlayer.openMenu(menuProvider);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
                cir.setReturnValue(InteractionResult.PASS);
            }
            boolean bl = !serverPlayer.getMainHandItem().isEmpty() || !serverPlayer.getOffhandItem().isEmpty();
            boolean bl2 = serverPlayer.isSecondaryUseActive() && bl;
            ItemStack itemStack2 = itemStack.copy();
            if (!bl2) {
                cir.setReturnValue(InteractionResult.PASS);
            }
            if (itemStack.isEmpty() || serverPlayer.getCooldowns().isOnCooldown(itemStack.getItem())) {
                cir.setReturnValue(InteractionResult.PASS);
            }
            UseOnContext useOnContext = new UseOnContext(serverPlayer, interactionHand, blockHitResult);
            if (this.isCreative()) {
                int i = itemStack.getCount();
                interactionResult2 = itemStack.useOn(useOnContext);
                itemStack.setCount(i);
            } else {
                interactionResult2 = itemStack.useOn(useOnContext);
            }
            if (interactionResult2.consumesAction()) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack2);
            }
            cir.setReturnValue(interactionResult2);
        }
    }
    @Inject(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$handleBlockBreakActionM(BlockPos $$0, ServerboundPlayerActionPacket.Action $$1, Direction $$2, int $$3, int $$4, CallbackInfo ci) {
        if (this.player != null) { StandUser standComp = ((StandUser) player);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (powers.isPiloting() && piloting != null && piloting.isAlive() && !piloting.isRemoved() && powers instanceof PowersJustice) {
                BlockState $$6 = this.level.getBlockState($$0);
                if (!$$6.isAir() && $$6.getBlock() instanceof FogBlock) {
                    roundabout$handleBlockBreakAction($$0,$$1,$$2,$$3,$$4);
                }
                ci.cancel();
            }
        }
    }

    @Unique
    public void roundabout$handleBlockBreakAction(BlockPos $$0, ServerboundPlayerActionPacket.Action $$1, Direction $$2, int $$3, int $$4) {

        double ROUNDABOUT$MAX_INTERACTION_DISTANCE = Mth.square(ClientNetworking.getAppropriateConfig().justiceSettings.fogAndPilotRange+15);
        if (this.player.getEyePosition().distanceToSqr(Vec3.atCenterOf($$0)) > ROUNDABOUT$MAX_INTERACTION_DISTANCE) {
        } else if ($$0.getY() >= $$3) {
            this.player.connection.send(new ClientboundBlockUpdatePacket($$0, this.level.getBlockState($$0)));
        } else {
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


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

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
}
