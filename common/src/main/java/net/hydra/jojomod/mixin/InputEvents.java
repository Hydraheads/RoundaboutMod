package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IGameRenderer;
import net.hydra.jojomod.access.IPlayerEntity;
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
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.BlockHitResult;
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

    @Shadow
    @Final
    public ParticleEngine particleEngine;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Unique
    public boolean roundabout$activeMining = false;

    /** This class is in part for detecting and canceling mouse inputs during stand attacks.
     * Please note this should
     * only apply to non-enhancer stands while their physical body is out.*/
        @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutAttack(CallbackInfoReturnable<Boolean> ci) {
            //handleInputEvents
            if (player != null) {
                StandUser standComp = ((StandUser) player);
                boolean isMining = (standComp.getActivePower() == PowerIndex.MINING);
                if (standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    ci.setReturnValue(true);
                } else if (standComp.getActive() && standComp.getStandPowers().interceptAttack()){
                    if (this.hitResult != null) {
                        boolean $$1 = false;
                        if (isMining) {
                            ci.setReturnValue(true);
                            return;
                        } else {
                            switch (this.hitResult.getType()) {
                                case BLOCK:
                                    BlockHitResult $$2 = (BlockHitResult) this.hitResult;
                                    BlockPos $$3 = $$2.getBlockPos();
                                    if (!this.level.getBlockState($$3).isAir()) {
                                        this.gameMode.startDestroyBlock($$3, $$2.getDirection());
                                        if (this.level.getBlockState($$3).isAir()) {
                                            $$1 = true;
                                        }
                                        break;
                                    }
                            }
                        }
                        ci.setReturnValue($$1);

                    }
                }
                //while (this.options.attackKey.wasPressed()) {
                //}
            }
        }




        @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutBlockBreak(boolean $$0, CallbackInfo ci) {
            if (player != null) {
                StandUser standComp = ((StandUser) player);
                boolean isMining = (standComp.getActivePower() == PowerIndex.MINING);
                if (standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                    if (!$$0){
                        this.missTime = 0;
                    }
                    if (this.gameMode != null) {
                        this.gameMode.stopDestroyBlock();
                    }
                    ci.cancel();
                } else if (standComp.getActive() && standComp.getStandPowers().interceptAttack()){
                    if (isMining) {
                        if (!this.player.isUsingItem()) {
                            if ($$0 && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
                                roundabout$activeMining = true;
                                BlockHitResult $$1 = (BlockHitResult) this.hitResult;
                                BlockPos $$2 = $$1.getBlockPos();
                                if (!this.level.getBlockState($$2).isAir()) {
                                    Direction $$3 = $$1.getDirection();
                                    if (this.gameMode.continueDestroyBlock($$2, $$3)) {
                                        this.particleEngine.crack($$2, $$3);
                                    }
                                }
                            } else {
                                standComp.tryPower(PowerIndex.NONE, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);

                                this.gameMode.stopDestroyBlock();
                            }
                        }
                        ci.cancel();
                    } else {
                        if (!this.options.keyAttack.isDown()){
                            roundabout$activeMining = false;
                        } else {
                            ci.cancel();
                        }
                    }
                }
            }
        }
        @Inject(method = "startUseItem", at = @At("TAIL"), cancellable = true)
        public void roundabout$DoItemUse(CallbackInfo ci) {
            if (player != null) {
                roundabout$TryGuard();
            }
        }

        @Unique
        public boolean roundabout$TryGuard(){
            StandUser standComp = ((StandUser) player);
            if (standComp.getActive() && standComp.getStandPowers().interceptGuard()) {
                return standComp.getStandPowers().buttonInputGuard(this.options.keyRight.isDown(),this.options);
            }
            return false;
        }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void roundabout$tick(CallbackInfo ci) {
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void roundaboutDoItemUseCancel(CallbackInfo ci) {
        if (player != null) {

            StandUser standComp = ((StandUser) player);

            if (standComp.isDazed() || ((TimeStop)player.level()).CanTimeStopEntity(player)) {
                ci.cancel();
            } else if (standComp.getActive()) {
                if (standComp.isGuarding() || standComp.isBarraging() || standComp.isClashing() || standComp.getStandPowers().cancelItemUse()) {
                    ci.cancel();
                }
            }


            if (level != null) {
                if (!this.player.isHandsBusy()) {
                    if (this.hitResult != null) {

                        for (InteractionHand $$0 : InteractionHand.values()) {
                            ItemStack $$1 = this.player.getItemInHand($$0);
                if ((((IPlayerEntity)player).roundabout$getDodgeTime() > -1 ||
                                    ((StandUser)player).roundabout$getLeapTicks() > -1) &&
                                    ($$1.getItem().isEdible()
                                            || ($$1.getItem() instanceof PotionItem))){
                    ci.cancel();
                                return;
                            }
            }}}}


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
                                                roundabout$TryGuard();
                                                ci.cancel();
                                            }
                                        } else if ($$1.getItem() instanceof EndCrystalItem || $$1.getItem() instanceof MinecartItem
                                                || $$1.getItem() instanceof BucketItem){
                                            roundabout$TryGuard();
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
                                            roundabout$TryGuard();
                                            ci.cancel();

                                            if ((((IPlayerEntity)player).roundabout$getDodgeTime() > -1 ||
                                                    ((StandUser)player).roundabout$getLeapTicks() > -1) &&
                                                    ($$1.getItem().isEdible()
                                                            || ($$1.getItem() instanceof PotionItem))){
                                                return;
                                            }

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
        ((StandUser)player).roundabout$setTSJump(roundaboutTSJump);
        ModPacketHandler.PACKET_ACCESS.timeStopFloat(roundaboutTSJump);
    }

    @Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
    public void roundaboutInput(CallbackInfo ci){
        if (player != null) {

            if (player.isAlive()) {
                //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());

                /**Time Stop Levitation*/
                boolean TSJumping = ((StandUser)player).roundabout$getTSJump();
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
                        //((IGameRenderer)this.gameRenderer).roundabout$loadEffect(new ResourceLocation("shaders/post/spider.json"));
                        KeyInputs.summonKey(player,((Minecraft) (Object) this));
                    }

                    KeyInputs.MoveKey1(player,((Minecraft) (Object) this),sameKeyOne(KeyInputRegistry.abilityOneKey),
                        this.options);

                    KeyInputs.MoveKey2(player,((Minecraft) (Object) this),sameKeyOne(KeyInputRegistry.abilityTwoKey),
                        this.options);

                    KeyInputs.MoveKey3(player,((Minecraft) (Object) this),sameKeyOne(KeyInputRegistry.abilityThreeKey),
                        this.options);

                    KeyInputs.MoveKey4(player,((Minecraft) (Object) this),sameKeyOne(KeyInputRegistry.abilityFourKey),
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

                    boolean isMining = (standComp.getActivePower() == PowerIndex.MINING);
                    Entity TE = standComp.getTargetEntity(player, -1);
                    if (!isMining && TE == null && this.hitResult != null && !this.player.isHandsBusy()
                    && (standComp.getActivePower() == PowerIndex.NONE || standComp.getAttackTimeDuring() == -1)
                    && !standComp.isGuarding()) {
                        boolean $$1 = false;
                        switch (this.hitResult.getType()) {
                            case ENTITY:
                                this.gameMode.attack(this.player, ((EntityHitResult) this.hitResult).getEntity());
                                break;
                            case BLOCK:
                                BlockHitResult $$2 = (BlockHitResult) this.hitResult;
                                BlockPos $$3 = $$2.getBlockPos();
                                if (!this.level.getBlockState($$3).isAir()) {
                                    this.gameMode.startDestroyBlock($$3, $$2.getDirection());
                                    standComp.tryPower(PowerIndex.MINING, true);
                                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.MINING);
                                    break;
                                }
                        }
                    }
                    if (!isMining && !roundabout$activeMining && standComp.getInterruptCD()) {
                        standComp.getStandPowers().buttonInputAttack(this.options.keyAttack.isDown(),this.options);
                    }

                    if (!isMining && standComp.isGuarding() && !standComp.isBarraging()){
                        standComp.getStandPowers().buttonInputBarrage(this.options.keyAttack.isDown(),this.options);
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
