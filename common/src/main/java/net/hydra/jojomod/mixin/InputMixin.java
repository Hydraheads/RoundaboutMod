package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class InputMixin {

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
                if (standComp.isDazed()) {
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
                if (standComp.getActive() || standComp.isDazed()) {
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
                        ModPacketHandler.PACKET_ACCESS.StandGuardClientPacket();
                        standComp.tryPower(PowerIndex.GUARD,true);
                    }
                }
            }
        }
    @Inject(method = "startUseItem", at = @At("Head"), cancellable = true)
    public void roundaboutDoItemUseCancel(CallbackInfo ci) {
        if (player != null) {
            StandUser standComp = ((StandUser) player);
            if (standComp.isDazed()) {
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
                if (standComp.getActive()) {
                    if (this.options.keyAttack.isDown() && !player.isUsingItem()) {

                        if (standComp.getInterruptCD()) {
                            if (standComp.canAttack()) {
                                ModPacketHandler.PACKET_ACCESS.StandAttackPacket();
                                standComp.tryPower(PowerIndex.ATTACK, true);
                            }
                        }

                        if (standComp.isGuarding() && !standComp.isBarraging()
                                && (standComp.getAttackTime() >= standComp.getAttackTimeMax() ||
                                (standComp.getActivePowerPhase() != standComp.getActivePowerPhaseMax()))){
                            ModPacketHandler.PACKET_ACCESS.StandBarragePacket();
                            standComp.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                        }
                    }
                }
                    //this.handleStandRush(this.currentScreen == null && this.options.attackKey.isPressed());
            }
        }
}
