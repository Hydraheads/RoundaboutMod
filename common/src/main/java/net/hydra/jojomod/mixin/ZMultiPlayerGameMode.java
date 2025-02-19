package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IInputEvents;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class ZMultiPlayerGameMode {

    @Shadow
    private boolean isDestroying;
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private ClientPacketListener connection;
    @Shadow
    private BlockPos destroyBlockPos = null;

    @Shadow
    private float destroyProgress;

    @Shadow private GameType localPlayerMode;

    @Shadow protected abstract void ensureHasSentCarriedItem();

    @Shadow protected abstract void startPrediction(ClientLevel $$0, PredictiveAction $$1);

    /**While your offhand is frozen in stone, you cannot use it*/
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$BlockBreak(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        byte curse = ((StandUser)$$0).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.OFF_HAND && $$0.getMainArm() == HumanoidArm.RIGHT && $$1 == InteractionHand.OFF_HAND)
                    || (curse == LocacacaCurseIndex.MAIN_HAND && $$0.getMainArm() == HumanoidArm.LEFT && $$1 == InteractionHand.OFF_HAND)) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
    @Inject(method = "performUseItemOn", at = @At("HEAD"), cancellable = true)
    public void roundabout$performUseItemOn(LocalPlayer $$0, InteractionHand $$1, BlockHitResult $$2, CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser)$$0).roundabout$getStandPowers() instanceof PowersJustice PJ && PJ.isPiloting()){
            BlockPos $$3 = $$2.getBlockPos();
            ItemStack $$4 = $$0.getItemInHand($$1);
            if (this.localPlayerMode == GameType.SPECTATOR) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                boolean $$5 = !$$0.getMainHandItem().isEmpty() || !$$0.getOffhandItem().isEmpty();
                boolean $$6 = $$0.isSecondaryUseActive() && $$5;
                if (!$$6) {
                    BlockState $$7 = this.minecraft.level.getBlockState($$3);
                    if (!this.connection.isFeatureEnabled($$7.getBlock().requiredFeatures())) {
                        cir.setReturnValue(InteractionResult.FAIL);
                    }

                    cir.setReturnValue(InteractionResult.PASS);
                }

                if (!$$4.isEmpty() && !$$0.getCooldowns().isOnCooldown($$4.getItem())) {
                    UseOnContext $$9 = new UseOnContext($$0, $$1, $$2);
                    InteractionResult $$11;
                    if (this.localPlayerMode.isCreative()) {
                        int $$10 = $$4.getCount();
                        $$11 = $$4.useOn($$9);
                        $$4.setCount($$10);
                    } else {
                        $$11 = $$4.useOn($$9);
                    }

                    cir.setReturnValue($$11);
                } else {
                    cir.setReturnValue(InteractionResult.PASS);
                }
            }
        }
    }

    /**Prevents stand mining from making your vanilla attack cooldown reset*/
    @Inject(method = "releaseUsingItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$releaseUsingItem(Player pl, CallbackInfo ci) {
        if (((IInputEvents)Minecraft.getInstance()).roundabout$sameKeyTwo(KeyInputRegistry.guardKey)) {
            ci.cancel();
        }
    }
    /**Prevents stand mining from making your vanilla attack cooldown reset*/
    @Inject(method = "stopDestroyBlock()V", at = @At("HEAD"), cancellable = true)
    public void roundabout$stopDestroyBlock(CallbackInfo ci) {
        if (((StandUser) this.minecraft.player).roundabout$getActive() && ((StandUser) this.minecraft.player).roundabout$getStandPowers().canUseMiningStand()) {
            if (this.isDestroying) {
                BlockState $$0 = this.minecraft.level.getBlockState(this.destroyBlockPos);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, $$0, -1.0F);
                this.connection
                        .send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN));
                this.isDestroying = false;
                this.destroyProgress = 0.0F;
                this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
            }
            ci.cancel();
        }
    }
}
