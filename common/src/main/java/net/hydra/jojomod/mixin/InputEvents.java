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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class InputEvents {

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
                roundaboutTryGuard();
            }
        }

        @Unique
        public void roundaboutTryGuard(){
            StandUser standComp = ((StandUser) player);
            if (standComp.getActive()) {
                if (!standComp.isGuarding() && !standComp.isBarraging() && !standComp.isClashing()) {
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
                    standComp.tryPower(PowerIndex.GUARD,true);
                }
            }
        }


    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Nullable
    public ClientLevel level;

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



            if (level != null && ((TimeStop) level).isTimeStoppingEntity(player)) {
                if (!this.player.isHandsBusy()) {
                    if (this.hitResult != null) {

                        for (InteractionHand $$0 : InteractionHand.values()) {
                            ItemStack $$1 = this.player.getItemInHand($$0);
                            if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                                return;
                            }

                            if (this.hitResult != null) {
                                switch (this.hitResult.getType()) {
                                    case ENTITY:
                                        EntityHitResult $$2 = (EntityHitResult) this.hitResult;
                                        Entity $$3 = $$2.getEntity();
                                        if ($$3 instanceof LivingEntity &&
                                                !($$3 instanceof ArmorStand)){
                                            roundaboutTryGuard();
                                            ci.cancel();
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    public void roundaboutSetTSJump(boolean roundaboutTSJump){
        ((StandUser)player).roundaboutSetTSJump(roundaboutTSJump);
        ModPacketHandler.PACKET_ACCESS.timeStopFloat(roundaboutTSJump);
    }

    @Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
    public void roundaboutInput(CallbackInfo ci){
        if (player != null) {

            if (player.isAlive()) {
                //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());

                /**Time Stop Levitation*/
                boolean TSJumping = ((StandUser)player).roundaboutGetTSJump();
                if (((TimeStop)player.level()).isTimeStoppingEntity(player)) {
                    if (TSJumping && player.onGround()) {
                        TSJumping = false;
                        this.roundaboutSetTSJump(false);
                    }
                    if (options.keyJump.isDown()) {
                        if (player.getDeltaMovement().y <= 0 && !player.onGround()) {
                            TSJumping = true;
                            this.roundaboutSetTSJump(true);
                        }
                    } else {
                        TSJumping = false;
                        this.roundaboutSetTSJump(false);
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
                        this.roundaboutSetTSJump(false);
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
