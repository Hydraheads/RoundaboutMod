package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.WebBlock;
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
                    standComp.tryPower(PowerIndex.GUARD,true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
                }
            }
        }


    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Final
    public GameRenderer gameRenderer;


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


            if (level != null && ((StandUserClientPlayer) player).getRoundaboutNoPlaceTSTicks() > -1) {
                if (!this.player.isHandsBusy()) {
                    if (this.hitResult != null) {

                        for (InteractionHand $$0 : InteractionHand.values()) {
                            ItemStack $$1 = this.player.getItemInHand($$0);
                            if (!$$1.isItemEnabled(this.level.enabledFeatures())) {
                                return;
                            }

                            if (this.hitResult != null) {
                                switch (this.hitResult.getType()) {
                                    case BLOCK:
                                        if ($$1.getItem() instanceof BlockItem) {
                                            Block block = ((BlockItem) $$1.getItem()).getBlock();
                                            if (block instanceof BedBlock || block instanceof WebBlock || block instanceof RespawnAnchorBlock
                                                    || block.defaultDestroyTime() > 20){
                                                roundaboutTryGuard();
                                                ci.cancel();
                                            }
                                        } else if ($$1.getItem() instanceof EndCrystalItem || $$1.getItem() instanceof MinecartItem
                                                || $$1.getItem() instanceof BucketItem){
                                            roundaboutTryGuard();
                                            ci.cancel();
                                        }
                                }
                            }
                        }
                    }
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
                                        if ($$3 instanceof LivingEntity){
                                            roundaboutTryGuard();
                                            ci.cancel();

                                            if (!$$1.isEmpty()) {
                                                InteractionResult $$8 = this.gameMode.useItem(this.player, $$0);
                                                if ($$8.consumesAction()) {
                                                    if ($$8.shouldSwing()) {
                                                        this.player.swing($$0);
                                                    }

                                                    this.gameRenderer.itemInHandRenderer.itemUsed($$0);
                                                    return;
                                                }
                                            }
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
                    if (player.getAbilities().flying && TSJumping) {
                        this.roundaboutSetTSJump(false);
                    } else {
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
                    if (sameKeyOne(KeyInputRegistry.summonKey)) {
                        KeyInputs.summonKey(player,((Minecraft) (Object) this));
                    }
                    if (sameKeyOne(KeyInputRegistry.abilityOneKey)) {
                        //client.player.sendMessage(Text.of("Ability Key"));
                    }
                    if (sameKeyOne(KeyInputRegistry.abilityTwoKey)) {
                        //client.player.sendMessage(Text.of("Ability Key 2"));
                    }
                    if (sameKeyOne(KeyInputRegistry.abilityThreeKey)) {
                        //client.player.sendMessage(Text.of("Ability Key 3"));
                    }

                    KeyInputs.specialMoveKey(player,((Minecraft) (Object) this),sameKeyOne(KeyInputRegistry.abilityFourKey),
                            this.options);

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
                    standComp.tryPower(PowerIndex.NONE,true);
                   ModPacketHandler.PACKET_ACCESS.StandGuardCancelClientPacket();
                }
            }
            if (standComp.getActive() && !((TimeStop)player.level()).CanTimeStopEntity(player)) {
                if (this.options.keyAttack.isDown() && !player.isUsingItem()) {

                    if (standComp.getInterruptCD()) {
                        if (standComp.canAttack()) {
                            standComp.tryPower(PowerIndex.ATTACK, true);
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.ATTACK);
                        }
                    }

                    if (standComp.isGuarding() && !standComp.isBarraging()
                            && (standComp.getAttackTime() >= standComp.getAttackTimeMax() ||
                            (standComp.getActivePowerPhase() != standComp.getActivePowerPhaseMax()))){
                        standComp.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BARRAGE_CHARGE);
                    }
                }
            }
                //this.handleStandRush(this.currentScreen == null && this.options.attackKey.isPressed());
        }
    }

    public boolean sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.options.keyLoadHotbarActivator) && this.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.options.keySaveHotbarActivator) && this.options.keySaveHotbarActivator.isDown()));
    }

}
