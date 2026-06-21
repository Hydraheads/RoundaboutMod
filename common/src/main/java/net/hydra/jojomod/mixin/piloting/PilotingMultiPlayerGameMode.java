package net.hydra.jojomod.mixin.piloting;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandPowers;
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
    public void roundabout$performUseItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        StandPowers powers = ((StandUser)player).roundabout$getStandPowers();
        if (powers != null && powers.isPiloting() && this.minecraft != null){

            BlockPos hitresult = blockHitResult.getBlockPos();
            ItemStack itemStack = player.getItemInHand(hand);
            if (powers.canPilotPlaceBlock(itemStack)) {
                if (this.localPlayerMode == GameType.SPECTATOR) {
                    cir.setReturnValue(InteractionResult.SUCCESS);
                } else {
                    boolean $$5 = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
                    boolean $$6 = player.isSecondaryUseActive() && $$5;
                    if (!$$6) {
                        BlockState blockState = this.minecraft.level.getBlockState(hitresult);
                        if (!this.connection.isFeatureEnabled(blockState.getBlock().requiredFeatures())) {
                            cir.setReturnValue(InteractionResult.FAIL);
                        }

                        cir.setReturnValue(InteractionResult.PASS);
                    }

                    if (!itemStack.isEmpty() && !player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                        UseOnContext context = new UseOnContext(player, hand, blockHitResult);
                        InteractionResult interactionResult;
                        if (this.localPlayerMode.isCreative()) {
                            int $$10 = itemStack.getCount();
                            interactionResult = itemStack.useOn(context);
                            itemStack.setCount($$10);
                        } else {
                            interactionResult = itemStack.useOn(context);
                        }

                        cir.setReturnValue(interactionResult);
                    } else {
                        cir.setReturnValue(InteractionResult.PASS);
                    }
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
