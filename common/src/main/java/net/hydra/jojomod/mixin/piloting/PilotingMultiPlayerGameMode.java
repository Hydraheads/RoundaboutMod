package net.hydra.jojomod.mixin.piloting;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiPlayerGameMode.class)
public abstract class PilotingMultiPlayerGameMode {

    /**Piloting stand logic changes item use logic to be at a new location*/

    @Inject(method = "performUseItemOn", at = @At("HEAD"), cancellable = true)
    public void roundabout$performUseItemOn(LocalPlayer $$0, InteractionHand $$1, BlockHitResult $$2, CallbackInfoReturnable<InteractionResult> cir) {
        if ($$0 != null && ((StandUser)$$0).roundabout$getStandPowers() instanceof PowersJustice PJ && PJ.isPiloting() && this.minecraft != null){

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



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    private GameType localPlayerMode;

    @Shadow protected abstract void ensureHasSentCarriedItem();

    @Shadow protected abstract void startPrediction(ClientLevel $$0, PredictiveAction $$1);

    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private ClientPacketListener connection;
}
