package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class InputEvents {

    @Shadow
    @Final
    public Options options;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Shadow
    public int missTime;


    /** This class is in part for detecting and canceling mouse inputs during stand attacks.
     * Please note this should
     * only apply to non-enhancer stands while their physical body is out.*/
        @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutAttack(CallbackInfoReturnable<Boolean> ci) {
            //handleInputEvents
            if (player != null) {
                StandUser standComp = ((StandUser) player);
                if (standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    ci.setReturnValue(true);
                } else if (standComp.getActive()){
                    ci.setReturnValue(true);
                }
                //while (this.options.attackKey.wasPressed()) {
                //}
            }
        }
        @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutBlockBreak(boolean $$0, CallbackInfo ci) {
            if (player != null) {
                StandUser standComp = ((StandUser) player);
                if (standComp.getActive() || standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    if (!$$0){
                        this.missTime = 0;
                    }
                    if (this.gameMode != null) {
                        this.gameMode.stopDestroyBlock();
                    }
                    ci.cancel();
                }
            }
        }
        @Inject(method = "startUseItem", at = @At("TAIL"), cancellable = true)
        public void roundaboutDoItemUse(CallbackInfo ci) {
            if (player != null) {
                StandUser standComp = ((StandUser) player);
                if (standComp.getActive()) {
                    if (!standComp.isGuarding() && !standComp.isBarraging() && !standComp.isClashing()) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
                        standComp.tryPower(PowerIndex.GUARD,true);
                    }
                }
            }
        }


    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void roundaboutDoItemUseCancel(CallbackInfo ci) {
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            if (standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                ci.cancel();
            } else if (standComp.getActive()) {
                if (standComp.isGuarding() || standComp.isBarraging() || standComp.isClashing()) {
                    ci.cancel();
                }
            }
        }
    }

        @Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
        public void roundaboutInput(CallbackInfo ci){
            if (player != null) {

                if (player.isAlive()) {
                    //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());

                    /*Time Stop Levitation*/
                    boolean TSJumping = ((StandUser)player).roundaboutGetTSJump();
                    if (((TimeStop)player.level()).isTimeStoppingEntity(player)) {
                        if (TSJumping && player.onGround()) {
                            ((StandUser)player).roundaboutSetTSJump(false);
                        }
                        if (options.keyJump.isDown()) {
                            if (player.getDeltaMovement().y <= 0 && !player.onGround()) {
                                TSJumping = true;
                                ((StandUser) player).roundaboutSetTSJump(TSJumping);
                            }
                        } else {
                            TSJumping = false;
                            ((StandUser) player).roundaboutSetTSJump(TSJumping);
                        }

                            if (TSJumping) {
                                if (options.keyJump.isDown()) {
                                    float cooking = (float) (player.getDeltaMovement().y + 0.2);
                                    if (options.keyShift.isDown()) {
                                        if (cooking >= 0.0001) {
                                            cooking = 0.0001F;
                                        }
                                    } else {
                                        if (cooking >= 0.1) {
                                            cooking = 0.1F;
                                        }
                                    }
                                    player.setDeltaMovement(
                                            player.getDeltaMovement().x,
                                            cooking,
                                            player.getDeltaMovement().z
                                    );
                                }
                            }
                    } else {
                        if (TSJumping) {
                            ((StandUser)player).roundaboutSetTSJump(false);
                        }
                    }
                    /*If you have a stand out, update the stand leaning attributes.
                     * Currently, strafe is reported, but unused.*/
                    if (((StandUser) player).getActive()) {
                        StandEntity stand = ((StandUser) player).getStand();
                        if (stand != null) {
                            var mf = stand.getMoveForward();
                            byte forward = 0;
                            byte strafe = 0;
                            if (options.keyUp.isDown()) forward++;
                            if (options.keyDown.isDown()) forward--;
                            if (options.keyLeft.isDown()) strafe++;
                            if (options.keyRight.isDown()) strafe--;

                            if (mf != forward) {
                                ModPacketHandler.PACKET_ACCESS.moveSyncPacket(forward,strafe);
                            }
                        }
                    }

                        //RoundaboutMod.LOGGER.info("px");
                        if (KeyInputRegistry.summonKey.isDown()) {
                            KeyInputs.summonKey(player,((Minecraft) (Object) this));
                        }
                        if (KeyInputRegistry.abilityOneKey.isDown()) {
                            //client.player.sendMessage(Text.of("Ability Key"));
                        }
                        if (KeyInputRegistry.abilityTwoKey.isDown()) {
                            //client.player.sendMessage(Text.of("Ability Key 2"));
                        }
                        if (KeyInputRegistry.abilityThreeKey.isDown()) {
                            //client.player.sendMessage(Text.of("Ability Key 3"));
                        }
                        if (KeyInputRegistry.abilityFourKey.isDown()) {
                            KeyInputs.specialMoveKey(player,((Minecraft) (Object) this));
                        }
                        if (KeyInputRegistry.menuKey.isDown()) {
                            KeyInputs.menuKey(player,((Minecraft) (Object) this));
                        }
                        if (KeyInputs.roundaboutClickCount > 0) {
                            KeyInputs.roundaboutClickCount--;
                        }

                }

                StandUser standComp = ((StandUser) player);
                if (!this.options.keyUse.isDown()) {
                    if (standComp.isGuarding() || standComp.isBarraging()) {
                            /*This code makes it so there is a slight delay between blocking and subsequent punch chain attacks.
                            * This delay exists so you can't right click left click chain for instant full power punches.*/
                       if (standComp.getActivePowerPhase() > 0 ) {
                           standComp.setInterruptCD(3);
                       }
                       ModPacketHandler.PACKET_ACCESS.StandGuardCancelClientPacket();
                       standComp.tryPower(PowerIndex.NONE,true);
                    }
                }
                if (standComp.getActive() && !((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    if (this.options.keyAttack.isDown() && !player.isUsingItem()) {

                        if (standComp.getInterruptCD()) {
                            if (standComp.canAttack()) {
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.ATTACK);
                                standComp.tryPower(PowerIndex.ATTACK, true);
                            }
                        }

                        if (standComp.isGuarding() && !standComp.isBarraging()
                                && (standComp.getAttackTime() >= standComp.getAttackTimeMax() ||
                                (standComp.getActivePowerPhase() != standComp.getActivePowerPhaseMax()))){
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BARRAGE_CHARGE);
                            standComp.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                        }
                    }
                }
                    //this.handleStandRush(this.currentScreen == null && this.options.attackKey.isPressed());
            }
        }
}
